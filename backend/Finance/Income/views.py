from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from Finance.BudgetRevision.models import BudgetRevision
from Finance.BudgetItem.models import BudgetItem
from Finance.BudgetItem.schema import BudgetItemSchema
from Finance.Income.models import Income

from FarmProfile.HasIncome.models import HasIncome

from auth import login_required, current_farm_profile

views_income_bp = Blueprint('views_income', __name__)

budget_item_schema = BudgetItemSchema()
budget_items_schema = BudgetItemSchema(many=True)

@views_income_bp.route('/income', methods=['POST'])
@login_required
def post_income():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    income_category_id = data.get('income_category_id')
    amount = data.get('amount')
    remarks = data.get('remarks')

    try:

        farm_profile_id = current_farm_profile()

        query = Income(
            date=date,
            income_category_id=income_category_id, 
            amount=amount, 
            remarks=remarks)
        db.session.add(query)
        db.session.commit()

        queryExpenditure = HasIncome(farm_profile_id=farm_profile_id, income_id=query.id)
        db.session.add(queryExpenditure)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_income_bp.route('/income/<int:income_id>', methods=['PUT'])
@login_required
def update_income(income_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    income_category_id = data.get('income_category_id')
    amount = data.get('amount')
    remarks = data.get('remarks')

    # Create new budget revision based on revised item;
    try:
        # Assuming you have a Livestock model and an existing livestock object
        income = Income.query.get(income_id)
        if income:
            income.date = date 
            income.budget_category_id = income_category_id 
            income.amount = amount 
            income.remarks = remarks
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Income {income_id} has been updated.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Income {income_id} not found.'
            }
            return jsonify(response), 404

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500

@views_income_bp.route('/income/<int:income_id>', methods=['DELETE'])
@login_required
def delete_income(income_id):
    # Assuming you have a Livestock model and an existing livestock object

    try:
        query = Income.query.get(income_id)
        if query:
            db.session.delete(query)
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Income {income_id} has been deleted.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Income {income_id} not found.'
            }
            return jsonify(response), 404

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500