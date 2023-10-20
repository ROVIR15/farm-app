from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
from Finance.Expenditure.schema import ExpenditureSchema

class BudgetItemSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    budget_category_id = fields.Int(required=True)
    amount = fields.Int(required=True)
    expenditures = fields.Nested(ExpenditureSchema, many=True, require=False)
    created_at = CustomDateTimeField()
