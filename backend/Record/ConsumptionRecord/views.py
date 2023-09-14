from db_connection import db
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
    query = ConsumptionRecord.query.all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'livestock_id': item.livestock_id,
            'foods_id': item.foods_id,
            'consumption_record_id': item.consumption_record_id,
            'description': item.description,
            'created_at': item.created_at,
        }
    results.append(data)
    result = consumption_records_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/consumption-record/<int:consumption_record_id>', methods=['GET'])
def get_a_block_area(consumption_record_id):
    # Retrieve all livestock records from the database
    query = ConsumptionRecord.query.get(consumption_record_id)

    # Serialize the livestock data using the schema
    result = consumption_record_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/consumption-record', methods=['POST'])
def post_block_area():
    data = request.get_json()  # Get the JSON data from the request body

    try:
        if isinstance(data, list):
            for item in data:
                block_area_id = item.block_area_id
                sku_id = item.sku_id
                score = item.score
                left = item.left
                remarks = item.remarks

                query = ConsumptionRecord(
                    block_area_id=block_area_id,
                    sku_id=sku_id,
                    score=score,
                    left=left,
                    remarks=remarks
                )
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


@views_bp.route('/consumption-record/<int:consumption_record_id>', methods=['PUT'])
def update_block_area(consumption_record_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    score = item.score
    left = item.left
    remarks = item.remarks


    # Assuming you have a Livestock model and an existing livestock object
    query = ConsumptionRecord.query.get(consumption_record_id)
    if query:
        query.score = score
        query.left = left
        query.remarks = remarks
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Block Area {consumption_record_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {consumption_record_id} not found.'
        }
        return jsonify(response), 404


@views_bp.route('/consumption-record/<int:consumption_record_id>', methods=['DELETE'])
def delete_block_area(consumption_record_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = ConsumptionRecord.query.get(consumption_record_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Block Area {consumption_record_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {consumption_record_id} not found.'
        }
        return jsonify(response), 404
