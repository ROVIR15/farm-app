from db_connection import db


class Budget (db.Model):
    __tablename__ = "budget"

    id = db.Column(db.Integer(), primary_key=True)
    month = db.Column(db.Date(), nullable=False)

    def __repr__(self):
        return f'<Budget {self.name}>'
