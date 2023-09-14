from db_connection import db


class Sled (db.Model):
    __tablename__ = "sled"

    id = db.Column(db.Integer(), primary_key=True)
    block_area_id = db.Column(db.Integer(), db.ForeignKey('block_area.id'),nullable=False)
    name = db.Column(db.String(), nullable=False)
    description = db.Column(db.Text())

    # Define the `block_area` relationship
    block_area = db.relationship('BlockArea', foreign_keys=[block_area_id], lazy=True)

    def __init__(self, name, description, block_area_id):
        self.name = name
        self.description = description
        self.block_area_id = block_area_id