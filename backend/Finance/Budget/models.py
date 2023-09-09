from db import db


class Budget (db.Model):
    __tablename__ = "budget"

    id = db.Column(db.Integer(), primary_key=True)
    month = db.Column(db.Date(), nullable=False)
