from db import db
from datetime import datetime

class BlockAreaSledLivestock (db.Model):
    id = db.Column(db.Integer(), nullable=False, primary_key=True)
    livestock_id = db.Column(db.Integer(), db.ForeignKey('livestock.id'), nullable=False)
    # livestock=db.relationship('Livestock', backref='block_area_sled_livestock', useList=False)
    block_area_id = db.Column(db.Integer(), db.ForeignKey('block_area.id'), nullable=False)
    # block_area = db.relationship('BlockArea', backref='block_area_sled_livestock', useList=False)
    sled_id = db.Column(db.Integer(), db.ForeignKey('sled.id'), nullable=False)
    # sled = db.relationship('Sled', backref='block_area_sled_livestock', useList=False)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)
    updated_at = db.Column(db.DateTime(timezone=True),
                           default=datetime.utcnow, onupdate=datetime.utcnow)

    def __repr__ (self):
      return f'<BlockAreaSledLivestock {self.id}>'