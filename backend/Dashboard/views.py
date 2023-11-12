from flask import Blueprint, request, jsonify
from sqlalchemy import func, and_, extract
from datetime import datetime
from decimal import Decimal

from FarmProfile.HasLivestock.models import HasLivestock
from Record.BCSRecord.models import BCSRecord
from Record.WeightRecord.models import WeightRecord

from auth import login_required, current_farm_profile

views_dashboard_bp = Blueprint('views_dashboard', __name__)


@views_dashboard_bp.route('/feeding-graph', methods=['GET'])
@login_required
def get_feeding_graph():
    month_year = request.args.get('month-year')

    if isinstance(month_year, str):
        param = month_year.split("-")
        month = param[0]
        year = param[1]
    else:
        date = datetime.now()
        month = date.month
        year = date.year

    try:
        farm_profile_id = current_farm_profile()

        has_livestock = HasLivestock.query \
            .with_entities(HasLivestock.livestock_id).filter_by(
                farm_profile_id=farm_profile_id).all()

        livestock_list = [item.livestock_id for item in has_livestock]

        b_columns_to_select = [
            BCSRecord.date,
            # func.to_char(BCSRecord.created_at, 'DD-MM-YYYY').label('day'),
            func.avg(BCSRecord.score).label('bcs_score')
        ]

        bcs_query = BCSRecord.query \
            .with_entities(*b_columns_to_select) \
            .filter(
                BCSRecord.livestock_id.in_(livestock_list),
                extract('month', BCSRecord.date) == month,
                extract('year', BCSRecord.date) == year
            ) \
            .group_by(BCSRecord.date) \
            .all()

        bcs_results = {
            'label': 'Skor BCS',
            'score': [],
            'date': []
        }
        for item in bcs_query:
            bcs_results['date'].append(item.date.strftime("%d-%m-%Y"))
            score = f'{item.bcs_score:.2f}'
            bcs_results['score'].append(score)

        w_columns_to_select = [
            WeightRecord.date,
            # func.to_char(BCSRecord.created_at, 'DD-MM-YYYY').label('day'),
            func.avg(WeightRecord.score).label('weight_score')
        ]


        weight_query = WeightRecord.query \
            .with_entities(*w_columns_to_select) \
            .filter(
                WeightRecord.livestock_id.in_(livestock_list),
                extract('month', WeightRecord.date) == month,
                extract('year', WeightRecord.date) == year
            ) \
            .group_by(WeightRecord.date) \
            .all()

        weight_results = {
            'label': 'Skor Berat Badan',
            'score': [],
            'date': []
        }

        for item in weight_query:
            weight_results['date'].append(item.date.strftime("%d-%m-%Y"))
            score = f'{item.weight_score:.2f}'
            weight_results['score'].append(score)

        return jsonify({'bcs_results': bcs_results, 'weight_results': weight_results}), 200

    except Exception as e:
        error_message = str(e)
        response = {
            'status': 'error',
            'message': f'Sorry, Failed to get livestock collections data. Error: {error_message}'
        }

        return jsonify(response), 500
