from db import db

class BlockArea (db.Model):
  id = db.Column(db.Integer(), primary_key=True)
  name = db.Column(db.String(50), nullable=False)
  description = db.Column(db.Text)

  def __repr__ (self):
    return f'<BlockArea {self.name}>'