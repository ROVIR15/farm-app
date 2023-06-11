from db import db

class ProductHasCategory (db.Model):
  id = db.Column(db.Integer(), primary_key=True)
  category_id = db.Column(db.Integer(), nullable=False)
  sku_id = db.Column(db.Integer(), nullable=False)

  def __repr__ (self):
    return f'<ProductHasCategory {self.id}>'