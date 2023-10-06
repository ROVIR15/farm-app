from db_connection import db
from flask import Blueprint, request, jsonify
from sqlalchemy import func, extract, desc
from sqlalchemy.orm import subqueryload
from Record.FeedingRecord.models import FeedingRecord
from Record.FeedingRecord.schema import FeedingRecordSchema

from utils.get_feed_category_label import get_feed_category_label

views_consumption_bp = Blueprint('views', __name__)

consumption_record_schema = FeedingRecordSchema()
consumption_records_schema = FeedingRecordSchema(many=True)


@views_consumption_bp.route('/feeding-records', methods=['GET'])
def get_consumption_records():
    # Get if there is argument send by user related with livestock_id
    block_area_id = request.args.get('block_area_id')

    try:
        if block_area_id is not None and block_area_id != '':
            # Retrieve all livestock records from the database
            query = FeedingRecord.query.all()
        else:
            query = FeedingRecord.query.filter(
                block_area_id==block_area_id)

        results = []
        # Serialize the livestock data using the schema
        for item in query:
            data = {
                'id': item.id,
                'feed_category': item.feed_category,
                'block_area_id': item.block_area_id,
                'sku_id': item.sku_id,
                'score': item.score,
                'left': item.left,
                'remarks': item.remarks,
                # 'created_at': item.created_at,
            }
            results.append(data)
        result = consumption_records_schema.dump(results)

        # Return the serialized data as JSON response
        return jsonify(result), 200
        
    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_consumption_bp.route('/feeding-record/<int:block_area_id>', methods=['GET'])
def get_a_consumption_record(block_area_id):
    try:

        columns_to_select = [
            FeedingRecord.feed_category,
            FeedingRecord.block_area_id,
            func.to_char(FeedingRecord.created_at, 'DD Mon YYYY').label('day'),
            func.sum(FeedingRecord.score).label('total_score')
        ]
        # Retrieve all livestock records from the database
        query = FeedingRecord.query \
        .with_entities(*columns_to_select) \
        .filter_by(block_area_id=block_area_id) \
        .group_by(FeedingRecord.feed_category, FeedingRecord.block_area_id, func.to_char(FeedingRecord.created_at, 'DD Mon YYYY')) \
        .order_by(desc(func.to_char(FeedingRecord.created_at, 'DD Mon YYYY'))) \
        .all()

        # Create a dictionary to group data by 'day'
        day_map = {}

        results = []
        for item in query:
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
                "feed_category": get_feed_category_label(feed_category),
                "total_score": total_score
            })

            result.update(day_map[day])            
            results.append(result)

        # Serialize the livestock data using the schema
        # result = consumption_records_schema.dump(query)

        # Return the serialized data as JSON response
        return jsonify(results), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Error: {error_message}'
        }

        return jsonify(response), 500


@views_consumption_bp.route('/feeding-record', methods=['POST'])
def post_consumption_record():
    data = request.get_json()  # Get the JSON data from the request body

    # Process the data or perform any desired operations
    # For example, you can access specific fields from the JSON data
    array = data.get('consumption_record')

    try:
        if not isinstance(array, list):
            raise Exception('payload must be in array')
        else:
            for item in array:
                feed_category = item['feed_category']
                block_area_id = item['block_area_id']
                sku_id = item['sku_id']
                score = item['score']
                left = item['left']
                remarks = item['remarks']

                query = FeedingRecord(
                    feed_category=feed_category,
                    block_area_id=block_area_id,
                    sku_id=sku_id,
                    score=score,
                    left=left,
                    remarks=remarks
                )
                db.session.add(query)
                db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Hello! Your Feeding has been recorded.'
            }

            return jsonify(response), 200

    except Exception as e:
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_consumption_bp.route('/feeding-record/<int:consumption_record_id>', methods=['PUT'])
def update_consumption_record(consumption_record_id):
    data = request.get_json()  # Get the JSON data from the request body

    try:
        # Process the data or perform any desired operations
        # For example, you can access specific fields from the JSON data
        block_area_id = data.get('block_area_id')
        sku_id = data.get('sku_id')
        score = data.get('score')
        left = data.get('left')
        remarks = data.get('remarks')
        consumption_record = FeedingRecord.query.get(consumption_record_id)

        if consumption_record:
            consumption_record.block_area_id = block_area_id
            consumption_record.sku_id = sku_id
            consumption_record.score = score
            consumption_record.left = left
            consumption_record.remarks = remarks
            db.session.commit()

            # Create a response JSON
            response = {
                'status': 'success',
                'message': f'Consumption {consumption_record_id} has been updated.'
            }
            return jsonify(response), 200

        else:
            response = {
                'status': 'error',
                'message': f'Block Area {consumption_record_id} not found.'
            }
            return jsonify(response), 404
    except Exception as e:
        # Assuming you have a Livestock model and an existing livestock object
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to update consumption record data. Error: {error_message}'
        }

        return jsonify(response), 500


@views_consumption_bp.route('/feeding-record/<int:consumption_record_id>', methods=['DELETE'])
def delete_consumption_record(consumption_record_id):
    # Assuming you have a Livestock model and an existing livestock object
    query = FeedingRecord.query.get(consumption_record_id)
    if query:
        db.session.delete(query)
        db.session.commit()

        response = {
            'status': 'success',
            'message': f'Block Area {consumption_record_id} has been deleted.'
        }
        return jsonify(response), 200
    else:
        response = {
            'status': 'error',
            'message': f'Block Area {consumption_record_id} not found.'
        }
        return jsonify(response), 404
