from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import func, desc
from sqlalchemy.orm import joinedload, subqueryload
from Livestock.models import Livestock
from Livestock.schema import LivestockSchema
from Livestock.schema import LivestockSchema_new

from ProductHasCategory.models import ProductHasCategory
from Product.models import Product
from SKU.models import SKU

from FarmProfile.HasLivestock.models import HasLivestock as FarmProfileHasLivestock
from FarmProfile.models import FarmProfileHasUsers

from BlockArea.models import BlockArea
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from Record.FeedingRecord.models import FeedingRecord

from datetime import datetime

from utils.index import get_feed_category_label, remove_duplicates

from auth import login_required, current_farm_profile

views_bp = Blueprint('views_livestock', __name__)

livestock_schema = LivestockSchema()
livestocks_schema = LivestockSchema(many=True)

livestock_schema_new = LivestockSchema_new()
livestocks_schema_new = LivestockSchema_new(many=True)


@views_bp.route('/livestocks', methods=['GET'])
@login_required
def get_livestocks():
    # Retrieve all livestock records from the database
    farm_profile_id = current_farm_profile()

    try:
        query = FarmProfileHasLivestock.query.options([subqueryload(FarmProfileHasLivestock.livestock)]).filter_by(
            farm_profile_id=farm_profile_id).order_by(desc(FarmProfileHasLivestock.livestock_id)).all()

        results = []
        if not query:
            e = 'You dont have any livestock'
            response = {
                'status': 'error',
                'message': e
            }

            return jsonify(response)
        # Serialize the livestock data using the schema
        for item in query:
            if hasattr(item, 'livestock'):
                date_obj = item.livestock.created_at

                # Format the date as "DD MMMM YYYY"
                formatted_date = date_obj.strftime("%d %B %Y")

                data = {
                    'id': item.livestock.id,
                    'name': item.livestock.name,
                    'gender': item.livestock.gender,
                    'birth_date': item.livestock.birth_date,
                    'bangsa': item.livestock.bangsa,
                    'info': f'{item.livestock.get_gender_label()} | {item.livestock.calculate_age()} | Bangsa {item.livestock.bangsa}',
                    'description': item.livestock.description,
                    'created_at': formatted_date,
                }
                results.append(data)

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

        query_block_area = BlockArea.query.get(
            query_block_area_livestock.block_area_id)
        livestock_count = len(
            query_block_area.livestock) if query_block_area.livestock else 0

        columns_to_select = [
            FeedingRecord.feed_category,
            FeedingRecord.block_area_id,
            func.to_char(FeedingRecord.created_at, 'DD Mon YYYY').label('day'),
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

        results_feeding = []
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

            day_map[day]["feed_list"].append({
                "feed_category": get_feed_category_label(feed_category),
                "total_score": total_score / livestock_count
            })

            result.update(day_map[day])
            results_feeding.append(result)

        # Retrieve all livestock records from the database
        query = Livestock.query.options([
            subqueryload(Livestock.weight_records),
            subqueryload(Livestock.bcs_records),
            subqueryload(Livestock.health_records)
        ]).get(livestock_id)

        result = {
            'id': query.id,
            'name': query.name,
            'gender': query.gender,
            'bangsa': query.bangsa,
            'birth_date': query.birth_date,
            'info': f'{query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
            'description': query.description,
            'bcs_records': [],
            'weight_records': [],
            'feeding_records': [],
            'health_records': query.health_records
        }

        results_bcs_records = []
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
                    'created_at': current_record.created_at
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
                    'created_at': current_record.created_at
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

        result['bcs_records'] = results_bcs_records[::-1]
        result['weight_records'] = results_weight_records[::-1]
        result['feeding_records'] = remove_duplicates(results_feeding, 'day')

        # Serialize the livestock data using the schema
        result = livestock_schema.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
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


@views_bp.route('/livestock/<int:livestock_id>', methods=['PUT'])
@login_required
def update_livestock(livestock_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

    try:
        # Assuming you have a Livestock model and an existing livestock object
        livestock = Livestock.query.get(livestock_id)
        if livestock:
            livestock.name = name
            livestock.gender = gender
            livestock.bangsa = bangsa
            livestock.description = description
            db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Livestock {livestock_id} has been updated.'
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

        print(bashl, livestock)
        if livestock and bashl:
            db.session.delete(bashl)
            db.session.commit()
            db.session.delete(livestock)
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
