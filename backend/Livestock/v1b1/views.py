from db_connection import db
from flask import Blueprint, request, jsonify, current_app
from sqlalchemy import func, desc
from sqlalchemy.orm import joinedload, subqueryload
from Livestock.models import Livestock
from Livestock.schema import LivestockSchema
from Livestock.schema import LivestockSchema_new
from Descendant.schema import DescendantSchema

from Descendant.models import Descendant

from ProductHasCategory.models import ProductHasCategory
from Product.models import Product
from SKU.models import SKU

from FarmProfile.HasLivestock.models import HasLivestock as FarmProfileHasLivestock
from FarmProfile.models import FarmProfileHasUsers

from Sled.models import Sled
from BlockArea.models import BlockArea
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from Record.FeedingRecord.models import FeedingRecord

from datetime import datetime

from utils.index import get_feed_category_label, remove_duplicates

from auth import login_required, current_farm_profile

from decimal import Decimal
from babel.dates import format_date

views_bp = Blueprint('views_livestock_v1b1', __name__)

livestock_schema = LivestockSchema()
livestocks_schema = LivestockSchema(many=True)

livestock_schema_new = LivestockSchema_new()
livestocks_schema_new = LivestockSchema_new(many=True)


@views_bp.route('/search-livestocks', methods=['GET'])
@login_required
def get_search_livestocks():
    # Retrieve all livestock records from the database
    search_params = request.args.get('search_params')
    farm_profile_id = current_farm_profile()

    try:
        query = FarmProfileHasLivestock.query.options(subqueryload(FarmProfileHasLivestock.livestock)) \
            .filter_by(farm_profile_id=farm_profile_id) \
            .all()
        #    .filter(Livestock.name.like(f'%{search_params}%')) \

        if not query:
            e = 'You dont have any livestock'
            response = {
                'status': 'error',
                'message': e
            }

            return jsonify(response), 404

        results = []
        # Serialize the livestock data using the schema
        for item in query:

            query_block_area_livestock = BlockAreaSledLivestock.query.filter_by(
                livestock_id=item.livestock_id).first()

            if hasattr(item, 'livestock'):
                date_obj = item.livestock.created_at

                # Format the date as "DD MMMM YYYY"
                formatted_date = date_obj.strftime("%d %B %Y")

                if query_block_area_livestock:
                    data = {
                        'id': item.livestock.id,
                        'name': item.livestock.name,
                        'bangsa': item.livestock.bangsa,
                        'info': f'Tinggal di kandang {query_block_area_livestock.sled.name} di {query_block_area_livestock.block_area.name}'
                    }
                else:
                    data = {
                        'id': item.livestock.id,
                        'name': item.livestock.name,
                        'info': f'Belum di taruh kandang',
                        'created_at': formatted_date,
                    }

                results.append(data)

        results = list(filter(lambda k: search_params in k['name'], results))

        result = livestocks_schema_new.dump(results)
        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to get livestock collections data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_livestock(livestock_id):

    try:
        query_block_area_livestock = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_id).first()

        query_d = Descendant.query.options([
            subqueryload(Descendant.livestock)
        ]).filter_by(livestock_id=livestock_id).first()

        descendant_result = None
        if query_d:
            parent_male_id = query_d.livestock_male_id if query_d.livestock_male_id is not None else None
            parent_male_name = f'{query_d.livestock_male_id}-{query_d.parent_male.name}' if query_d.livestock_male_id is not None else None
            parent_female_id = query_d.livestock_female_id if query_d.livestock_female_id is not None else None
            parent_female_name = f'{query_d.livestock_female_id}-{query_d.parent_female.name}' if query_d.livestock_female_id is not None else None

            descendant_result = {
                'id': query_d.id,
                'livestock_id': query_d.livestock_id,
                'livestock_name': query_d.livestock.name,
                'parent_male_id': parent_male_id,
                'parent_male_name': parent_male_name,
                'parent_female_id': parent_female_id,
                'parent_female_name': parent_female_name
            }

        if query_block_area_livestock is None:
            # Retrieve all livestock records from the database
            query = Livestock.query.options([
                subqueryload(Livestock.weight_records),
                subqueryload(Livestock.height_records),
                subqueryload(Livestock.bcs_records),
                subqueryload(Livestock.health_records)
            ]).get(livestock_id)

            result = {
                'id': query.id,
                'name': query.name,
                'gender': query.gender,
                'bangsa': query.bangsa,
                'birth_date': query.birth_date.strftime('%d-%m-%Y'),
                'info': f'Belum di taruh kandang | {query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
                'description': query.description,
                'bcs_records': [],
                'height_records': [],
                'weight_records': [],
                'feeding_records': [],
                'health_records': query.health_records,
                'descendant': descendant_result
            }

            results_bcs_records = []
            results_weight_records = []
            results_height_records = []
            prev_score = None

            if isinstance(query.bcs_records, list):
                for current_record in query.bcs_records:
                    data_bcs = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id-ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_bcs['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_bcs['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_bcs['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_bcs['prev_score'] = 0

                    results_bcs_records.append(data_bcs)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            if isinstance(query.weight_records, list):
                for current_record in query.weight_records:
                    data_weight = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id-ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = 0

                    results_weight_records.append(data_weight)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            if isinstance(query.height_records, list):
                for current_record in query.height_records:
                    data_height = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id-ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = 0

                    results_height_records.append(data_height)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            result['bcs_records'] = results_bcs_records[::-1]
            result['height_records'] = results_height_records[::-1]
            result['weight_records'] = results_weight_records[::-1]
            result['feeding_records'] = None

            # Serialize the livestock data using the schema
            result = livestock_schema.dump(result)
        else:
            results_feeding = []
            if (query_block_area_livestock.block_area_id is None and query_block_area_livestock.sled_id is not None) or query_block_area_livestock.block_area_id is not None:
                sled = Sled.query.get(query_block_area_livestock.sled_id)
                if sled.block_area_id is None:
                    results_feeding = []
                else:
                    query_block_area_livestock.block_area_id = sled.block_area_id
                    query_block_area = BlockArea.query.get(
                        query_block_area_livestock.block_area_id)
                    livestock_count = len(
                        query_block_area.livestock) if query_block_area.livestock is not None else 0

                    columns_to_select = [
                        FeedingRecord.feed_category,
                        FeedingRecord.block_area_id,
                        func.to_char(FeedingRecord.created_at,
                                     'DD Mon YYYY').label('day'),
                        func.sum(FeedingRecord.score).label('total_score')
                    ]

                    query_feeding = FeedingRecord.query \
                        .with_entities(*columns_to_select) \
                        .filter_by(block_area_id=query_block_area.id) \
                        .group_by(FeedingRecord.feed_category, FeedingRecord.block_area_id, func.to_char(FeedingRecord.created_at, 'DD Mon YYYY')) \
                        .order_by(desc(func.to_char(FeedingRecord.created_at, 'DD Mon YYYY'))) \
                        .all()

                    # Create a dictionary to group data by 'day'
                    day_map = {}

                    for item in query_feeding:
                        day = item.day
                        feed_category = item.feed_category
                        total_score = item.total_score

                        result = {
                            'day': day,
                            'block_area_id': item.block_area_id,
                            'feed_list': []
                        }

                        if day not in day_map:
                            day_map[day] = {
                                "day": day,
                                "block_area_id": item.block_area_id,
                                "feed_list": []
                            }

                        score = total_score / livestock_count
                        day_map[day]["feed_list"].append({
                            "feed_category": get_feed_category_label(feed_category),
                            "total_score": f'{score:.2f}'
                        })

                        result.update(day_map[day])
                        results_feeding.append(result)

            # Retrieve all livestock records from the database
            query = Livestock.query.options([
                subqueryload(Livestock.weight_records),
                subqueryload(Livestock.height_records),
                subqueryload(Livestock.bcs_records),
                subqueryload(Livestock.health_records)
            ]).get(livestock_id)

            sled = {'id': query_block_area_livestock.sled_id,
                    'name': query_block_area_livestock.sled.name} if query_block_area_livestock.sled is not None else {'id': "None", 'name': "None"}
            block_area = {'id': query_block_area_livestock.block_area_id,
                          'name': query_block_area_livestock.block_area.name} if query_block_area_livestock.block_area is not None else {'id': None, 'name': None}

            result = {
                'id': query.id,
                'name': query.name,
                'gender': query.gender,
                'bangsa': query.bangsa,
                'birth_date': format_date(query.birth_date, format='dd MMMM YYYY', locale='id_ID'),
                'info': f'Tinggal di kandang S-{sled["id"]} {sled["name"]} di blok BA-{block_area["id"]} {block_area["name"]} | {query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
                'description': query.description,
                'bcs_records': [],
                'weight_records': [],
                'height_records': [],
                'feeding_records': [],
                'health_records': query.health_records,
                'sled_id': query_block_area_livestock.sled_id,
                'block_area_id': query_block_area_livestock.block_area_id,
                'descendant': descendant_result
            }

            results_bcs_records = []
            results_height_records = []
            results_weight_records = []
            prev_score = None

            if isinstance(query.bcs_records, list):
                for current_record in query.bcs_records:
                    data_bcs = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id_ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_bcs['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_bcs['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_bcs['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_bcs['prev_score'] = 0

                    results_bcs_records.append(data_bcs)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            if isinstance(query.weight_records, list):
                for current_record in query.weight_records:
                    data_weight = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id_ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_weight['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_weight['prev_score'] = 0

                    results_weight_records.append(data_weight)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            if isinstance(query.height_records, list):
                for current_record in query.height_records:
                    data_height = {
                        'id': current_record.id,
                        'livestock_id': current_record.livestock_id,
                        'date': current_record.date,
                        'score': current_record.score,
                        'remarks': current_record.remarks,
                        'created_at': format_date(current_record.created_at, format='dd MMMM YYYY', locale='id_ID')
                    }

                    if prev_score is not None:
                        growth = current_record.score - prev_score
                        percentage = (growth / prev_score) * 100
                        # Format the percentage with two decimal places
                        data_height['growth'] = f'{percentage:.2f}%'
                        # Format the percentage with two decimal places
                        data_height['prev_score'] = prev_score if prev_score is not None else 0
                    else:
                        # Format the percentage with two decimal places
                        data_height['growth'] = f'{0:.2f}%'
                        # Format the percentage with two decimal places
                        data_height['prev_score'] = 0

                    results_height_records.append(data_height)
                    prev_score = current_record.score  # Update prev_score for the next iteration

            result['bcs_records'] = results_bcs_records[::-1]
            result['height_records'] = results_height_records[::-1]
            result['weight_records'] = results_weight_records[::-1]
            result['feeding_records'] = remove_duplicates(
                results_feeding, 'day')

            # Serialize the livestock data using the schema
            result = livestock_schema.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        current_app.logger.error(f"An error occurred: {error_message}")

        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get {livestock_id} livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/detail-livestock/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_livestock_new(livestock_id):

    try:
        query_block_area_livestock = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_id).first()

        query_d = Descendant.query.options([
            subqueryload(Descendant.livestock)
        ]).filter_by(livestock_id=livestock_id).first()

        descendant_result = None
        if query_d:
            parent_male_id = query_d.livestock_male_id if query_d.livestock_male_id is not None else None
            parent_male_name = f'{query_d.livestock_male_id}-{query_d.parent_male.name}' if query_d.livestock_male_id is not None else None
            parent_female_id = query_d.livestock_female_id if query_d.livestock_female_id is not None else None
            parent_female_name = f'{query_d.livestock_female_id}-{query_d.parent_female.name}' if query_d.livestock_female_id is not None else None

            descendant_result = {
                'id': query_d.id,
                'livestock_id': query_d.livestock_id,
                'livestock_name': query_d.livestock.name,
                'parent_male_id': parent_male_id,
                'parent_male_name': parent_male_name,
                'parent_female_id': parent_female_id,
                'parent_female_name': parent_female_name
            }

        if query_block_area_livestock is None:
            # Retrieve all livestock records from the database
            query = Livestock.query.get(livestock_id)

            result = {
                'id': query.id,
                'name': query.name,
                'gender': query.gender,
                'bangsa': query.bangsa,
                'birth_date': query.birth_date,
                'info': f'Belum di taruh kandang | {query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
                # 'sled_id': None,
                'sled_id': query_block_area_livestock.sled_id,
                # 'block_area_id': None,
                'block_area_id': query_block_area_livestock.block_area_id,
                'description': query.description,
                'descendant': descendant_result
            }

            # Serialize the livestock data using the schema
            result = livestock_schema_new.dump(result)
        else:
            # Retrieve all livestock records from the database
            query = Livestock.query.get(livestock_id)

            sled = {'id': query_block_area_livestock.sled_id,
                    'name': query_block_area_livestock.sled.name} if query_block_area_livestock.sled is not None else {'id': "None", 'name': "None"}
            block_area = {'id': query_block_area_livestock.block_area_id,
                          'name': query_block_area_livestock.block_area.name} if query_block_area_livestock.block_area is not None else {'id': None, 'name': None}

            result = {
                'id': query.id,
                'name': query.name,
                'gender': query.gender,
                'bangsa': query.bangsa,
                'birth_date': query.birth_date,
                'info': f'Tinggal di kandang S-{sled["id"]} {sled["name"]} di blok BA-{block_area["id"]} {block_area["name"]} | {query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
                # 'sled_id': None,
                'sled_id': query_block_area_livestock.sled_id,
                # 'block_area_id': None,
                'block_area_id': query_block_area_livestock.block_area_id,
                'description': query.description,
                'descendant': descendant_result
            }

            # Serialize the livestock data using the schema
            result = livestock_schema_new.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        current_app.logger.error(f"An error occurred: {error_message}")

        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get {livestock_id} livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock', methods=['POST'])
@login_required
def post_livestock():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    birth_date = data.get('birth_date') if data.get(
        'birth_date') is not None else "2023-01-30"
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

    try:
        farm_profile_id = current_farm_profile()

        if not farm_profile_id:
            raise Exception("Cannot find farm profile!")
        else:
            query = Livestock(name=name, gender=gender, birth_date=birth_date,
                              bangsa=bangsa, description=description)
            db.session.add(query)
            db.session.commit()

            product = Product(
                name=name, unit_measurement="ekor", description="none")
            db.session.add(product)
            db.session.commit()

            phc = ProductHasCategory(product_id=product.id, category_id=3)
            db.session.add(phc)
            db.session.commit()

            sku = SKU(product_id=product.id, name=name)
            db.session.add(sku)
            db.session.commit()

            has_livestock = FarmProfileHasLivestock(
                livestock_id=query.id, farm_profile_id=farm_profile_id)
            db.session.add(has_livestock)
            db.session.commit()

            descendant = Descendant(livestock_id=query.id)
            db.session.add(descendant)
            db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Hello, {name}! Your message has been received.',
                'livestock_id': query.id
            }

            return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, {name}! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock/move-to-sled', methods=['POST'])
@login_required
def update_livestock_sled():
    data = request.get_json()

    farm_profile_id = current_farm_profile()

    livestock_id = data.get('livestock_id')
    sled_id = data.get('sled_id')
    block_area_id = data.get('block_area_id') if data.get('block_area_id') != 0 else None

    try:
        # check whether livestock is belong to the farm_profile id
        query_fphl = FarmProfileHasLivestock.query.filter_by(
            farm_profile_id=farm_profile_id).first()

        if not query_fphl:
            raise Exception('It is not your livestock, this prohibited')

        # check livestock already placed on spesific sled and block_area
        query_basl = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_id).first()

        if not query_basl:
            # if its not will register a livestock to a block area and sled with provided data on body request
            BlockAreaSledLivestock(
                livestock_id=livestock_id, block_area_id=block_area_id, sled_id=sled_id)
        else:
            # if found then update
            query_basl.sled_id = sled_id
            query_basl.block_area_id = block_area_id
            db.session.commit()

        response = {
            'status': 'Success',
            'message': f'Your Livestock already moved to {sled_id} on {block_area_id}'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to update livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock/<int:livestock_id>', methods=['PUT'])
@login_required
def update_livestock(livestock_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    birth_date = data.get('birth_date')
    bangsa = data.get('bangsa')
    description = data.get('description')
    sled_id = data.get('sled_id')

    parent_male_id = data.get('parent_male_id')
    parent_female_id = data.get('parent_female_id')

    try:
        # Assuming you have a Livestock model and an existing livestock object
        livestock = Livestock.query.get(livestock_id)
        if livestock:
            livestock.name = name
            livestock.birth_date = birth_date
            livestock.gender = gender
            livestock.bangsa = bangsa
            livestock.description = description
            db.session.commit()

            descendant = Descendant.query.filter_by(
                livestock_id=livestock_id).first()

            if descendant:
                descendant.livestock_male_id = parent_male_id
                descendant.livestock_female_id = parent_female_id
                db.session.commit()
            else:
                new_descendant = Descendant(
                    livestock_id=livestock_id, livestock_male_id=parent_male_id, livestock_female_id=parent_female_id)
                db.session.add(new_descendant)
                db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Livestock {livestock_id} has been updated. sled {sled_id}'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Livestock {livestock_id} not found.'
            }
            return jsonify(response), 404

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, {name}! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock/<int:livestock_id>', methods=['DELETE'])
@login_required
def delete_livestock(livestock_id):

    try:
        # Assuming you have a Livestock model and an existing livestock object
        bashl = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_id).first()

        livestock = Livestock.query.get(livestock_id)

        if livestock:
            db.session.delete(livestock)
            db.session.commit()

            if bashl:
                db.session.delete(bashl)
                db.session.commit()

            response = {
                'status': 'success',
                'message': f'Livestock {livestock_id} has been deleted.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Livestock {livestock_id} not found.'
            }
            return jsonify(response), 404

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500
