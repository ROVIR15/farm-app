from db_connection import db
from datetime import datetime

class BudgetRevision (db.Model):
    __tablename__ = "budget_revision"

    id = db.Column(db.Integer(), primary_key=True)
    budget_id = db.Column(db.Integer(), db.ForeignKey('budget.id'),nullable=False)
    budget_category_id = db.Column(db.Integer(), db.ForeignKey('budget_category.id'),nullable=False)
    from_amount = db.Column(db.Float(), nullable=False)
    to_amount = db.Column(db.Float(), nullable=False)
    notes = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    def __repr__(self):
        return f'<BudgetRevision {self.name}>'
    
