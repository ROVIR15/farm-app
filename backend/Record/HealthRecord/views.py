from db_connection import db
from flask import Blueprint, request, jsonify
from Record.HealthRecord.models import HealthRecord
from Record.HealthRecord.schema import HealthRecordSchema

from sqlalchemy import desc
from auth import login_required

views_health_record_bp = Blueprint('views_health_record', __name__)

health_record_schema = HealthRecordSchema()
health_many_record_schema = HealthRecordSchema(many=True)


@views_health_record_bp.route('/health-records', methods=['GET'])
@login_required
def get_health_record():

    try:
        # Retrieve all health records from database
        query = HealthRecord.query.order_by(desc(HealthRecord.created_at)).all()

        results = []
        # Serialize the health record data using the schema
        for item in query:
            data = {
                'id': item.id,
                'livestock_id': item.livestock_id,
                'date': item.date,
                'disease_type': item.disease_type,
                'treatment_methods': item.treatment_methods,
                'remarks': item.remarks,
                'created_at': item.created_at
            }
            results.append(data)
        result = health_many_record_schema.dump(results)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500


@views_health_record_bp.route('/health-record/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_health_record(livestock_id):

    try:
        # Retrieve BCS Record based on livestock_id from the database
        query = HealthRecord.query.filter_by(livestock_id=livestock_id).order_by(desc(HealthRecord.created_at))

        results = []
        # Serialize the health record data using the schema
        for item in query:
            data = {
                'id': item.id,
                'livestock_id': item.livestock_id,
                'date': item.date,
                'disease_type': item.disease_type,
                'treatment_methods': item.treatment_methods,
                'remarks': item.remarks,
                'created_at': item.created_at
            }
            results.append(data)
        result = health_many_record_schema.dump(results)

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


@views_health_record_bp.route('/health-record', methods=['POST'])
@login_required
def post_health_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    disease_type = data.get('disease_type')
    treatment_methods = data.get('treatment_methods')
    date = data.get('date')
    remarks = data.get('remarks')

    try:
        query = HealthRecord(livestock_id=livestock_id,
                             disease_type=disease_type,
                             treatment_methods=treatment_methods,
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


@views_health_record_bp.route('/health-record/<int:id>', methods=['PUT'])
@login_required
def update_health_record(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    disease_type = data.get('disease_type')
    treatment_methods = data.get('treatment_methods')
    date = data.get('date')
    remarks = data.get('remarks')

    health_record = HealthRecord.query.get(id)
    if health_record:
        health_record.date = date
        health_record.disease_type = disease_type
        health_record.treatment_methods = treatment_methods
        health_record.remarks = remarks
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


@views_health_record_bp.route('/health-record/<int:id>', methods=['DELETE'])
@login_required
def delete_health_record(id):
    # Assuming the system has health record model and an existing health_record object
    query = HealthRecord.query.get(id)
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
