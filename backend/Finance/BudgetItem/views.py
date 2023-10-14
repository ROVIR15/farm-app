from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from Finance.BudgetRevision.models import BudgetRevision
from Finance.BudgetItem.models import BudgetItem
from Finance.BudgetItem.schema import BudgetItemSchema

from FarmProfile.HasBudgetItem.models import HasBudgetItem

from auth import login_required, current_farm_profile

views_budget_item_bp = Blueprint('views_budget_item', __name__)

budget_item_schema = BudgetItemSchema()
budget_items_schema = BudgetItemSchema(many=True)


@views_budget_item_bp.route('/budget-item', methods=['POST'])
@login_required
def post_budget():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    budget_category_id = data.get('budget_category_id')
    amount = data.get('amount')
    month_year = data.get('month_year')

    try:

        farm_profile_id = current_farm_profile()

        query = BudgetItem(
            budget_category_id=budget_category_id, amount=amount, month_year=month_year)
        db.session.add(query)
        db.session.commit()

        queryBI = HasBudgetItem(farm_profile_id=farm_profile_id, budget_item_id=query.id)
        db.session.add(queryBI)
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


@views_budget_item_bp.route('/budget-item/<int:budget_item_id>', methods=['PUT'])
@login_required
def update_budget(budget_item_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    budget_category_id = data.get('budget_category_id')
    month_year = data.get('month_year')
    to_amount = data.get('amount')
    notes = 'none'

    # Create new budget revision based on revised item;
    try:
        # Assuming you have a Livestock model and an existing livestock object
        budget_item = BudgetItem.query.get(budget_item_id)
        if budget_item:
            budget_item.amount = to_amount
            budget_item.budget_category_id = budget_category_id
            db.session.commit()

            query = BudgetRevision(
                month_year=month_year,
                budget_category_id=budget_category_id,
                from_amount=budget_item.amount,  # get last budget amount and stored as from_amount
                to_amount=to_amount,
                notes=notes)
            db.session.add(query)
            db.session.commit()
            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Budget Item {budget_item_id} has been updated.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Budget Item {budget_item_id} not found.'
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

@views_budget_item_bp.route('/budget-item/<int:budget_item_id>', methods=['DELETE'])
@login_required
def delete_budget(budget_item_id):
    # Assuming you have a Livestock model and an existing livestock object

    try:
        query = BudgetItem.query.get(budget_item_id)
        if query:
            db.session.delete(query)
            db.session.commit()

            response = {
                'status': 'success',
                'message': f'Budget Item {budget_item_id} has been deleted.'
            }
            return jsonify(response), 200
        else:
            response = {
                'status': 'error',
                'message': f'Budget Item {budget_item_id} not found.'
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