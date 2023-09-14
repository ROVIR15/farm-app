from db_connection import db
from sqlalchemy import Numeric
from datetime import datetime

class BudgetCategory (db.Model):
    __tablename__ = "budget_category"

    id = db.Column(db.Integer(), primary_key=True)
    budget_name = db.Column(db.String(150), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    def __repr__(self):
        return f'<BudgetCategory {self.name}>'
