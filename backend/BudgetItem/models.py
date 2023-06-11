from db import db
from sqlalchemy import Numeric
from datetime import datetime

class BudgetItem (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    budget_id = db.Column(db.Integer, nullable=False)
    name = db.Column(db.String(50), nullable=False)
    amount = db.Column(Numeric(precision=10, scale=4), nullable=False)

def __repr__(self):
    return f'<BudgetItem {self.name}>'
