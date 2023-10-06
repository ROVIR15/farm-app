from db_connection import db
from sqlalchemy import func
from datetime import datetime

class FeedingRecord(db.Model):
    __tablename__ = "consumption_record"

    id = db.Column(db.Integer(), primary_key=True)
    feed_category = db.Column(db.Integer(), nullable=False)
    block_area_id = db.Column(db.Integer(), db.ForeignKey(
        'block_area.id'), nullable=False)
    sku_id = db.Column(db.Integer(), db.ForeignKey(
        'sku.id'), nullable=False)
    date = db.Column(db.Date(), nullable=False)
    score = db.Column(db.Integer(), nullable=False)
    left = db.Column(db.Integer(), nullable=True)
    remarks = db.Column(db.Text(), nullable=True)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    # livestock = db.relationship('Sled', back_populates='block_area', lazy=True)
    block_area = db.relationship('BlockArea', back_populates='feeding_records', lazy=True)

    def __repr__(self):
        return f'<HealthRecord {self.name}>'


