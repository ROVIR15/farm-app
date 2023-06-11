from db import db
from sqlalchemy import func
from datetime import datetime

class Budget (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    month = db.Column(db.Integer, nullable=False)


def __repr__(self):
    return f'<Budget {self.id}>'
