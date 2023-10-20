from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class ExpenditureSchema(Schema):
    id = fields.Int(required=True)
    date = fields.Str(required=True)
    budget_category_id = fields.Int(required=True)
    budget_sub_category_id = fields.Int(required=True)
    sku_id = fields.Int(required=True)
    amount = fields.Float(required=True)
    remarks = fields.Str(required=True)