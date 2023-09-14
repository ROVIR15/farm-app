from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from Finance.Budget.models import Budget
from Finance.Budget.schema import BudgetSchema

views_budget_bp = Blueprint('views_budget', __name__)

budget_schema = BudgetSchema()
budgets_schema = BudgetSchema(many=True)


@views_budget_bp.route('/budget', methods=['GET'])
def get_budgets():
    # Retrieve all livestock records from the database
    query = Budget.query.options(subqueryload(Budget.items)).all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'month': item.month,
            'items': item.sleds
            # 'created_at': item.created_at,
        }
        results.append(data)
    result = budgets_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_budget_bp.route('/budget/<int:budget_id>', methods=['GET'])
def get_a_budget(budget_id):
    # Retrieve all livestock records from the database
    query = Budget.query.options(
        subqueryload(Budget.sleds)).get(budget_id)

    # Serialize the livestock data using the schema
    result = budget_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_budget_bp.route('/budget', methods=['POST'])
def post_budget():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    try:
        query = Budget(name=name, description=description)
        db.session.add(query)
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
            'message': f'Sorry, {name}! Failed to store livestock data. Error: {error_message}'
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
