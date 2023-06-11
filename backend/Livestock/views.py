from db import db
from flask import Blueprint, jsonify
from sqlalchemy.orm import joinedload, subqueryload
from Livestock.models import Livestock
from Livestock.schema import LivestockSchema

views_bp = Blueprint('views', __name__)

livestock_schema = LivestockSchema()
livestocks_schema = LivestockSchema(many=True)

@views_bp.route('/livestocks', methods=['GET'])
def get_livestocks():
    # Retrieve all livestock records from the database
    query = Livestock.query.options(subqueryload(Livestock.info)).all()

    results = []
    # Serialize the livestock data using the schema
    for item in query:
        data = {
            'id': item.id,
            'name': item.name,
            'gender': item.gender,
            'bangsa': item.bangsa,
            'description': item.description,
            'created_at': item.created_at,
            'updated_at': item.updated_at,
            'info': {
                'block_area_id': item.info[0].block_area_id,
                'sled_id': item.info[0].sled_id
            }
        }
    results.append(data)
    result = livestocks_schema.dump(results)

    # Return the serialized data as JSON response
    return jsonify(result)


@views_bp.route('/livestock/<int:livestock_id>', methods=['GET'])
def get_a_livestock(livestock_id):
    # Retrieve all livestock records from the database
    query = Livestock.query.get(livestock_id)

    # Serialize the livestock data using the schema
    result = livestock_schema.dump(query)

    # Return the serialized data as JSON response
    return jsonify(result)
