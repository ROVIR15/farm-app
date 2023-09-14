from db_connection import db
from Product.models import Product
from Category.models import Category

class ProductHasCategory(db.Model):
    __tablename__ = 'product_has_category'

    # Define the primary key columns and foreign keys with target table specified
    product_id = db.Column(db.Integer, db.ForeignKey('product.id'), primary_key=True)
    category_id = db.Column(db.Integer, db.ForeignKey('category.id'), primary_key=True)

    # Define the relationships correctly with the foreign keys
    product = db.relationship('Product', back_populates='categories', foreign_keys=[product_id], lazy=True)
    category = db.relationship('Category', back_populates='products', foreign_keys=[category_id], lazy=True)

    def __repr__(self):
        return f'<ProductHasCategory {self.product_id}-{self.category_id}>'