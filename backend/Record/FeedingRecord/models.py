from db_connection import db
from sqlalchemy import func
from datetime import datetime

class FeedingRecord(db.Model):
    __tablename__ = "consumption_record"

    id = db.Column(db.Integer(), primary_key=True)
    food_category = db.Column(db.Integer(), nullable=False)
    block_area_id = db.Column(db.Integer(), db.ForeignKey(
        'livestock.id'), nullable=False)
    sku_id = db.Column(db.Integer(), db.ForeignKey(
        'sku.id'), nullable=False)
    date = db.Column(db.Date(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    # livestock = db.relationship('Sled', back_populates='block_area', lazy=True)

    def __repr__(self):
        return f'<HealthRecord {self.name}>'
