from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class BudgetItemSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    budget_id = fields.Int(required=True)
    budget_category_id = fields.Int(required=True)
    amount = fields.Int(required=True)
    created_at = CustomDateTimeField()

class BudgetSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    items= fields.Nested(BudgetItemSchema, many=True)

