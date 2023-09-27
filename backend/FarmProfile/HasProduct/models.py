from db_connection import db
from datetime import datetime


class HasProduct (db.Model):
    __tablename__ = 'farm_profile_has_product'

    id = db.Column(db.Integer(), primary_key=True)
    farm_profile_id = db.Column(
        db.Integer(), db.ForeignKey('farm_profile.id'), nullable=False)
    product_id = db.Column(
        db.Integer(), db.ForeignKey('product.id'), nullable=False)  # db.ForeignKey('breeding.id')
    sku_id = db.Column(
        db.Integer(), db.ForeignKey('sku.id'), nullable=False)  # db.ForeignKey('breeding.id')

    product = db.relationship('Product', foreign_keys=[product_id])
    sku = db.relationship('SKU', foreign_keys=[sku_id])