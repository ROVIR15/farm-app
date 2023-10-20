from marshmallow import Schema, fields


class BudgetCategorySchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
