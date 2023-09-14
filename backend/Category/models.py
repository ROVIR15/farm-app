from db_connection import db
from sqlalchemy import func
from datetime import datetime

class Category (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(150), nullable=False)

    products = db.relationship('ProductHasCategory', back_populates='category')