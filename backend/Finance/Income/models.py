from db_connection import db
from utils.index import CustomDateTimeField

class Income (db.Model):
    __tablename__ = "income"

    id = db.Column(db.Integer(), primary_key=True)
    income_category_id = db.Column(db.Integer(), db.ForeignKey('income_categories.id'), nullable=False)
    # income_category_id = db.Column(db.Integer(), db.ForeignKey('income_categories.id'),nullable=False)
    date = db.Column(db.Date(), nullable=False)
    amount = db.Column(db.Float(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = CustomDateTimeField()

    category_label = db.relationship('IncomeCategories', foreign_keys=[income_category_id])

    def __repr__(self):
        return f'<Income {self.name}>'
