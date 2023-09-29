from db_connection import db
from flask import Blueprint, request, jsonify
from Record.BCSRecord.models import BCSRecord
from Record.BCSRecord.schema import BCSRecordSchema

from sqlalchemy import desc

from auth import login_required

views_bcs_record_bp = Blueprint('views_bcs_record', __name__)

bcs_record_schema = BCSRecordSchema()
bcs_many_record_schema = BCSRecordSchema(many=True)


@views_bcs_record_bp.route('/bcs-records', methods=['GET'])
@login_required
def get_bcs_record():
    try:
        # Retrieve all bc records from database
        query = BCSRecord.query.order_by(desc(BCSRecord.created_at)).all()

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
    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_bcs_record_bp.route('/bcs-record/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_bcs_record(livestock_id):
    try:
        # Retrieve BCS Record based on livestock_id from the database
        query = BCSRecord.query.filter_by(livestock_id=livestock_id).order_by(desc(BCSRecord.created_at))

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
        return jsonify(result), 200
    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_bcs_record_bp.route('/bcs-record', methods=['POST'])
@login_required
def post_bcs_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    try:
        query = BCSRecord(livestock_id=livestock_id, date=date,
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
@login_required
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
@login_required
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
