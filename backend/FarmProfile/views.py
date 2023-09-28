from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from FarmProfile.models import FarmProfile
from FarmProfile.models import FarmProfileHasUsers
from FarmProfile.schema import FarmProfileSchema

from auth import login_required, current_farm_profile

views_farm_profile_bp = Blueprint('views_farm_profile', __name__)

farm_profile_schema = FarmProfileSchema()
farm_profile_many_schema = FarmProfileSchema(many=True)


@views_farm_profile_bp.route('/farm-profiles', methods=['GET'])
@login_required
def get_farm_profile_details():
    # Retrieve all farm_profile records from the database
    query = FarmProfile.query.all()

    # Serialize the query results using the schema
    schema = FarmProfileSchema(many=True)
    result = schema.dump(query)

    # Return the serialized data as a JSON response
    return jsonify(result)


@views_farm_profile_bp.route('/farm-profile', methods=['GET'])
@login_required
def get_a_farm_profile_detail():

    farm_profile_id = current_farm_profile()

    try: 
        # Retrieve farm_profile detail records from the database
        query = FarmProfile.query.get(farm_profile_id)

        # Serialize the feature data using the schema
        result = farm_profile_schema.dump(query)

        # Return the serialized data as JSON response
        return jsonify(result)
    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry!, Failed to proceed due to {response}'
        }
        return jsonify(response), 500


@views_farm_profile_bp.route('/farm-profile', methods=['POST'])
@login_required
def post_farm_profile_details():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    user_id = data.get('user_id')
    name = data.get('name')
    address_one = data.get('address_one')
    address_two = data.get('address_two')
    city = data.get('city')
    province = data.get('province')

    try:
        query = FarmProfile(
            name=name,
            address_one=address_one,
            address_two=address_two,
            city=city,
            province=province)
        db.session.add(query)
        db.session.commit()

        query2 = FarmProfileHasUsers(farm_profile_id=query.id, user_id=user_id)
        db.session.add(query2)
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
            'status': 'error'
        }
        return jsonify(response), 500


@views_farm_profile_bp.route('/farm-profile/<int:farm_profile_id>', methods=['PUT'])
@login_required
def update_farm_profile_detail(farm_profile_id):
    data = request.get_json()  # Get the JSON data from the request only

    # Process the data or perform any desired operations
    # For example, you can access spesific fields from the JSON data
    name = data.get('name')
    address_one = data.get('address_one')
    address_two = data.get('address_two')
    city = data.get('city')
    province = data.get('province')

    # Assuming you have a Block Area Sled Livestock model and an existing object
    farm_profile_detail = FarmProfile.query.get(farm_profile_id)
    if farm_profile_detail:
        farm_profile_detail.name = name
        farm_profile_detail.address_one = address_one
        farm_profile_detail.address_two = address_two
        farm_profile_detail.city = city
        farm_profile_detail.province = province
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error'
        }
        return jsonify(response), 404


@views_farm_profile_bp.route('/farm-profile/<int:farm_profile_id>', methods=['DELETE'])
def remove_farm_profile_detail(farm_profile_id):
    # Assuming you have a Feature model and an existing feature object
    farm_profile_detail = FarmProfile.query.get(farm_profile_id)
    if farm_profile_detail:
        db.session.delete(farm_profile_detail)
        db.session.commit()

        response = {
            'status': 'success'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Livestock Detail {farm_profile_id} not found.'
        }


@views_farm_profile_bp.route('/farm-profile-add-user', methods=['POST'])
@login_required
def post_farm_profile_has_user():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    user_id = data.get('user_id')
    farm_profil_id = data.get('farm_profil_id')

    try:
        query = FarmProfileHasUsers(
            user_id=user_id,
            farm_profil_id=farm_profil_id)
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
            'status': 'error'
        }
        return jsonify(response), 500

@views_farm_profile_bp.route('/farm-profile-remove-user/<int:user_id>', methods=['DELETE'])
@login_required
def remove_farm_profile_user(user_id):
    # Assuming you have a Feature model and an existing feature object
    farm_profile_detail = FarmProfile.query.get(user_id)
    if farm_profile_detail:
        db.session.delete(farm_profile_detail)
        db.session.commit()

        response = {
            'status': 'success'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'User {user_id} not found.'
        }

