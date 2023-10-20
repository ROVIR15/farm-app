from db_connection import db
from sqlalchemy import Numeric
from datetime import datetime

class IncomeCategories (db.Model):
    __tablename__ = "income_categories"

    id = db.Column(db.Integer(), primary_key=True)
    category_name = db.Column(db.String(150), nullable=False)

    def __repr__(self):
        return f'<IncomeCategories {self.name}>'
