from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload, joinedload
from Sled.models import Sled
from FarmProfile.HasSled.models import HasSled as FarmProfileHasSled
from FarmProfile.models import FarmProfileHasUsers

from Sled.schema import SledSchema

from auth import login_required, current_user, current_farm_profile

views_sled_bp = Blueprint('views_sled', __name__)

sled_schema = SledSchema()
sleds_schema = SledSchema(many=True)


@views_sled_bp.route('/sleds', methods=['GET'])
@login_required
def get_sleds():

    farm_profile_id = 5

    # Retrieve all livestock records from the database
    query = FarmProfileHasSled.query.options([subqueryload(
        FarmProfileHasSled.sled).subqueryload(Sled.block_area)]).filter_by(farm_profile_id=farm_profile_id).all()

    # query = Sled.query.options(subqueryload(Sled.block_area)).all()

    try:
        if query:
            results = []

            # Serialize the livestock data using the schema
            for item in query:
                if item.sled.block_area:
                    block_area_name = item.sled.block_area.name
                    block_area_description = item.sled.block_area.description
                else:
                    block_area_name = ""
                    block_area_description = ""

                data = {
                    'id': item.sled.id,
                    'block_area_id': item.sled.block_area_id,
                    'name': item.sled.name,
                    'description': item.sled.description,
                    'block_area_name': block_area_name,
                    'block_area_description': block_area_description
                }
                results.append(data)
            result = sleds_schema.dump(results)

            # Return the serialized data as JSON response
            return jsonify(result)
        else:
            response = {
                'status': 'error',
                'message': 'You dont have any sled'
            }

            return jsonify(response)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_sled_bp.route('/sled/<int:sled_id>', methods=['GET'])
@login_required
def get_sled(sled_id):
    # Retrieve all livestock records from the database
    query = Sled.query.options(subqueryload(Sled.block_area)).get(sled_id)

    # Serialize the livestock data using the schema
    result = sled_schema.dump(query)

    # Return the serialized data as JSON response
    # return jsonify(current_user)
    return jsonify(result), 200


@views_sled_bp.route('/sled', methods=['POST'])
@login_required
def post_sled():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')
    block_area_id = data.get('block_area_id')
    farm_profile_id = data.get('farm_profile_id')

    try:
        query = Sled(name=name, block_area_id=block_area_id,
                     description=description)
        db.session.add(query)
        db.session.commit()

        user_id = current_user()

        farm_profile = FarmProfileHasUsers.query.filter_by(
            user_id=user_id).first()

        has_sled = FarmProfileHasSled(
            farm_profile_id=farm_profile.farm_profile_id, sled_id=query.id)
        db.session.add(has_sled)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        db.session.rollback()
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, {name}! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_sled_bp.route('/sled/<int:sled_id>', methods=['PUT'])
@login_required
def update_sled(sled_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')
    block_area_id = data.get('block_area_id')

    # Assuming you have a Livestock model and an existing livestock object
    sled = Sled.query.get(sled_id)
    if sled:
        sled.name = name
        sled.block_area_id = block_area_id
        sled.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Sled {sled_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Sled {sled_id} not found.'
        }
        return jsonify(response), 404


@views_sled_bp.route('/sled/<int:sled_id>', methods=['DELETE'])
@login_required
def delete_sled(sled_id):
    # Assuming you have a Livestock model and an existing livestock object
    sled = Sled.query.get(sled_id)
    if sled:
        db.session.delete(sled)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Sled {sled_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Sled {sled_id} not found.'
        }
        return jsonify(response), 404