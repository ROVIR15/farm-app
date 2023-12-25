from db_connection import db
from sqlalchemy import func
from datetime import datetime


class BlockArea (db.Model):
    __tablename__ = "block_area"

    id = db.Column(db.Integer(), primary_key=True)
    name = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    imageUrl = db.Column(db.Text, nullable=True)
    sleds = db.relationship('Sled', back_populates='block_area', lazy=True)
    livestock = db.relationship('BlockAreaSledLivestock', back_populates='block_area', lazy=True)
    feeding_records = db.relationship('FeedingRecord', back_populates='block_area', lazy=True)

    def __repr__(self):
        return f'<BlockArea {self.name}>'
