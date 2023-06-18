from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedload, subqueryload
from Sled.models import Sled
from Sled.schema import SledSchema

views_bp = Blueprint('views', __name__)

sled_schema = SledSchema()
sleds_schema = SledSchema(many=True)


@views_bp.route('/sleds', methods=['GET'])
def get_sleds():
    # Retrieve all livestock records from the database
    query = Sled.query.options(subqueryload(Sled.block_area)).all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'block_area_id': item.block_area_id,
            'name': item.name,
            'description': item.description,
            'block_area': {
                'block_area_id': item.block_area[0].block_area_id,
                'name': item.block_area[0].name,
                'description': item.block_area[0].description
            }
        }
    results.append(data)
    result = sleds_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/sled/<int:sled_id>', methods=['GET'])
def get_sled(sled_id):
    # Retrieve all livestock records from the database
    query = Sled.query.options(subqueryload(Sled.block_area)).all()

    # Serialize the livestock data using the schema
    result = sled_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/sled', methods=['POST'])
def post_sled():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')
    block_area_id = data.get('block_area_id')

    try:
        query = Sled(name=name, block_area_id=block_area_id,
                     description=description)
        db.session.add(query)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Hello, {name}! Your message "{message}" has been received.'
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


@views_bp.route('/sled/<int:sled_id>', methods=['PUT'])
def update_sled(sled_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')
    block_area_id = data.get('sled_id')

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


@views_bp.route('/sled/<int:sled_id>', methods=['DELETE'])
def delete_sled(sled_id):
    # Assuming you have a Livestock model and an existing livestock object
    sled = Sled.query.get(sled_id)
    if sled:
        db.session.delete(sled_id)
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
