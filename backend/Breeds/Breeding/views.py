from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import desc
from sqlalchemy.orm import subqueryload

from datetime import datetime

from Breeds.Breeding.models import Breeding
from Breeds.BreedingHistory.models import BreedingHistory
from Breeds.BreedingStatus.models import BreedingStatus
from Breeds.Pregnancy.models import Pregnancy
from Breeds.Lambing.models import Lambing
from Sled.models import Sled

from Livestock.models import Livestock
from BlockAreaSledLivestock.models import BlockAreaSledLivestock

from Product.models import Product
from ProductHasCategory.models import ProductHasCategory
from SKU.models import SKU

from FarmProfile.HasLivestock.models import HasLivestock
from FarmProfile.HasBreeding.models import HasBreeding

from BlockAreaSledLivestock.models import BlockAreaSledLivestock

from auth import current_farm_profile, login_required

from Breeds.Breeding.schema import BreedingSchema
from Breeds.BreedingHistory.schema import BreedingHistorySchema
from Breeds.Lambing.schema import LambingSchema

views_breeding_bp = Blueprint('views_breeding', __name__)

breeding_record_schema = BreedingSchema()
breeding_records_schema = BreedingSchema(many=True)

breeding_history_record_schema = BreedingHistorySchema()
breeding_histories_record_schema = BreedingHistorySchema(many=True)

lambing_record_schema = LambingSchema()
lambing_records_schema = LambingSchema(many=True)


@views_breeding_bp.route('/breedings', methods=['GET'])
@login_required
def get_breedings():
    try:
        farm_profile_id = current_farm_profile()
        query = HasBreeding.query.options([subqueryload(HasBreeding.breedings).subqueryload(Breeding.breeding_status)]) \
                .filter_by(
                    farm_profile_id=farm_profile_id
                ) \
                .order_by(desc(HasBreeding.breeding_id)) \
                .all()
        results = []
        if not query:
            return jsonify([]), 200
        else:
            list_of_breeding = query
            if not isinstance(list_of_breeding, list):
                raise Exception("Not found any breeding record")
            else:
                for item in list_of_breeding:
                    date = item.created_at.strftime("%d %b %Y")
                    result = {
                        "id": item.breedings.id,
                        "name": f'Breeding #{item.breedings.id}',
                        "farm_profile_id": item.farm_profile_id,
                        "livestock_male_id": item.breedings.livestock_male_id,
                        "livestock_female_id": item.breedings.livestock_female_id,
                        "livestock_male_name": item.breedings.livestock_male.name,
                        "livestock_female_name": item.breedings.livestock_female.name,
                        "sled_id": item.breedings.sled_id,
                        "is_active": item.breedings.is_active,
                        "created_at": date
                    }
                    results.append(result)
                result = breeding_records_schema.dump(results)
                return jsonify(result), 200
        # breedings = HasBreeding.query().filter_by(farm_profile_id=farm_profile_id)
    except Exception as e:
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding/<int:breeding_id>', methods=['GET'])
def get_a_breeding(breeding_id):
    try:
        query = Breeding.query.options([
                    subqueryload(Breeding.livestock_male),
                    subqueryload(Breeding.livestock_female),
                    subqueryload(Breeding.lambing),
                    subqueryload(Breeding.breeding_history),
                    subqueryload(Breeding.breeding_status),
                    subqueryload(Breeding.sled).subqueryload(Sled.block_area)
                ]) \
                .get(breeding_id)

        query_preg = Pregnancy.query.filter_by(breeding_id=breeding_id).first()

        lambing = []
        for item in query.lambing:
            date_obj = item.livestock.created_at
            # Format the date as "DD MMMM YYYY"
            formatted_date = date_obj.strftime("%d %B %Y")
            __temp = {
                "id": item.id,
                "breeding_id": item.breeding_id,
                "livestock": {
                    'id': item.livestock.id,
                    'name': item.livestock.name,
                    'gender': item.livestock.gender,
                    'birth_date': item.livestock.birth_date,
                    'bangsa': item.livestock.bangsa,
                    'info': f'{item.livestock.get_gender_label()} | {item.livestock.calculate_age()} | Bangsa {item.livestock.bangsa}',
                    'description': item.livestock.description,
                    'created_at': formatted_date,
                },
                "created_at": item.created_at
            }

            lambing.append(__temp)

        status_breeding = query.breeding_status[0] if isinstance(query.breeding_status, list) and len(query.breeding_status) else None

        if query.sled.block_area:
            block_area_name = query.sled.block_area.name
            block_area_description = query.sled.block_area.description
        else:
            block_area_name = ""
            block_area_description = ""

        sled_ = {
            'id': query.sled.id,
            'block_area_id': query.sled.block_area_id,
            'name': query.sled.name,
            'description': query.sled.description,
            'block_area_name': block_area_name,
            'block_area_description': block_area_description
        }

        result = {
            "id": query.id,
            "is_active": query.is_active,
            "sled_id": query.sled_id,
            "created_at": query.created_at,
            "lambing": lambing,
            "pregnancy": query_preg,
            "livestock_male": query.livestock_male,
            "livestock_female": query.livestock_female,
            "breeding_history": query.breeding_history,
            "breeding_status": status_breeding,
            "sled": sled_
        }

        if not query:
            raise Exception('Breeding data not found!')

        result = breeding_record_schema.dump(result)

        return jsonify(result), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding', methods=['POST'])
def post_new_breeding():
    data = request.get_json()  # Get the JSON data from request body

    try:
        # Process the data or perform any desired operations
        livestock_male_id = data.get('livestock_male_id')
        livestock_female_id = data.get('livestock_female_id')
        date = data.get('date') if data.get(
            'date') is not None else "2023-10-10"
        sled_id = data.get('sled_id')
        block_area_id = data.get('block_area_id')

# Convert the input date to a datetime object
# input_date_object = datetime.strptime(input_date, "%d-%m-%Y")
# 
# Format the date as "yyyy-mm-dd"
# output_date = input_date_object.strftime("%Y-%m-%d")

        # Store data to breeding colleciton
        query = Breeding(livestock_male_id=livestock_male_id,
                         livestock_female_id=livestock_female_id,
                         sled_id=sled_id,
                         is_active=True,
                         date=date)
        db.session.add(query)
        db.session.commit()

        if not query:
            raise Exception('Failed to store data')
        # Move Livestock to the same sled
        # Find existing colleciton on block_area_sled_livestock
        male_query = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_male_id)
        female_query = BlockAreaSledLivestock.query.filter_by(
            livestock_id=livestock_female_id).first()

        if male_query and female_query:
            new_record_male_sled_block_area = BlockAreaSledLivestock(
                livestock_id=livestock_male_id, sled_id=sled_id, block_area_id=block_area_id)
            db.session.add(new_record_male_sled_block_area)
            db.session.commit()

            new_record_female_sled_block_area = BlockAreaSledLivestock(
                livestock_id=livestock_female_id, sled_id=sled_id, block_area_id=block_area_id)
            db.session.add(new_record_female_sled_block_area)
            db.session.commit()

        else:
            raise Exception('One of livestock cannot be found')

        # Make record on breeding_history that this couple start to breed
        breeding_status_history = BreedingHistory(breeding_id=query.id,
                                                  remarks="This two livestock is started to breeding programs")
        db.session.add(breeding_status_history)
        db.session.commit()

        # Create and initiate the status of this new breeding couple
        breeding_status_status = BreedingStatus(breeding_id=query.id,
                                                breeding_status_name_id=1,
                                                remarks="None")
        db.session.add(breeding_status_status)
        db.session.commit()

        # Initiate new pregnancy
        p_query = Pregnancy(breeding_id=query.id,
                            is_active=False)
        db.session.add(p_query)
        db.session.commit()

        farm_profile_id = current_farm_profile()
        store_breeding = HasBreeding(
            farm_profile_id=farm_profile_id, breeding_id=query.id)
        db.session.add(store_breeding)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'breeding_id': query.id,
            'farm_profile': store_breeding.id
        }

        return jsonify(response), 200

    except Exception as e:
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding-histories/<int:breeding_id>', methods=['GET'])
def get_breeding_history(breeding_id):

    try:
        query = BreedingHistory.query.filter_by(breeding_id=breeding_id).all()

        results = []
        if not isinstance(query, list) and not query:
            raise Exception('Breeding History Not FOund!')
        else:
            for item in query:
                result = {
                    "id": item.id,
                    "breeding_id": item.breeding_id,
                    "remarks": item.remarks,
                    "created_at": item.created_at
                }
                results.append(result)
            data = breeding_histories_record_schema.dump(results)

            return jsonify(data), 200
    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding-history', methods=['POST'])
def post_breeding_history():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    breeding_id = data.get('breeding_id')
    remarks = data.get('remarks')

    try:
        query = BreedingHistory(breeding_id=breeding_id,
                                remarks=remarks)
        db.session.add(query)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding-history/<int:breeding_history_id>', methods=['DELETE'])
def delete_breeding_history(breeding_history_id):
    # Assuming the system has bcs record model and an existing bcs_record object
    query = BreedingHistory.query.get(breeding_history_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Breeding History {breeding_history_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message':  f'Breeding History {breeding_history_id} not found.'
        }
        return jsonify(response), 404


@views_breeding_bp.route('/pregnancy/<int:breeding_id>', methods=['PUT'])
def put_pregnancy(breeding_id):
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    remarks = data.get('remarks')
    is_active = data.get('is_active')

    try:
        # Find collection preganancy based on breeding_id
        query = Pregnancy.query.filter_by(breeding_id=breeding_id).first()

        if query:
            query.is_active = is_active
            query.remarks = "This couple is stated pregnant"
            db.session.commit()

            if query.is_active:
                query = BreedingHistory(breeding_id=breeding_id,
                                        remarks="This couple is started pregnancy")
            else:
                query = BreedingHistory(
                    breeding_id=breeding_id, remarks=remarks)

            db.session.add(query)
            db.session.commit()

        else:
            response = {
                'status': 'error',
                'message': f'Couple pregnancy status cannot be found'
            }
            return jsonify(response), 404

        # Create a response JSON
        response = {
            'status': 'success'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding/lambing/<int:breeding_id>', methods=['GET'])
def get_lambing(breeding_id):

    try:
        query = Lambing.query.options([subqueryload(Lambing.livestock)]).filter(
            breeding_id == breeding_id).all()

        results = []
        for item in query:
            result = {
                "id": item.id,
                "livestock_id": item.livestock_id,
                "breeding_id": item.breeding_id,
                "livestock": item.livestock
            }
            results.append(result)

        serialize = lambing_records_schema.dump(results)
        return jsonify(serialize), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Unable to proceed your request, error: {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding/lambing/<int:lambing_id>', methods=['DELETE'])
def delete_lambing(lambing_id):

    try:
        query = Lambing.query.get(lambing_id)

        if query:
            basl = BlockAreaSledLivestock.query.filter_by(
                livestock_id=query.livestock_id).first()
            livestock_info = Livestock.query.get(query.livestock_id)

            db.session.delete(query)
            db.session.commit()

            db.session.delete(basl)
            db.session.commit()

            db.session.delete(livestock_info)
            db.session.commit()

        else:
            response = {
                'status': 'error',
                'message': 'Unable to proceed your request, due to: Lambing not found'
            }

            return jsonify(response), 404

        response = {
            'status': 'success',
            'message': 'Lambing has been deleted!'
        }

        return jsonify(response), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Unable to proceed your request, due to: {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding/lambing', methods=['POST'])
def post_lambing():
    data = request.get_json()

    farm_profile_id = current_farm_profile()

    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')
    block_area_id = data.get('block_area_id')
    sled_id = data.get('sled_id')
    breeding_id = data.get('breeding_id')
    # Get the current date
    birth_date = "2023-09-08" if data.get('birth_date') is None else data.get('birth_date')

    try:
        query = Livestock(name=name, gender=gender, birth_date=birth_date,
                          bangsa=bangsa, description=description)
        db.session.add(query)
        db.session.commit()

        lambing = Lambing(breeding_id=breeding_id, livestock_id=query.id)
        db.session.add(lambing)
        db.session.commit()

        product = Product(
            name=name, unit_measurement="ekor", description="none")
        db.session.add(product)
        db.session.commit()

        phc = ProductHasCategory(
            product_id=product.id, category_id=3)
        db.session.add(phc)
        db.session.commit()

        sku = SKU(product_id=product.id, name=name)
        db.session.add(sku)
        db.session.commit()

        has_livestock = HasLivestock(
            livestock_id=query.id, farm_profile_id=farm_profile_id)
        db.session.add(has_livestock)
        db.session.commit()

        block_area_sled_livestock = BlockAreaSledLivestock(
            livestock_id=query.id, block_area_id=block_area_id, sled_id=sled_id)
        db.session.add(block_area_sled_livestock)
        db.session.commit()

        # Make record on breeding_history as the couple giving birth
        query2 = BreedingHistory(breeding_id=breeding_id,
                                 remarks=f'This breeding couple has given birth of {1} lambs')
        db.session.add(query2)
        db.session.commit()

        # Create new the status of this couple
        # Breeding status is end
        query3 = BreedingStatus(breeding_id=breeding_id,
                                breeding_status_name_id=2,
                                remarks="Already giving birth")
        db.session.add(query3)
        db.session.commit()

        # Make record on breeding_history as the couple is end his breeding programs
        query4 = BreedingHistory(breeding_id=breeding_id,
                                 remarks=f'This breeding couple has done')
        db.session.add(query4)
        db.session.commit()


        breeding = Breeding.query.get(breeding_id)

        if breeding:
            breeding.is_active = False
            db.session.commit()

        response = {
            'status': 'Success'
        }

        return jsonify(response), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_breeding_bp.route('/breeding/<int:id>', methods=['DELETE'])
def delete_breeding(id):
    # Assuming the system has bcs record model and an existing bcs_record object
    try:
        query = Breeding.query.get(id)
        if query:
            db.session.delete(query)
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Breeding {id} has been deleted.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message':  f'Breeding {id} not found.'
            }
            return jsonify(response), 404
    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500