from db_connection import db
from utils.index import CustomDateTimeField

class Expenditure (db.Model):
    __tablename__ = "expenditure"

    id = db.Column(db.Integer(), primary_key=True)
    date = db.Column(db.Date(), nullable=False)
    budget_category_id = db.Column(db.Integer, db.ForeignKey('budget_category.id'), db.ForeignKey('expenditure.budget_category_id'), nullable=False)
    budget_sub_category_id = db.Column(db.Integer, db.ForeignKey('budget_sub_categories.id'), nullable=False)
    sku_id = db.Column(db.Integer(), db.ForeignKey('sku.id'),nullable=True)
    amount = db.Column(db.Float(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = CustomDateTimeField()

    budget_sub_category = db.relationship('BudgetSubCategories', foreign_keys=[budget_sub_category_id], lazy=True)

    def __repr__(self):
        return f'<Expenditure {self.name}>'
