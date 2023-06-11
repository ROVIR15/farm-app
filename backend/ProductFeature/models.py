from db import db

class ProducFeature (db.Model):
  id = db.Column(db.Integer(), primary_key=True)
  product_id = db.Column(db.Integer(), nullable=False)
  feature_id = db.Column(db.Integer(), nullable=False)
  description = db.Column(db.String(100), nullable=False)

  def __repr__ (self):
    return f'<ProducFeature {self.id}>'