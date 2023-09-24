from db_connection import db

class SKU(db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    name = db.Column(db.String(), nullable=False)
    product_id = db.Column(db.Integer(), db.ForeignKey('sku.product_id'), nullable=False)
    feature_id = db.Column(db.Integer(), nullable=True)

    product = db.relationship('ProductHasCategory',back_populates='sku')

    def __repr__(self):
        return f'<ProducFeature {self.id}>'
