from sqlalchemy import func, desc, asc
from datetime import datetime, timedelta
from db_connection import db

from Record.HeightRecord.models import HeightRecord
from Record.WeightRecord.models import WeightRecord
from Record.BCSRecord.models import BCSRecord
from Record.HealthRecord.models import HealthRecord


class Livestock (db.Model):
    __tablename__ = "livestock"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    birth_date = db.Column(db.Date(), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    gender = db.Column(db.Integer, nullable=False)
    bangsa = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    imageurl = db.Column(db.Text, nullable=False)
    live = db.relationship('BlockAreaSledLivestock',
                           back_populates='livestock', lazy=True)

    # parent = db.relationship('Descendant', back_populates='livestock', lazy=True)
    weight_records = db.relationship('WeightRecord', back_populates='livestock', order_by=asc(
        WeightRecord.created_at), lazy=True)
    height_records = db.relationship('HeightRecord', back_populates='livestock', order_by=asc(
        HeightRecord.created_at), lazy=True)
    bcs_records = db.relationship('BCSRecord', back_populates='livestock', order_by=asc(
        BCSRecord.created_at), lazy=True)
    health_records = db.relationship('HealthRecord', back_populates='livestock', order_by=asc(
        HealthRecord.created_at), lazy=True)

    def get_gender_label(self):
        gender_mapping = {1: 'Jantan', 2: 'Betina'}
        return gender_mapping.get(self.gender, None)

    def formatted_created_date(self):
        # Convert the date string to a datetime object
        date_obj = datetime.strptime(self.created_at, "%Y-%m-%dT%H:%M:%S.%f")

        # Format the date as "DD MMMM YYYY"
        formatted_date = date_obj.strftime("%d %B %Y")

        return formatted_date

    def calculate_age(self):
        if self.birth_date is None:
            return "Please defined birth date of the livestock"

        current_date = datetime.now().date()
        age = current_date - self.birth_date

        years = age.days // 365
        remaining_days = age.days % 365
        months = remaining_days // 30

        if years > 0 and months > 0:
            return f"{years} Tahun {months} Bulan"
        elif years > 0:
            return f"{years} Tahun"
        elif months > 0:
            return f"{months} Bulan"
        else:
            return "Kurang dari sebulan"


def __repr__(self):
    return f'<Livestock {self.name}>'
