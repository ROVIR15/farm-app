from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from BlockArea.models import BlockArea
from FarmProfile.HasBlockArea.models import HasBlockArea as FarmProfileHasBlockArea
from BlockArea.schema import BlockAreaSchema

views_block_area_bp = Blueprint('views_block_area', __name__)

block_area_schema = BlockAreaSchema()
blocks_area_schema = BlockAreaSchema(many=True)


@views_block_area_bp.route('/block-areas', methods=['GET'])
def get_block_areas():
    # Retrieve all livestock records from the database
    query = BlockArea.query.options(subqueryload(BlockArea.sleds)).all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.name,
            'description': item.description,
            'sleds': item.sleds
            # 'created_at': item.created_at,
        }
        results.append(data)
    result = blocks_area_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['GET'])
def get_a_block_area(block_area_id):
    # Retrieve all livestock records from the database
    query = BlockArea.query.options(subqueryload(BlockArea.sleds)).get(block_area_id)

    # Serialize the livestock data using the schema
    result = block_area_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_block_area_bp.route('/block-area', methods=['POST'])
def post_block_area():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')
    farm_profile_id = data.get('farm_profile_id')

    try:
        query = BlockArea(name=name, description=description)
        db.session.add(query)
        db.session.commit()

        has_block_area = FarmProfileHasBlockArea(block_area_id=query.id, farm_profile_id=farm_profile_id)

        # Create a response JSON
        response = {
            'status': 'success',
            # 'message': f'Hello, {name}! Your message "{message}" has been received.'
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

@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['PUT'])
def update_block_area(block_area_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    block_area = BlockArea.query.get(block_area_id)
    if block_area:
        block_area.name = name
        block_area.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Block Area {block_area_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {block_area_id} not found.'
        }
        return jsonify(response), 404


@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['DELETE'])
def delete_block_area(block_area_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = BlockArea.query.get(block_area_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Block Area {block_area_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {block_area_id} not found.'
        }
        return jsonify(response), 404