from db_connection import db
from flask import Blueprint, request, jsonify
from Record.BCSRecord.models import BCSRecord
from Record.BCSRecord.schema import BCSRecordSchema

views_bcs_record_bp = Blueprint('views_bcs_record', __name__)

bcs_record_schema = BCSRecordSchema()
bcs_many_record_schema = BCSRecordSchema(many=True)


@views_bcs_record_bp.route('/bcs-records', methods=['GET'])
def get_bcs_record():
    # Retrieve all bc records from database
    query = BCSRecord.query.all()

    results = []
    # Serialize the bcs record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'livestock_id': item.livestock_id,
            'date': item.date,
            'score': item.score,
            'remarks': item.remarks,
            'created_at': item.created_at
        }
        results.append(data)
    result = bcs_many_record_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bcs_record_bp.route('/bcs-record/<int:livestock_id>', methods=['GET'])
def get_a_bcs_record(livestock_id):
    # Retrieve BCS Record based on livestock_id from the database
    query = BCSRecord.query.get(livestock_id)

    # Serialize the livestock data using the schema
    result = bcs_record_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bcs_record_bp.route('/bcs-record', methods=['POST'])
def post_bcs_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    score = data.get('score')
    remarks = data.get('remarks')

    try:
        query = BCSRecord(livestock_id=livestock_id,
                          score=score, remarks=remarks)
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


@views_bcs_record_bp.route('/bcs-record/<int:id>', methods=['PUT'])
def update_bcs_record(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    bcs_record = BCSRecord.query.get(id)
    if bcs_record:
        bcs_record.date = date
        bcs_record.score = score
        bcs_record.remarks = remarks
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


@views_bcs_record_bp.route('/bcs-record/<int:id>', methods=['DELETE'])
def delete_bcs_record(id):
    # Assuming the system has bcs record model and an existing bcs_record object
    query = BCSRecord.query.get(id)
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

