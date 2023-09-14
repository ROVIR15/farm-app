from db_connection import db
from flask import Blueprint, request, jsonify
from Record.FeedRecord.models import FeedRecord
from Record.FeedRecord.schema import FeedRecordSchema

views_feeding_record_bp = Blueprint('views_feeding_record', __name__)

feeding_record_schema = FeedRecordSchema()
feeding_many_record_schema = FeedRecordSchema(many=True)


@views_feeding_record_bp.route('/feeding-records', methods=['GET'])
def get_feeding_record():
    # Retrieve all feeding records from database
    query = FeedRecord.query.all()

    results = []
    # Serialize the feeding record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'block_area_id': item.block_area_id,
            'sku_id': item.sku_id,
            'date': item.date,
            'remarks': item.remarks,
            'created_at': item.created_at
        }
        results.append(data)
    result = feeding_many_record_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_feeding_record_bp.route('/feeding-record/<int:block_area_id>', methods=['GET'])
def get_a_feeding_record(block_area_id):
    # Retrieve BCS Record based on block_area_id from the database
    query = FeedRecord.query.get(block_area_id)

    # Serialize the livestock data using the schema
    result = feeding_record_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_feeding_record_bp.route('/feeding-record', methods=['POST'])
def post_feeding_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    block_area_id = data.get('block_area_id')
    sku_id = data.get('sku_id')
    score = data.get('score')
    date = data.get('date')
    remarks = data.get('remarks')

    try:
        query = FeedRecord(block_area_id=block_area_id,
                             sku_id=sku_id,
                             score=score,
                             remarks=remarks,
                             date=date)
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


@views_feeding_record_bp.route('/feeding-record/<int:id>', methods=['PUT'])
def update_feeding_record(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    block_area_id = data.get('block_area_id')
    sku_id = data.get('sku_id')
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    feeding_record = FeedRecord.query.get(id)
    if feeding_record:
        feeding_record.date = block_area_id
        feeding_record.date = sku_id
        feeding_record.date = date
        feeding_record.score = score
        feeding_record.remarks = remarks
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
        return jsonify(response), 500


@views_feeding_record_bp.route('/feeding-record/<int:id>', methods=['DELETE'])
def delete_feeding_record(id):
    # Assuming the system has feeding record model and an existing feeding_record object
    query = FeedRecord.query.get(id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'BCS Record {id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message':  f'BCS Record {id} not found.'
        }
        return jsonify(response), 404
