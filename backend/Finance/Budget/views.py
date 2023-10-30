from db_connection import db
from datetime import datetime
from flask import Blueprint, request, jsonify
from sqlalchemy import func, and_
from sqlalchemy.orm import subqueryload
from Finance.Budget.models import Budget
from Finance.BudgetItem.models import BudgetItem
from Finance.Expenditure.models import Expenditure
from Finance.Income.models import Income

from Finance.Budget.schema import BudgetSchema
from Finance.Expenditure.schema import ExpenditureSchema
from Finance.BudgetItem.schema import BudgetItemSchema

from decimal import Decimal

from FarmProfile.HasBudgetItem.models import HasBudgetItem
from FarmProfile.HasIncome.models import HasIncome

from auth import login_required, current_farm_profile

views_budget_bp = Blueprint('views_budget', __name__)

budget_item_schema = BudgetItemSchema()
budget_items_schema = BudgetItemSchema(many=True)

expenditures_schema = ExpenditureSchema(many=True)

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

        total_budget_amount = 0
        total_expenditure = 0
        total_income = 0

        query = HasBudgetItem.query.options([
            subqueryload(HasBudgetItem.budget_item).subqueryload(BudgetItem.expenditures)
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

        income_query = HasIncome.query \
                        .filter(
                            and_(
                                HasIncome.income.has(func.extract(
                                    'month', Income.date) == month),
                                HasIncome.income.has(func.extract(
                                    'year', Income.date) == year)
                            )
                        ).filter_by(
                            farm_profile_id=farm_profile_id
                        ).all()

        results_income = []
        if isinstance(income_query, list) and income_query:
            for item in income_query:
                item_ = item.income
                total_income = total_income + Decimal(item_.amount)
                data = {
                    'id': item_.id,
                    'income_category_id': item_.income_category_id,
                    'category_label': item_.category_label.category_name,
                    'date': item_.date,
                    'amount': item_.amount,
                    'remarks': item_.remarks
                }
                results_income.append(data)

        columns_to_select = [
            Expenditure.budget_category_id,
            func.sum(Expenditure.amount).label('total_expenditure')
        ]

        results = []

        # Serialize the livestock data using the schema
        for koko in query:
            item = koko.budget_item

            total_expenditure_on_category = 0
            expenditures = None
            
            if item.expenditures and isinstance(item.expenditures, list):
                expenditures = expenditures_schema.dump(item.expenditures)
                # Use a list comprehension to extract the 'amount' values from the dictionaries
                amounts = [item["amount"] for item in expenditures]

                # Calculate the total expenditure by summing the amounts
                total_expenditure_on_category = sum(amounts)
                total_expenditure = total_expenditure + total_expenditure_on_category

            total_budget_amount = total_budget_amount + item.amount

            budget_left_ = Decimal(item.amount) - Decimal(total_expenditure)

            data = {
                'id': item.id,
                'month_year': item.month_year,
                'budget_category_id': item.budget_category.id,
                'budget_category_name': item.budget_category.budget_category_name,
                'month_year': item.month_year,
                'budget_amount': item.amount,
                'total_expenditure': total_expenditure,
                'left': budget_left_
                # 'created_at': item.created_at,
            }
            results.append(data)

        budget_left = Decimal(total_budget_amount) - Decimal(total_expenditure)

        response = {
            'month_year': month_year,
            'total_budget_amount': total_budget_amount,
            'total_expenditure': total_expenditure,
            'total_income': total_income,
            'budget_left': budget_left,
            'status': 'Lebih dari budget' if (budget_left) < 0 else 'Aman',
            'budget_breakdown': results,
            'incomes': results_income
        }

        # Return the serialized data as JSON response
        return jsonify(response), 200

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
