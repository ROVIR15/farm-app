from db_connection import db
from flask import Blueprint, request, jsonify
from Breeds.Breeding.models import Breeding
from Breeds.BreedingHistory.models import BreedingHistory
from Breeds.BreedingStatus.models import BreedingStatus
from Breeds.Pregnancy.models import Pregnancy

from Livestock.models import Livestock

from BlockAreaSledLivestock.models import BlockAreaSledLivestock

from Breeds.Breeding.schema import BreedingSchema

views_breeding_bp = Blueprint('views_breeding', __name__)

breeding_record_schema = BreedingSchema()
breeding_records_schema = BreedingSchema(many=True)


@views_breeding_bp.route('/breeding', methods=['POST'])
def post_new_breeding():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_male_id = data.get('livestock_male_id')
    livestock_female_id = data.get('livestock_female_id')
    sled_id = data.get('sled_id')
    block_area_id = data.get('block_area_id')

    try:
        # Store data to breeding colleciton
        query = Breeding(livestock_male_id=livestock_male_id,
                         livestock_female_id=livestock_female_id,
                         sled_id=sled_id,
                         is_active=True)
        db.session.add(query)
        db.session.commit()

        # Move Livestock to the same sled
        # Find existing colleciton on block_area_sled_livestock
        male_query = BlockAreaSledLivestock.query.get(livestock_male_id)
        female_query = BlockAreaSledLivestock.query.get(livestock_female_id)

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
        BreedingHistory(breeding_id=query.id,
                        remarks="This two livestock is started to breeding programs")

        # Create and initiate the status of this new breeding couple
        BreedingStatus(breeding_id=query.id,
                       breeding_status_name_id=1,
                       remarks="None")

        # Initiate new pregnancy
        query = Pregnancy(breeding_id=query.id,
                          is_active=False,
                          remarks="None")
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


@views_breeding_bp.route('/pregnancy/<int:breeding_id>', methods=['PUT'])
def put_pregnancy(breeding_id):
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    remarks = data.get('remarks')
    is_active = data.get('is_active')

    try:
        # Find collection preganancy based on breeding_id
        query = Pregnancy.query.get(breeding_id)

        if query:
            query.is_active = is_active
            query.remarks = "This couple is stated pregnant"
            db.session.commit()
        else:
            response = {
                'status': 'error',
                'message': f'Couple pregnancy status cannot be found'
            }

        if query.is_active:
            BreedingHistory(breeding_id=breeding_id,
                            remarks="This couple is started pregnancy")
        else:
            BreedingHistory(breeding_id=breeding_id, remarks=remarks)

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


@views_breeding_bp.route('/lambing', methods=['POST'])
def post_lambing():
    data = request.get_json()

    try:
        if isinstance(data, list):
            for item in data:
                name = item.get('name')
                gender = item.get('gender')
                bangsa = item.get('bangsa')
                description = item.get('description')

                query = Livestock(name=name, gender=gender,
                                  bangsa=bangsa, description=description)
                db.session.add(query)
                db.session.commit()

            # Make record on breeding_history as the couple giving birth
            query2 = BreedingHistory(breeding_id=query.id,
                            remarks=f'This breeding couple has given birth of {len(data)} lambs')
            db.session.add(query2)
            db.session.commit()

            # Create new the status of this couple
            # Breeding status is end
            query3 = BreedingStatus(breeding_id=query.id,
                           breeding_status_name_id=2,
                           remarks="Already giving birth")
            db.session.add(query3)
            db.session.commit()

            # Make record on breeding_history as the couple is end his breeding programs
            query4 = BreedingHistory(breeding_id=query.id,
                            remarks=f'This breeding couple has done')
            db.session.add(query4)
            db.session.commit()

        else:
            raise Exception('Data cannot be empty nor not in array')

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
