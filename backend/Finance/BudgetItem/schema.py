from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
from Finance.Expenditure.schema import ExpenditureSchema
from Finance.BudgetCategory.schema import BudgetCategorySchema

class BudgetItemSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    budget_category = fields.Nested(BudgetCategorySchema)
    budget_category_id = fields.Int(required=True)
    budget_category_name = fields.Str(required=True)
    amount = fields.Int(required=True)
    total_budget = fields.Int(required=True)
    total_expenditure = fields.Int(required=True)
    summary_text = fields.Str(required=True)
    left = fields.Int(required=True)
    expenditures = fields.Nested(ExpenditureSchema, many=True, require=False)
    created_at = CustomDateTimeField()
