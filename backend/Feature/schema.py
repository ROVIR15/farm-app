from marshmallow import Schema, fields

class FeatureSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    type = fields.Str(required=True)
    name = fields.Str(required=True)