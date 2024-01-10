from flask import Blueprint, request, jsonify
from usecases.Record.MilkService import MilkService

from auth import login_required

views_milk_record_bp = Blueprint('views_milk_record', __name__)


@views_milk_record_bp.route('/milk-record/<int:livestock_id>', methods=['GET'])
@login_required
def get_milk_record_by_livestock_id(livestock_id):
    response = MilkService.getMilkProductionByLivestock(livestock_id)

    # Check if 'status' key exists in the response dictionary
    if 'status' in response:
        # Access the status code from the response dictionary
        status_code = response['status']
        return jsonify(response), status_code
    else:
        # Default to 200 OK if 'status' is not present
        return jsonify({'message': 'Something Error'}), 500


@views_milk_record_bp.route('milk-record', methods=['POST'])
@login_required
def store_milk_record():
    data = request.get_json()  # Get the JSON data from request body

    livestock_id = data.get('livestock_id')
    date = data.get('date')
    score = data.get('score')
    remarks = data.get('remarks')

    query = MilkService.createMilkRecord(livestock_id=livestock_id,
                                         date=date,
                                         score=score,
                                         remarks=remarks  
                                        )
    return jsonify(query), 200

@views_milk_record_bp.route('milk-record/<int:milk_record_id>', methods=['PUT'])
@login_required
def update_milk_record(milk_record_id):
    data = request.get_json()

    score = data.get('score')
    date = data.get('date')

    response = MilkService.updateMilkRecord(milk_record_id, new_date=date, new_score=score)
    if 'status' in response:
        # Access the status code from the response dictionary
        status_code = response['status']
        return jsonify(response), status_code
    else:
        # Default to 200 OK if 'status' is not present
        return jsonify({'message': 'Something Error'}), 500

@views_milk_record_bp.route('milk-record/<int:milk_record_id>', methods=['DELETE'])
@login_required
def delete_milk_record(milk_record_id):
    response = MilkService.deleteMilkRecord(milk_record_id)

    # Check if 'status' key exists in the response dictionary
    if 'status' in response:
        # Access the status code from the response dictionary
        status_code = response['status']
        return jsonify(response), status_code
    else:
        # Default to 200 OK if 'status' is not present
        return jsonify({'message': 'Something Error'}), 500