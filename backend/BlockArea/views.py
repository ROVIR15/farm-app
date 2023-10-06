from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import func, desc
from sqlalchemy.orm import subqueryload
from BlockArea.models import BlockArea
from FarmProfile.HasBlockArea.models import HasBlockArea as FarmProfileHasBlockArea
from FarmProfile.models import FarmProfileHasUsers
from Record.FeedingRecord.models import FeedingRecord
from BlockArea.schema import BlockAreaSchema

from auth import login_required, current_user, current_farm_profile

views_block_area_bp = Blueprint('views_block_area', __name__)

block_area_schema = BlockAreaSchema()
blocks_area_schema = BlockAreaSchema(many=True)


@views_block_area_bp.route('/block-areas', methods=['GET'])
@login_required
def get_block_areas():
    # Retrieve all livestock records from the database
    # query = BlockArea.query.all()
    # query = BlockArea.query.options(subqueryload(BlockArea.sleds)).all()
    farm_profile_id = current_farm_profile()

    try:
        query = FarmProfileHasBlockArea.query.options(
            subqueryload(FarmProfileHasBlockArea.block_area)).filter_by(farm_profile_id=farm_profile_id).order_by(desc(FarmProfileHasBlockArea.block_area_id)).all()
        results = []

        if not query:
            response = {
                'status': 'error',
                'message': 'You dont have any block area'
            }
            return jsonify(response), 404

        # Serialize the livestock data using the schema
        for item in query:
            if hasattr(item, 'block_area'):
                data = {
                    'id': item.block_area.id,
                    'name': item.block_area.name,
                    'description': item.block_area.description,
                    # 'sleds': item.sleds
                    # 'created_at': item.created_at,
                }
                results.append(data)
        result = blocks_area_schema.dump(results)

        # Return the serialized data as JSON response
        return jsonify(result), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get collections of block area due to {error_message}'
        }
        return jsonify(response), 500


@views_block_area_bp.route('/block-area-info/<int:block_area_id>', methods=['GET'])
@login_required
def get_a_block_area_info(block_area_id):

    try:
        # Retrieve all livestock records from the database
        query = BlockArea.query.get(block_area_id)

        # Get the count of sleds and livestock for the BlockArea
        sleds_count = len(query.sleds) if query.sleds else 0
        livestock_count = len(query.livestock) if query.livestock else 0

        result = {
            'id': query.id,
            'name': query.name,
            'info': f'{sleds_count} Kandang, {livestock_count} Ekor',
            'description': query.description,
        }

        # Serialize the livestock data using the schema
        result = block_area_schema.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get collections of block area due to {error_message}'
        }
        return jsonify(response), 500


@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['GET'])
@login_required
def get_a_block_area(block_area_id):

    try:
        # Retrieve all livestock records from the database
        query = BlockArea.query.options([
            subqueryload(BlockArea.sleds),
            subqueryload(BlockArea.livestock)]).get(block_area_id)

        columns_to_select = [
            FeedingRecord.feed_category,
            FeedingRecord.block_area_id,
            func.to_char(FeedingRecord.created_at, 'DD Mon YYYY').label('day'),
            func.sum(FeedingRecord.score).label('total_score')
        ]
 
        query2 = FeedingRecord.query \
            .with_entities(*columns_to_select) \
            .filter_by(block_area_id=block_area_id) \
            .group_by(FeedingRecord.feed_category, FeedingRecord.block_area_id, func.to_char(FeedingRecord.created_at, 'DD Mon YYYY')) \
            .order_by(desc(func.to_char(FeedingRecord.created_at, 'DD Mon YYYY'))) \
            .all()

        
        # Create a dictionary to group data by 'day'
        day_map = {}

        results_data = []
        for item in query2:
            day = item.day
            feed_category = item.feed_category
            total_score = item.total_score

            result = {
                'day': day,
                'block_area_id': item.block_area_id,
                'feed_list': []
            }

            if day not in day_map:
                day_map[day] = {
                    "day": day,
                    "block_area_id": item.block_area_id,
                    "feed_list": []
                }

            day_map[day]["feed_list"].append({
                "feed_category": feed_category,
                "total_score": total_score
            })

            result.update(day_map[day])  # Use update to add items from day_map to result dictionary
            results_data.append(result)

        # Get the count of sleds and livestock for the BlockArea
        sleds_count = len(query.sleds) if query.sleds else 0
        livestock_count = len(query.livestock) if query.livestock else 0

        result = {
            'id': query.id,
            'name': query.name,
            'info': f'{sleds_count} Kandang, {livestock_count} Ekor',
            'description': query.description,
            'sleds': query.sleds,
            'livestock': [],
            'feeding_records': []
        }

        livestock_list = []
        if isinstance(query.livestock, list):
            for item in query.livestock:
                livestock = item.livestock
                _temp = {
                    'id': livestock.id,
                    'name': livestock.name,
                    'gender': livestock.gender,
                    'bangsa': livestock.bangsa,
                    'info': f'{livestock.get_gender_label()} | {livestock.calculate_age()} | Bangsa {livestock.bangsa}',
                    'description': livestock.description,
                    'created_at': livestock.created_at.strftime("%d %B %Y")
                }
                livestock_list.append(_temp)

        result['livestock'] = livestock_list
        result['feeding_records'] = results_data

        # Serialize the livestock data using the schema
        result = block_area_schema.dump(result)

        # Return the serialized data as JSON response
        return jsonify(result)

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry! Failed to get collections of block area due to {error_message}'
        }
        return jsonify(response), 500


@views_block_area_bp.route('/block-area', methods=['POST'])
@login_required
def post_block_area():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    try:
        query = BlockArea(name=name, description=description)
        db.session.add(query)
        db.session.commit()

        user_id = current_user()
        farm_profile = FarmProfileHasUsers.query.filter_by(
            user_id=user_id).first()

        has_block_area = FarmProfileHasBlockArea(
            block_area_id=query.id, farm_profile_id=farm_profile.farm_profile_id)
        db.session.add(has_block_area)
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


@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['PUT'])
@login_required
def update_block_area(block_area_id):
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    name = data.get('name')
    description = data.get('description')

    # Assuming you have a Livestock model and an existing livestock object
    block_area = BlockArea.query.get(block_area_id)
    if block_area:
        block_area.name = name
        block_area.description = description
        db.session.commit()

        # Create a response JSON
        response = {
            'status': 'success',
            'message': f'Block Area {block_area_id} has been updated.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {block_area_id} not found.'
        }
        return jsonify(response), 404


@views_block_area_bp.route('/block-area/<int:block_area_id>', methods=['DELETE'])
@login_required
def delete_block_area(block_area_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = BlockArea.query.get(block_area_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Block Area {block_area_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {block_area_id} not found.'
        }
        return jsonify(response), 404
