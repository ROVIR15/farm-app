from db_connection import db
from flask import Blueprint, request, jsonify, current_app
from sqlalchemy import func, desc
from sqlalchemy.orm import joinedload, subqueryload

from FarmProfile.HasLivestock.models import HasLivestock as FarmProfileHasLivestock
from BlockAreaSledLivestock.models import BlockAreaSledLivestock

from Options.Livestock.schema import LivestockSchema_options


from auth import login_required, current_farm_profile

views_opts_livestock = Blueprint('views_opts_livestock', __name__)

livestock_schema_new = LivestockSchema_options()
livestocks_schema_new = LivestockSchema_options(many=True)

@views_opts_livestock.route('/livestocks', methods=['GET'])
@login_required
def get_livestocks():
    # Retrieve all livestock records from the database
    gender = request.args.get('gender')
    farm_profile_id = current_farm_profile()

    try:
        query = FarmProfileHasLivestock.query.options([subqueryload(FarmProfileHasLivestock.livestock)]).filter_by(
            farm_profile_id=farm_profile_id).order_by(desc(FarmProfileHasLivestock.livestock_id)).all()

        results = []
        if not query:
            e = 'You dont have any livestock'
            response = {
                'status': 'error',
                'message': e
            }

            return jsonify(response)

        def gender_filter_(_param):
            if 'gender' in _param:
                if int(_param['gender']) == int(gender):
                    return _param

        # Serialize the livestock data using the schema
        for item in query:

            query_block_area_livestock = BlockAreaSledLivestock.query.filter_by(
                livestock_id=item.livestock_id).first()

            if hasattr(item, 'livestock'):
                date_obj = item.livestock.created_at

                # Format the date as "DD MMMM YYYY"
                formatted_date = date_obj.strftime("%d %B %Y")

                if query_block_area_livestock:
                    data = {
                        'id': item.livestock.id,
                        'name': item.livestock.name,
                        'info': f'Tinggal di {query_block_area_livestock.sled.name} di {query_block_area_livestock.block_area.name} | {item.livestock.get_gender_label()}',
                        'gender': item.livestock.gender,
                        'created_at': formatted_date
                    }
                else:
                    data = {
                        'id': item.livestock.id,
                        'name': item.livestock.name,
                        'bangsa': item.livestock.bangsa,
                        'info': f'Belum di taruh kandang | {item.livestock.get_gender_label()}',
                        'gender': item.livestock.gender,
                        'created_at': formatted_date
                    }

                results.append(data)

        if gender is not None:
            results = filter(gender_filter_, results)
            results = list(results)
            print(results)

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

