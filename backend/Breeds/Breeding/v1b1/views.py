from db_connection import db
from flask import Blueprint, request, jsonify, current_app
from sqlalchemy import desc
from sqlalchemy.orm import subqueryload

from datetime import datetime

from Breeds.Breeding.models import Breeding
from FarmProfile.HasBreeding.models import HasBreeding

from auth import current_farm_profile, login_required

from Breeds.Breeding.schema import BreedingSchema
from Breeds.BreedingHistory.schema import BreedingHistorySchema
from Breeds.Lambing.schema import LambingSchema

views_breeding_bp = Blueprint('views_breeding_v1_1_new', __name__)

breeding_record_schema = BreedingSchema()
breeding_records_schema = BreedingSchema(many=True)

breeding_history_record_schema = BreedingHistorySchema()
breeding_histories_record_schema = BreedingHistorySchema(many=True)

lambing_record_schema = LambingSchema()
lambing_records_schema = LambingSchema(many=True)


@views_breeding_bp.route('/breeding-info/<int:breeding_id>', methods=['GET'])
@login_required
def get_a_breeding_info(breeding_id):
    try:
        farm_profile_id = current_farm_profile()
        query = HasBreeding.query.options([subqueryload(HasBreeding.breedings).subqueryload(Breeding.breeding_status)]) \
            .filter_by(
            farm_profile_id=farm_profile_id,
            breeding_id=breeding_id
        ) \
            .first()

        print(query)
        if not query:
            return jsonify({ 'status': 'error', 'message': 'Your data cannot be found!'}), 404
        else:
            item = query
            if not isinstance(item, object):
                raise Exception("Not found any breeding record")
            else:
                date = item.created_at.strftime("%d %b %Y")
                ldate = item.breedings.date.strftime("%d-%m-%Y")
                result = {
                    "id": item.breedings.id,
                    "name": f'Breeding #{item.breedings.id}',
                    "farm_profile_id": item.farm_profile_id,
                    "livestock_male_id": item.breedings.livestock_male_id,
                    "livestock_female_id": item.breedings.livestock_female_id,
                    "livestock_male_name": item.breedings.livestock_male.name,
                    "livestock_female_name": item.breedings.livestock_female.name,
                    "sled_id": item.breedings.sled_id,
                    "is_active": item.breedings.is_active,
                    "created_at": date,
                    "date": ldate
                }
                results = breeding_record_schema.dump(result)
                return jsonify(results), 200
        # breedings = HasBreeding.query().filter_by(farm_profile_id=farm_profile_id)
    except Exception as e:
        db.session.rollback()
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry error, due to {error_message}'
        }

        return jsonify(response), 500
