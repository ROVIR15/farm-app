from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedLoad, subqueryload
from ConsumptionRecord.models import ConsumptionRecord
from ConsumptionRecord.schema import ConsumptionRecordSchema

views_bp = Blueprint('views', __name__)

consumption_record_schema = ConsumptionRecordSchema()
consumption_records_schema = ConsumptionRecordSchema(many=True)


@views_bp.route('/consumption-records', methods=['GET'])
def get_consumption_records():
    # Retrieve all livestock records from the database
    query = ConsumptionRecord.query.options(subqueryload(Livestock.info)).all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'block_area': item.name,
            'description': item.description,
            'created_at': item.created_at,
        }
    results.append(data)
    result = blocks_area_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/livestock/<int:block_area_id>', methods=['GET'])
def get_a_block_area(block_area_id):
    # Retrieve all livestock records from the database
    query = BlockArea.query.get(block_area_id)

    # Serialize the livestock data using the schema
    result = block_area_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/block-area', methods=['POST'])
def post_block_area():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    try:
        query = BlockArea(name=name, description=description)
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

@views_bp.route('/block-area/<int:block_area_id>', methods=['PUT'])
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


@views_bp.route('/block-area/<int:block_area_id>', methods=['DELETE'])
def delete_block_area(block_area_id):
    # Assuming you have a Livestock model and an existing livestock object
    livestock = BlockArea.query.get(block_area_id)
    if livestock:
        db.session.delete(livestock)
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