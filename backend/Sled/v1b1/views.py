from db_connection import db
from flask import Blueprint, request, jsonify

from sqlalchemy import desc
from sqlalchemy.orm import subqueryload, joinedload
from Sled.models import Sled
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from FarmProfile.HasSled.models import HasSled as FarmProfileHasSled
from FarmProfile.HasBlockArea.models import HasBlockArea as FarmProfileHasBlockArea
from FarmProfile.models import FarmProfileHasUsers

from Sled.schema import SledSchema

from auth import login_required, current_user, current_farm_profile

views_sled_bp = Blueprint('views_sled_v1_1', __name__)

sled_schema = SledSchema()
sleds_schema = SledSchema(many=True)

@views_sled_bp.route('/sled/move-to-block-area/<int:sled_id>', methods=['PUT'])
@login_required
def update_sled_to_block_area(sled_id):
    data = request.get_json()

    farm_profile_id = current_farm_profile()

    block_area_id = data.get('block_area_id')

    try:
        # check whether this sled is belongs to you
        query_fphba = FarmProfileHasBlockArea.query.filter_by(farm_profile_id=block_area_id)

        if not query_fphba:
            raise Exception('Cannot process this block area not belongs to you!')

        # check whether this sled is belongs to you
        query_fphs = FarmProfileHasSled.query.filter_by(farm_profile_id=farm_profile_id)

        if not query_fphs:
            raise Exception('Cannot process this sled not belongs to you!')

        # check whether you have sled and basl
        query_sled = Sled.query.get(sled_id)
        query_basl = BlockAreaSledLivestock.query.filter_by(sled_id=sled_id).all()

        if not query_sled:
            response = {
                'status': 'error',
                'message': 'Cannot found a sled'
            }

            return jsonify(response), 404

        else:
            query_sled.block_area_id = block_area_id
            db.session.commit()

            for item in query_basl:
                item.block_area_id = block_area_id
                db.session.commit()

        response = {
            'status': 'success',
            'message': f'Your sled and livestock stored in {sled_id} is moved to {block_area_id} and livest'
        }

        return jsonify(response), 200

    except Exception as e:
        # Handling the exception if storing the data fails
        db.session.rollback()
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to move sled data. Error: {error_message}'
        }

        return jsonify(response), 500