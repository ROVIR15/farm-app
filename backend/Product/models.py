from db_connection import db


class Product (db.Model):
    id = db.Column(db.Integer(), primary_key=True)
    name = db.Column(db.String(255), nullable=False)
    unit_measurement = db.Column(db.String(10), nullable=False)
    description = db.Column(db.Text())

    # Define the 'categories' relationship
    categories = db.relationship(
        'ProductHasCategory', back_populates='product')

    def __repr__(self):
        return f'<Product {self.id}>'
