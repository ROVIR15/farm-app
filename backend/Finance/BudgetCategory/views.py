from db import db
from flask import Blueprint, request, jsonify
from Finance.BudgetCategory.models import BudgetCategory
from Finance.BudgetCategory.schema import BudgetCategorySchema

views_budget_category_bp = Blueprint('views_budget_category', __name__)

budget_category_schema = BudgetCategorySchema()
budget_categories_schema = BudgetCategorySchema(many=True)


@views_budget_category_bp.route('/budget-categorys', methods=['GET'])
def get_budget_category():
    # Retrieve all bc records from database
    query = BudgetCategory.query.all()

    results = []
    # Serialize the bcs record data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.name
        }
        results.append(data)
    result = budget_categories_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


# @views_budget_category_bp.route('/budget-category/<int:livestock_id>', methods=['GET'])
# def get_a_budget_category(livestock_id):
    # Retrieve Budget Category based on livestock_id from the database
    # query = BudgetCategory.query.get(livestock_id)

    # Serialize the livestock data using the schema
    # result = budget_category_schema.dump(query)

    # Return the serialized data as JSON response
    # return jsonify(result)


@views_budget_category_bp.route('/budget-category', methods=['POST'])
def post_budget_category():
    data = request.get_json()  # Get the JSON data from request body

    # Process the data or perform any desired operations
    livestock_id = data.get('livestock_id')
    score = data.get('score')
    remarks = data.get('remarks')

    try:
        query = BudgetCategory(livestock_id=livestock_id,
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


@views_budget_category_bp.route('/budget-category/<int:id>', methods=['PUT'])
def update_budget_category(id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    budget_name = data.get('budget_name')

    budget_category = BudgetCategory.query.get(id)
    if budget_category:
        budget_category.budget_name = budget_name
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


@views_budget_category_bp.route('/budget-category/<int:id>', methods=['DELETE'])
def delete_budget_category(id):
    # Assuming the system has bcs record model and an existing budget_category object
    query = BudgetCategory.query.get(id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Budget Category {id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message':  f'Budget Category {id} not found.'
        }
        return jsonify(response), 404

