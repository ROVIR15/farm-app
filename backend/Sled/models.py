from db import db

class Sled (db.Model):
  id = db.Column(db.Integer(), primary_key=True)
  block_area_id = db.Column(db.Integer(), nullable=False)
  name = db.Column(db.Integer(), nullable=False)
  description = db.Column(db.Text())

  def __repr__ (self):
    return f'<Sled {self.id}>'