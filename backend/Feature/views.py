from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy.orm import joinedload, subqueryload
from Feature.models import Feature
from Feature.schema import FeatureSchema

views_feature_bp = Blueprint('view_feature', __name__)

feature_schema = FeatureSchema()
features_schema = FeatureSchema(many=True)


@views_feature_bp.route('/features', methods=['GET'])
def get_features():
    # Retrieve all feature records from the database
    query = Feature.query.all()

    results = []
    # Serialize the feature data using the schema
    for item in query:
        data = {
            'id': item.id,
            'type': item.type,
            'name': item.name
        }
        results.append(data)

    result = features_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_feature_bp.route('/feature/<int:feature_id>', methods=['GET'])
def get_a_feature(feature_id):
    # Retrieve all feature records from the database
    query = Feature.query.get(feature_id)

    # Serialize the feature data using the schema
    result = feature_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_feature_bp.route('/feature', methods=['POST'])
def post_feature():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    type = data.get('type')
    name = data.get('name')

    try:
        query = Feature(type=type, name=name)
        db.session.add(query)
        db.session.commit()

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
            'message': f'Sorry, {name}! Failed to store feature data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_feature_bp.route('/feature/<int:feature_id>', methods=['PUT'])
def update_feature(feature_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    type = data.get('type')

    # Assuming you have a Feature model and an existing feature object
    feature = Feature.query.get(feature_id)
    if feature:
        feature.name = name
        feature.type = type
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Feature {feature_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Feature {feature_id} not found.'
        }
        return jsonify(response), 404


@views_feature_bp.route('/feature/<int:feature_id>', methods=['DELETE'])
def delete_feature(feature_id):
    # Assuming you have a Feature model and an existing feature object
    feature = Feature.query.get(feature_id)
    if feature:
        db.session.delete(feature)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Feature {feature_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Feature {feature_id} not found.'
        }
        return jsonify(response), 404
