from app import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedload, subqueryload
from Livestock.models import Livestock
from FarmProfile.HasLivestock.models import HasLivestock as FarmProfileHasLivestock
from Livestock.schema import LivestockSchema

views_bp = Blueprint('views_livestock', __name__)

livestock_schema = LivestockSchema()
livestocks_schema = LivestockSchema(many=True)


@views_bp.route('/livestocks', methods=['GET'])
def get_livestocks():
    # Retrieve all livestock records from the database
    query = Livestock.query.all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.name,
            'gender': item.gender,
            'bangsa': item.bangsa,
            'descrip tion': item.description,
            'created_at': item.created_at,
        # 'updated_at': item.updated_at,
            # 'info': {
            #     'block_area_id': item.info[0].block_area_id,
            #     'sled_id': item.info[0].sled_id
            # }
        }
    results.append(data)
    result = livestocks_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/livestock/<int:livestock_id>', methods=['GET'])
def get_a_livestock(livestock_id):
    # Retrieve all livestock records from the database
    query = Livestock.query.get(livestock_id)

    # Serialize the livestock data using the schema
    result = livestock_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/livestock', methods=['POST'])
def post_livestock():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')
    farm_profile_id = data.get('farm_profile_id')

    try:
        query = Livestock(name=name, gender=gender,
                          bangsa=bangsa, description=description)
        db.session.add(query)
        db.session.commit()

        has_livestock = FarmProfileHasLivestock(livestock_id=query.id, farm_profile_id=farm_profile_id)
        db.session.add(has_livestock)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Hello, {name}! Your message has been received.'
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
def update_livestock(livestock_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

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


@views_bp.route('/livestock/<int:livestock_id>', methods=['DELETE'])
def delete_livestock(livestock_id):
    # Assuming you have a Livestock model and an existing livestock object
    livestock = Livestock.query.get(livestock_id)
    if livestock:
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