from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import desc
from Record.WeightRecord.models import WeightRecord
from Record.WeightRecord.schema import WeightRecordSchema

from auth import login_required

views_weight_record_bp = Blueprint('views_weight_record', __name__)

weight_record_schema = WeightRecordSchema()
weight_many_record_schema = WeightRecordSchema(many=True)


@views_weight_record_bp.route('/weight-records', methods=['GET'])
@login_required
def get_weight_record():

    try:
        # Retrieve all bc records from database
        query = WeightRecord.query.all()

        results = []
        # Serialize the weight record data using the schema
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
        result = weight_many_record_schema.dump(results)

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


@views_weight_record_bp.route('/weight-record/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_weight_record(livestock_id):

    try:
        # Retrieve BCS Record based on livestock_id from the database
        query = WeightRecord.query.filter_by(livestock_id=livestock_id).order_by(desc(WeightRecord.created_at))

        results = []
        # Serialize the livestock data using the schema
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
        result = weight_many_record_schema.dump(results)

        # result = weight_record_schema.dump(query)

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


@views_weight_record_bp.route('/weight-record', methods=['POST'])
@login_required
def post_weight_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    try:
        query = WeightRecord(livestock_id=livestock_id,
                             score=score,
                             date=date,
                             remarks=remarks)
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


@views_weight_record_bp.route('/weight-record/<int:id>', methods=['PUT'])
@login_required
def update_weight_record(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    weight_record = WeightRecord.query.get(id)
    if weight_record:
        weight_record.date = date
        weight_record.score = score
        weight_record.remarks = remarks
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


@views_weight_record_bp.route('/weight-record/<int:id>', methods=['DELETE'])
@login_required
def delete_weight_record(id):
    # Assuming the system has weight record model and an existing weight_record object
    query = WeightRecord.query.get(id)
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
