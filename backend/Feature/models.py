from db import db
from sqlalchemy import func
from datetime import datetime

class Feature (db.Model):
    id = db.Column(db.Integer, primary_key=True)
    type = db.Column(db.String(50), nullable=False)
    name = db.Column(db.String(100), nullable=False)

def __repr__(self):
    return f'<Livestock {self.name}>'
