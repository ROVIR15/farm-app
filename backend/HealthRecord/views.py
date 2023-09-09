from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedLoad, subqueryload
from HealthRecord.models import HealthRecord
from HealthRecord.schema import HealthRecordSchema

views_health_record_bp = Blueprint('views_health_record', __name__)

health_record_schema = HealthRecordSchema()
health_records_schema = HealthRecordSchema(many=True)


@views_health_record_bp.route('/health-records', methods=['GET'])
def get_health_records():
    # Retrieve all livestock records from the database
    query = HealthRecord.query.all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'livestock_id': item.livestock_id,
            'foods_id': item.foods_id,
            'healt_record_id': item.healt_record_id,
            'description': item.description,
            'created_at': item.created_at,
        }
    results.append(data)
    result = consumption_records_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_health_record_bp.route('/health-record/<int:healt_record_id>', methods=['GET'])
def get_a_health_record(healt_record_id):
    # Retrieve all livestock records from the database
    query = HealthRecord.query.get(healt_record_id)

    # Serialize the livestock data using the schema
    result = consumption_record_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_health_record_bp.route('/health-record', methods=['POST'])
def post_health_record():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    try:
        query = HealthRecord(name=name, description=description)
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


@views_health_record_bp.route('/health-record/<int:healt_record_id>', methods=['PUT'])
def update_health_record(healt_record_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    block_area = HealthRecord.query.get(healt_record_id)
    if block_area:
        block_area.name = name
        block_area.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Health Record{healt_record_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Health Record{healt_record_id} not found.'
        }
        return jsonify(response), 404


@views_health_record_bp.route('/health-record/<int:healt_record_id>', methods=['DELETE'])
def delete_health_record(healt_record_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = HealthRecord.query.get(healt_record_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Health Record{healt_record_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Health Record{healt_record_id} not found.'
        }
        return jsonify(response), 404
