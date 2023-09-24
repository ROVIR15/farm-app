from marshmallow import Schema, fields
from Product.schema import ProductSchema
from Category.schema import CategorySchema

class ProductHasCategorySchema(Schema):
    sku_id = fields.Int(required=True)
    product_id = fields.Int(required=True)
    category_id = fields.Int(required=True)
    product_name = fields.Str(required=True)
    category_name = fields.Str(required=True)
    unit_measurement = fields.Str(required=True)