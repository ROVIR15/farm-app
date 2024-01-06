from db_connection import db
from datetime import datetime

class MilkRecord(db.Model):
    __tablename__ = "milk_record"

    id = db.Column(db.Integer(), primary_key=True)
    livestock_id = db.Column(db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    date = db.Column(db.Date(), nullable=False)
    score = db.Column(db.Float(), nullable=False)
    remarks = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    livestock = db.relationship('Livestock', foreign_keys=[livestock_id], lazy=True)

    def to_dict(self):
        return {
            'id': self.id,
            'livestock_id': self.livestock_id,
            'date': str(self.date),
            'score': self.score,
            'remarks': self.remarks
        }

    def __repr__(self):
        return f'<MilkRecord {self.id}>'