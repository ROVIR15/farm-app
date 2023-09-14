from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import subqueryload
from Category.models import Category
from Category.schema import CategorySchema

views_category_bp = Blueprint('views_category', __name__)

category_schema = CategorySchema()
blocks_area_schema = CategorySchema(many=True)


@views_category_bp.route('/categories', methods=['GET'])
def get_block_areas():
    # Retrieve all livestock records from the database
    query = Category.query.all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.name,
            # 'created_at': item.created_at,
        }
        results.append(data)
    result = blocks_area_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_category_bp.route('/category/<int:category_id>', methods=['GET'])
def get_a_block_area(category_id):
    # Retrieve all livestock records from the database
    query = Category.query.get(category_id)

    # Serialize the livestock data using the schema
    result = category_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_category_bp.route('/category', methods=['POST'])
def post_block_area():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')

    try:
        query = Category(name=name)
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

@views_category_bp.route('/category/<int:category_id>', methods=['PUT'])
def update_block_area(category_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    block_area = Category.query.get(category_id)
    if block_area:
        block_area.name = name
        block_area.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Category {category_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Category {category_id} not found.'
        }
        return jsonify(response), 404


@views_category_bp.route('/category/<int:category_id>', methods=['DELETE'])
def delete_block_area(category_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = Category.query.get(category_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Category {category_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Category {category_id} not found.'
        }
        return jsonify(response), 404