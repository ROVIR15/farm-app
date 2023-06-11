from db import db

class Product (db.Model):
  id = db.Column(db.Integer(), primary_key=True)
  name = db.Column(db.String(100), nullable=False)
  unit_measurement = db.Column(db.String(50), nullable=False)
  description = db.Column(db.Text())

  def __repr__ (self):
    return f'<Product {self.id}>'