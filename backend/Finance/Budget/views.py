from db_connection import db
from datetime import datetime
from flask import Blueprint, request, jsonify
from sqlalchemy import func, and_
from sqlalchemy.orm import subqueryload
from Finance.Budget.models import Budget
from Finance.BudgetItem.models import BudgetItem
from Finance.Expenditure.models import Expenditure

from Finance.Budget.schema import BudgetSchema
from Finance.BudgetItem.schema import BudgetItemSchema

from FarmProfile.HasBudgetItem.models import HasBudgetItem

from auth import login_required, current_farm_profile

views_budget_bp = Blueprint('views_budget', __name__)

budget_item_schema = BudgetItemSchema()
budget_items_schema = BudgetItemSchema(many=True)


@views_budget_bp.route('/budget', methods=['GET'])
@login_required
def get_budget():
    month_year = request.args.get('month-year')

    if isinstance(month_year, str):
        param = month_year.split("-")
        month = param[0]
        year = param[1]
    else:
        date = datetime.now()
        month = date.month
        year = date.year

    try:

        farm_profile_id = current_farm_profile()

        query = HasBudgetItem.query.options([
            subqueryload(HasBudgetItem.budget_item)
        ]).filter(
            and_(
                HasBudgetItem.budget_item.has(func.extract(
                    'month', BudgetItem.month_year) == month),
                HasBudgetItem.budget_item.has(func.extract(
                    'year', BudgetItem.month_year) == year)
            )
        ).filter_by(
            farm_profile_id=farm_profile_id
        ).all()

        # # Filter rows by month and year
        # query = BudgetItem.query.options([
        #     subqueryload(BudgetItem.budget_category)
        # ]) \
        # .filter(
        #     func.extract('month', BudgetItem.month_year) == month,
        #     func.extract('year', BudgetItem.month_year) == year
        # ).all()

        columns_to_select = [
            Expenditure.budget_category_id,
            func.sum(Expenditure.amount).label('total_expenditure')
        ]

        results = []
        # Serialize the livestock data using the schema
        for koko in query:
            item = koko.budget_item

            expenditure_ = Expenditure.query \
                            .with_entities(*columns_to_select) \
                            .filter_by(budget_category_id=item.budget_category.id) \
                            .group_by(Expenditure.budget_category_id) \
                            .all()
            
            spent = expenditure_[0].total_expenditure if isinstance(expenditure_, list) and len(expenditure_) > 0 else 0;

            data = {
                'id': item.id,
                'month_year': item.month_year,
                'budget_category_id': item.budget_category.id,
                'budget_category_name': item.budget_category.budget_category_name,
                'month_year': item.month_year,
                'amount': item.amount,
                'expenditure': spent
                # 'created_at': item.created_at,
            }
            results.append(data)
        # result = budget_items_schema.dump(results)

        # Return the serialized data as JSON response
        return jsonify(results), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_budget_bp.route('/budget', methods=['POST'])
@login_required
def post_budget():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    budget_category_id = data.get('budget_category_id')
    month_year = data.get('month_year')
    amount = data.get('amount')

    try:
        farm_profile_id = current_farm_profile()

        query = BudgetItem(budget_category_id=budget_category_id,
                           amount=amount, month_year=month_year)
        db.session.add(query)
        db.session.commit()

        queryBI = HasBudgetItem(
            farm_profile_id=farm_profile_id, budget_item_id=query.id)
        db.session.add(queryBI)
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            # 'message': f'Hello! Your message "{message}" has been received.'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry!, Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_budget_bp.route('/budget/<int:budget_id>', methods=['PUT'])
def update_budget(budget_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    budget = Budget.query.get(budget_id)
    if budget:
        budget.name = name
        budget.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Block Area {budget_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {budget_id} not found.'
        }
        return jsonify(response), 404


@views_budget_bp.route('/budget/<int:budget_id>', methods=['DELETE'])
def delete_budget(budget_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = Budget.query.get(budget_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Block Area {budget_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {budget_id} not found.'
        }
        return jsonify(response), 404
