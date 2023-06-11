from db import db
from sqlalchemy import Numeric
from datetime import datetime

class Expenditure (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    budget_id = db.Column(db.Integer, nullable=False)
    budget_item_id = db.Column(db.Integer, nullable=False)
    sku_id = db.Column(db.Integer, nullable=False)
    amount = db.Column(Numeric(precision=10, scale=2), nullable=False)
    remarks = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    updated_at = db.Column(db.DateTime(timezone=True),
                           default=datetime.utcnow, onupdate=datetime.utcnow)


def __repr__(self):
    return f'<Expenditure {self.id}>'
