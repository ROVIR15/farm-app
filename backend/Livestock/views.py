from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import desc
from sqlalchemy.orm import joinedload, subqueryload
from Livestock.models import Livestock
from Livestock.schema import LivestockSchema
from Livestock.schema import LivestockSchema_new

from ProductHasCategory.models import ProductHasCategory
from Product.models import Product
from SKU.models import SKU

from FarmProfile.HasLivestock.models import HasLivestock as FarmProfileHasLivestock
from FarmProfile.models import FarmProfileHasUsers

from datetime import datetime

from auth import login_required, current_farm_profile

views_bp = Blueprint('views_livestock', __name__)

livestock_schema = LivestockSchema()
livestocks_schema = LivestockSchema(many=True)

livestock_schema_new = LivestockSchema_new()
livestocks_schema_new = LivestockSchema_new(many=True)

@views_bp.route('/livestocks', methods=['GET'])
@login_required
def get_livestocks():
    # Retrieve all livestock records from the database
    farm_profile_id = current_farm_profile()

    try:
        query = FarmProfileHasLivestock.query.options([subqueryload(FarmProfileHasLivestock.livestock)]).filter_by(
            farm_profile_id=farm_profile_id).order_by(desc(FarmProfileHasLivestock.livestock_id)).all()
        # query = Livestock.query.all()

        results = []
        if not query:
            e = 'You dont have any livestock'
            response = {
                'status': 'error',
                'message': e
            }

            return jsonify(response)
        # Serialize the livestock data using the schema
        for item in query:
            if hasattr(item, 'livestock'):
                date_obj = item.livestock.created_at
            
                # Format the date as "DD MMMM YYYY"
                formatted_date = date_obj.strftime("%d %B %Y")

                data = {
                    'id': item.livestock.id,
                    'name': item.livestock.name,
                    'gender': item.livestock.gender,
                    'bangsa': item.livestock.bangsa,
                    'info': f'{item.livestock.get_gender_label()} | {item.livestock.calculate_age()} | Bangsa {item.livestock.bangsa}',
                    'description': item.livestock.description,
                    'created_at': formatted_date,
                }
                results.append(data)

        result = livestocks_schema_new.dump(results)
        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to get livestock collections data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock/<int:livestock_id>', methods=['GET'])
@login_required
def get_a_livestock(livestock_id):

    try:
        # Retrieve all livestock records from the database
        query = Livestock.query.options([
            subqueryload(Livestock.weight_records),
            subqueryload(Livestock.bcs_records),
            subqueryload(Livestock.health_records)
        ]).get(livestock_id)

        result = {
            'id': query.id,
            'name': query.name,
            'gender': query.gender,
            'bangsa': query.bangsa,
            'info': f'{query.get_gender_label()} | {query.calculate_age()} | Bangsa {query.bangsa}',
            'description': query.description,
            'bcs_records': query.bcs_records,
            'weight_records': query.weight_records,
            'health_records': query.health_records
        }

        # Serialize the livestock data using the schema
        result = livestock_schema.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get {livestock_id} livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_bp.route('/livestock', methods=['POST'])
@login_required
def post_livestock():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    birth_date = data.get('birth_date')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

    try:
        farm_profile_id = current_farm_profile()

        if not farm_profile_id:
            raise Exception("Cannot find farm profile!")
        else:
            query = Livestock(name=name, gender=gender, birth_date=birth_date,
                              bangsa=bangsa, description=description)
            db.session.add(query)
            db.session.commit()

            product = Product(
                name=name, unit_measurement="ekor", description="none")
            db.session.add(product)
            db.session.commit()

            phc = ProductHasCategory(product_id=product.id, category_id=3)
            db.session.add(phc)
            db.session.commit()

            sku = SKU(product_id=product.id, name=name)
            db.session.add(sku)
            db.session.commit()

            has_livestock = FarmProfileHasLivestock(
                livestock_id=query.id, farm_profile_id=farm_profile_id)
            db.session.add(has_livestock)
            db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Hello, {name}! Your message has been received.',
                'livestock_id': query.id
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


@views_bp.route('/livestock/<int:livestock_id>', methods=['PUT'])
@login_required
def update_livestock(livestock_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    gender = data.get('gender')
    bangsa = data.get('bangsa')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    livestock = Livestock.query.get(livestock_id)
    if livestock:
        livestock.name = name
        livestock.gender = gender
        livestock.bangsa = bangsa
        livestock.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Livestock {livestock_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Livestock {livestock_id} not found.'
        }
        return jsonify(response), 404


@views_bp.route('/livestock/<int:livestock_id>', methods=['DELETE'])
@login_required
def delete_livestock(livestock_id):
    # Assuming you have a Livestock model and an existing livestock object
    livestock = Livestock.query.get(livestock_id)
    if livestock:
        db.session.delete(livestock)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Livestock {livestock_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Livestock {livestock_id} not found.'
        }
        return jsonify(response), 404
