from db_connection import db
from datetime import datetime

class Sled (db.Model):
    __tablename__ = "sled"

    id = db.Column(db.Integer(), primary_key=True)
    block_area_id = db.Column(db.Integer(), db.ForeignKey('block_area.id'),nullable=False)
    name = db.Column(db.String(), nullable=False)
    description = db.Column(db.Text())
    imageurl = db.Column(db.Text(), nullable=True)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    # Define the `block_area` relationship
    block_area = db.relationship('BlockArea', foreign_keys=[block_area_id], lazy=True)

    def __init__(self, name, description, block_area_id):
        self.name = name
        self.description = description
        self.block_area_id = block_area_id