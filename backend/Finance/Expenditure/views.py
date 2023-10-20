from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from Finance.BudgetRevision.models import BudgetRevision
from Finance.BudgetItem.models import BudgetItem
from Finance.BudgetItem.schema import BudgetItemSchema
from Finance.Expenditure.models import Expenditure

from FarmProfile.HasExpenditure.models import HasExpenditure
from Finance.Expenditure.schema import ExpenditureSchema

from auth import login_required, current_farm_profile

views_expenditure_bp = Blueprint('views_expenditure', __name__)

budget_item_schema = BudgetItemSchema()
budget_items_schema = BudgetItemSchema(many=True)

expenditure_item_schema = ExpenditureSchema()

@views_expenditure_bp.route('/expenditure/<int:expenditure_id>', methods=['GET'])
@login_required
def get_a_expenditure(expenditure_id):
    try:
        query = Expenditure.query.get(expenditure_id)

        if not query:
            response = {
                'status': 'error',
                'message': f'Sorry! Expenditure {expenditure_id} not found!'
            }

            return jsonify(response), 404


        result = expenditure_item_schema.dump(query)

        return jsonify(result), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500

@views_expenditure_bp.route('/expenditure', methods=['POST'])
@login_required
def post_expenditure():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    budget_category_id = data.get('budget_category_id')
    budget_sub_category_id = data.get('budget_sub_category_id')
    sku_id = data.get('sku_id')
    amount = data.get('amount')
    remarks = data.get('remarks')

    try:

        farm_profile_id = current_farm_profile()

        query = Expenditure(
            date=date,
            budget_category_id=budget_category_id, 
            budget_sub_category_id=budget_sub_category_id, 
            sku_id=sku_id, 
            amount=amount, 
            remarks=remarks)
        db.session.add(query)
        db.session.commit()

        queryExpenditure = HasExpenditure(farm_profile_id=farm_profile_id, expenditure_id=query.id)
        db.session.add(queryExpenditure)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            # 'message': f'Hello, {name}! Your message "{message}" has been received.'
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


@views_expenditure_bp.route('/expenditure/<int:expenditure_id>', methods=['PUT'])
@login_required
def update_expenditure(expenditure_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    date = data.get('date')
    budget_category_id = data.get('budget_category_id')
    budget_sub_category_id = data.get('budget_sub_category_id')
    sku_id = data.get('sku_id')
    amount = data.get('amount')
    remarks = data.get('remarks')

    # Create new budget revision based on revised item;
    try:
        # Assuming you have a Livestock model and an existing livestock object
        expenditure = Expenditure.query.get(expenditure_id)
        if expenditure:
            expenditure.date = date 
            expenditure.budget_category_id = budget_category_id 
            expenditure.budget_sub_category_id = budget_sub_category_id 
            expenditure.sku_id = sku_id 
            expenditure.amount = amount 
            expenditure.remarks = remarks
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Expenditure {expenditure_id} has been updated.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Expenditure {expenditure_id} not found.'
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

@views_expenditure_bp.route('/expenditure/<int:expenditure_id>', methods=['DELETE'])
@login_required
def delete_expenditure(expenditure_id):
    # Assuming you have a Livestock model and an existing livestock object

    try:
        query = Expenditure.query.get(expenditure_id)
        if query:
            db.session.delete(query)
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Expenditure {expenditure_id} has been deleted.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Expenditure {expenditure_id} not found.'
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