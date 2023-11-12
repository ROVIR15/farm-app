from db_connection import db
from flask import Blueprint, request, jsonify

from Livestock.models import Livestock
from Record.HeightRecord.models import HeightRecord
from Record.HeightRecord.schema import HeightRecordSchema

from sqlalchemy import desc, asc

from auth import login_required

views_height_record_bp = Blueprint('views_height_record', __name__)

height_record_schema = HeightRecordSchema()
height_many_record_schema = HeightRecordSchema(many=True)


@views_height_record_bp.route('/height-records', methods=['GET'])
@login_required
def get_height_record():
    try:
        # Retrieve all bc records from database
        query = HeightRecord.query.order_by(desc(HeightRecord.created_at)).all()

        results = []
        # Serialize the height record data using the schema
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
        result = height_many_record_schema.dump(results)

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


@views_height_record_bp.route('/height-record/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_height_record(livestock_id):
    try:
        # Retrieve BCS Record based on livestock_id from the database
        query = HeightRecord.query.filter_by(livestock_id=livestock_id).order_by(asc(HeightRecord.created_at))

        results = []
        prev_score = None  # Initialize prev_score to None

        for current_record in query:
            data = {
                'id': current_record.id,
                'livestock_id': current_record.livestock_id,
                'date': current_record.date,
                'score': current_record.score,
                'remarks': current_record.remarks,
                'created_at': current_record.created_at
            }

            if prev_score is not None:
                growth = current_record.score - prev_score
                percentage = (growth / prev_score) * 100
                data['growth'] = f'{percentage:.2f}%'  # Format the percentage with two decimal places
                data['prev_score'] = prev_score if prev_score is not None else 0 # Format the percentage with two decimal places
            else:
                data['growth'] = f'{0:.2f}%'  # Format the percentage with two decimal places
                data['prev_score'] = 0  # Format the percentage with two decimal places

            results.append(data)
            prev_score = current_record.score  # Update prev_score for the next iteration

        results = results[::-1]
        result = height_many_record_schema.dump(results)

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


@views_height_record_bp.route('/height-record', methods=['POST'])
@login_required
def post_height_record():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    try:
        query_livestock = Livestock.query.get(livestock_id)

        if not query_livestock:
            return jsonify({ 'status': 'error', 'message': f'Sorry, Livestock cannot be found'}), 404

        query = HeightRecord(livestock_id=livestock_id, date=date,
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


@views_height_record_bp.route('/height-record/<int:id>', methods=['PUT'])
@login_required
def update_height_record(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    height_record = HeightRecord.query.get(id)
    if height_record:
        height_record.date = date
        height_record.score = score
        height_record.remarks = remarks
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


@views_height_record_bp.route('/height-record/<int:id>', methods=['DELETE'])
@login_required
def delete_height_record(id):
    # Assuming the system has height record model and an existing height_record object
    query = HeightRecord.query.get(id)
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
