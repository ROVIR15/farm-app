from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import not_
from sqlalchemy.orm import joinedload, subqueryload
from Product.models import Product as Product
from Feature.models import Feature
from ProductHasCategory.models import ProductHasCategory
from SKU.models import SKU

from FarmProfile.HasProduct.models import HasProduct
from ProductHasCategory.schema import ProductHasCategorySchema as ProductSchema

views_product_bp = Blueprint('views_product', __name__)

product_schema = ProductSchema()
products_schema = ProductSchema(many=True)


@views_product_bp.route('/products', methods=['GET'])
def get_products():

    # grandparents_with_grandchildren_named_bob = (
    #     session.query(Parent)
    #     .filter(Parent.children.any(Child.grandchildren.any(Grandchild.name == "Bob")))
    #     .options(
    #         subqueryload(Parent.children),
    #         subqueryload(Child.grandchildren)
    #     )
    #     .all()
    # )

   try:
        # Retrieve all product records from the database
        query = ProductHasCategory.query.options([
            subqueryload(ProductHasCategory.product),
            subqueryload(ProductHasCategory.category),
            subqueryload(ProductHasCategory.sku)
        ]).filter(not_(ProductHasCategory.category_id == 3))

        results = []
        # Serialize the product data using the schema
        for item in query:
            data = {
                'sku_id': item.sku.id,
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
    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry!, Failed to load collection of your product'
        }
        return jsonify(response), 500


@views_product_bp.route('/product/<int:product_id>', methods=['GET'])
def get_a_product(product_id):

    try:

        # Retrieve all product records from the database
        query = ProductHasCategory.query.options([
            subqueryload(ProductHasCategory.product),
            subqueryload(ProductHasCategory.category),
            subqueryload(ProductHasCategory.sku)
        ]).filter_by(product_id=product_id).first()

        if not query:
            response = {
                'status': 'error, not found'
            }
            return jsonify(response), 404

        # Serialize the product data using the schema
        result_query = {
            'sku_id': query.sku.id,
            'product_id': query.product_id,
            'category_id': query.category_id,
            'product_name': query.product.name,
            'category_name': query.category.name,
            'description': query.product.description,
            'unit_measurement': query.product.unit_measurement
        }
        result = product_schema.dump(result_query)

        # Return the serialized data as JSON response
        return jsonify(result), 200

    except Exception as e:
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to store product data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_product_bp.route('/product', methods=['POST'])
def post_product():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    category_id = data.get('category_id')
    unit_measurement = data.get('unit_measurement')
    description = data.get('description')
    feature1 = data.get('feature1')

    try:
        product_q = Product(name=name, unit_measurement=unit_measurement,
                            description=description)
        db.session.add(product_q)
        db.session.commit()

        phc = ProductHasCategory(
            category_id=category_id, product_id=product_q.id)
        db.session.add(phc)
        db.session.commit()

        if feature1:
            for item in feature1:
                feature = Feature(type=item.type, name=item.name)
                db.session.add(product_q)
                db.session.commit()

                sku_q = SKU(
                    product_id=product_q.id, name=name, feature_id=feature.id)
                db.session.add(sku_q)
                db.session.commit()

                fphp = HasProduct(
                    sku_id=sku_q.id, product_id=product_q.id, feature_id=feature.id)
                db.session.add(fphp)
                db.session.commit()
        else:
            sku_q = SKU(product_id=product_q.id, name=name)
            db.session.add(sku_q)
            db.session.commit()

            fphp = HasProduct(
                sku_id=sku_q.id, product_id=product_q.id)
            db.session.add(fphp)
            db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success'
        }

        return jsonify(response), 200

    except Exception as e:
        db.session.rollback()
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
    # sku = SKU.query.get()
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
