from db import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedload, subqueryload
from Product.models import Product as Product_q
from ProductHasCategory.models import ProductHasCategory as Product
from SKU.models import SKU

from ProductHasCategory.schema import ProductHasCategorySchema as ProductSchema

views_product_bp = Blueprint('views_product', __name__)

product_schema = ProductSchema()
products_schema = ProductSchema(many=True)


@views_product_bp.route('/products', methods=['GET'])
def get_products():
    # Retrieve all product records from the database
    query = Product.query.options([
        subqueryload(Product.product),
        subqueryload(Product.category),
    ]).all()

    results = []
    # Serialize the product data using the schema
    for item in query:
        data = {
            'product_id': item.product_id,
            'category_id': item.category_id,
            'product_name': item.product.name,
            'category_name': item.category.name,
            'description': item.product.description,
            'unit_measurement': item.product.unit_measurement
        }
        results.append(data)

    result = products_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_product_bp.route('/product/<int:product_id>', methods=['GET'])
def get_a_product(product_id):
    # Retrieve all product records from the database
    query = Product.query.get(product_id)

    # Serialize the product data using the schema
    result = product_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_product_bp.route('/product', methods=['POST'])
def post_product():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    category_id = data.get('category_id')
    unit_measurment = data.get('unit_measurment')
    description = data.get('description')
    feature1 = data.get(feature1)

    try:
        product_q = Product(name=name, unit_measurment=unit_measurment,
                        description=description)
        db.session.add(product_q)

        product_has_category_q = Product_q(product_id=product_q['id'], category_id=category_id)
        db.session.add(product_has_category_q)

        if feature1:
            for item in feature1:
                sku_q = SKU(product_id=product_q['id'], feature_id=item, description=description)
                db.session.add(sku_q)
        else:
            sku_q = SKU(product_id=product_q['id'], description=description)

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
            'message': f'Sorry, {name}! Failed to store product data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_product_bp.route('/product/<int:product_id>', methods=['PUT'])
def update_product(product_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

    # Assuming you have a Product model and an existing product object
    product = Product.query.get(product_id)
    if product:
        product.name = name
        product.gender = gender
        product.bangsa = bangsa
        product.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Product {product_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Product {product_id} not found.'
        }
        return jsonify(response), 404


@views_product_bp.route('/product/<int:product_id>', methods=['DELETE'])
def delete_product(product_id):
    # Assuming you have a Product model and an existing product object
    product = Product.query.get(product_id)
    sku = SKU.query.get()
    if product:
        db.session.delete(product)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Product {product_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Product {product_id} not found.'
        }
        return jsonify(response), 404
