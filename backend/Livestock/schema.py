from marshmallow import Schema, fields


class LivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Int(required=True)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)
    # info = fields.Nested(InfoSchema, allow_none=True)