from db_connection import db
from flask import Blueprint, request, jsonify
from Finance.BudgetCategory.models import BudgetCategory
from Finance.BudgetSubCategory.models import BudgetSubCategories
from Finance.IncomeCategories.models import IncomeCategories
from Finance.Income.models import Income

from Finance.BudgetCategory.schema import BudgetCategorySchema

views_budget_category_bp = Blueprint('views_budget_category', __name__)

budget_category_schema = BudgetCategorySchema()
budget_categories_schema = BudgetCategorySchema(many=True)


@views_budget_category_bp.route('/budget-categories', methods=['GET'])
def get_budget_category():
    # Retrieve all bc records from database
    query = BudgetCategory.query.all()

    results = []
    # Serialize the bcs record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.budget_category_name
        }
        results.append(data)
    result = budget_categories_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)

@views_budget_category_bp.route('/budget-sub-category/<int:budget_category_id>', methods=['GET'])
def get_budget_sub_category(budget_category_id):
    # Retrieve all bc records from database
    query = BudgetSubCategories.query.filter_by(budget_category_id=budget_category_id).all()

    results = []
    # Serialize the bcs record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'budget_category_id': item.budget_category_id,
            'name': item.sub_category_name
        }
        results.append(data)

    # Return the serialized data as JSON response
    return jsonify(results)

@views_budget_category_bp.route('/income-categories', methods=['GET'])
def get_income_categories():
    # Retrieve all bc records from database
    query = IncomeCategories.query.all()

    results = []
    # Serialize the bcs record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.category_name
        }
        results.append(data)

    # Return the serialized data as JSON response
    return jsonify(results)