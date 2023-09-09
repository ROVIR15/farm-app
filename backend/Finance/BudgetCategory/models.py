from db import db
from utils.index import CustomDateTimeField

class BudgetCategory (db.Model):
    __tablename__ = "budget_category"

    id = db.Column(db.Integer(), primary_key=True)
    budget_name = db.Column(db.String(150), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
