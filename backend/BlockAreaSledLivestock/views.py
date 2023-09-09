from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from BlockAreaSledLivestock.schema import BlockAreaSledLivestockSchema

views_livestock_details_bp = Blueprint('views_livestock_details', __name__)

block_area_sled_livestock_schema = BlockAreaSledLivestockSchema()
block_area_sled_livestock_many_schema = BlockAreaSledLivestockSchema(many=True)


@views_livestock_details_bp.route('/livestock-list', methods=['GET'])
def get_livestock_details():
    # Retrieve all livestock records from the database
    query = BlockAreaSledLivestock.query.options(
        subqueryload(BlockAreaSledLivestock.sled),
        subqueryload(BlockAreaSledLivestock.block_area),
        subqueryload(BlockAreaSledLivestock.livestock)
    ).all()

    # Serialize the query results using the schema
    schema = BlockAreaSledLivestockSchema(many=True)
    result = schema.dump(query)

    # Return the serialized data as a JSON response
    return jsonify(result)


@views_livestock_details_bp.route('/livestock-list/<int:livestock_id>', methods=['GET'])
def get_a_livestock_detail(livestock_id):
    # Retrieve livestock detail records from the database
    query = BlockAreaSledLivestock.query.get(livestock_id)

    # Serialize the feature data using the schema
    result = block_area_sled_livestock_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_livestock_details_bp.route('/livestock-list', methods=['POST'])
def post_livestock_details():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    livestock_id = data.get('livestock_id')
    sled_id = data.get('sled_id')
    block_area_id = data.get('block_area_id')

    try:
        query = BlockAreaSledLivestock(
            livestock_id=livestock_id, sled_id=sled_id, block_area_id=block_area_id)
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


@views_livestock_details_bp.route('/livestock-list/<int:livestock_id>', methods=['PUT'])
def update_livestock_detail(livestock_id):
    data = request.get_json()  # Get the JSON data from the request only

    # Process the data or perform any desired operations
    # For example, you can access spesific fields from the JSON data
    sled_id = data.get('sled_id')
    block_area_id = data.get('block_area_id')

    # Assuming you have a Block Area Sled Livestock model and an existing object
    livestock_detail = BlockAreaSledLivestock.query.get(livestock_id)
    if livestock_detail:
        livestock_detail.sled_id = sled_id
        livestock_detail.block_area_id = block_area_id
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


@views_livestock_details_bp.route('/livestock-list/<int:livestock_id>', methods=['DELETE'])
def remove_livestock_detail(livestock_id):
    # Assuming you have a Feature model and an existing feature object
    livestock_detail = BlockAreaSledLivestock.query.get(livestock_id)
    if livestock_detail:
        db.session.delete(livestock_detail)
        db.session.commit()

        response = {
            'status': 'success'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Livestock Detail {livestock_id} not found.'
        }
