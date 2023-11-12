from db_connection import db
from flask import Blueprint, request, jsonify

from sqlalchemy import desc
from sqlalchemy.orm import subqueryload
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from Sled.models import Sled
from FarmProfile.HasSled.models import HasSled as FarmProfileHasSled

from Options.Sled.schema import SledSchema_options

from auth import login_required, current_user, current_farm_profile

views_opt_sled_bp = Blueprint('views_opt_sled', __name__)

sled_schema = SledSchema_options()
sleds_schema = SledSchema_options(many=True)

@views_opt_sled_bp.route('/sleds', methods=['GET'])
@login_required
def get_sleds():

    farm_profile_id = current_farm_profile()

    # Retrieve all livestock records from the database
    query = FarmProfileHasSled.query.options([subqueryload(
        FarmProfileHasSled.sled).subqueryload(Sled.block_area)]).filter_by(farm_profile_id=farm_profile_id).order_by(desc(FarmProfileHasSled.sled_id)).all()

    try:
        if query:
            results = []

            # Serialize the livestock data using the schema
            for item in query:
                if item.sled.block_area:
                    block_area_name = item.sled.block_area.name
                    block_area_description = item.sled.block_area.description
                else:
                    block_area_name = ""
                    block_area_description = ""

                livestock_number = BlockAreaSledLivestock.query.filter_by(block_area_id=item.sled.block_area_id, sled_id=item.sled.id).count()

                data = {
                    'id': item.sled.id,
                    'block_area_id': item.sled.block_area_id,
                    'name': item.sled.name,
                    'info': f'terisi {livestock_number} ekor' if livestock_number else 'Tidak ada',
                    'block_area_name': block_area_name
                }
                results.append(data)
            result = sleds_schema.dump(results)

            # Return the serialized data as JSON response
            return jsonify(result)
        else:
            response = {
                'status': 'error',
                'message': 'You dont have any sled'
            }

            return jsonify(response)

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to store livestock data. Error: {error_message}'
        }

        return jsonify(response), 500

