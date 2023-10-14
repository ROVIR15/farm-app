from marshmallow import Schema, fields


class BudgetCategorySchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    budget_category_id = fields.Int(required=True)
    sub_category_name = fields.Str(required=True)
