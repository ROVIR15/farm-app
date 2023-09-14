from marshmallow import Schema, fields

class ProductSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    unit_measurement = fields.Str(required=True)
    description = fields.Str(required=True)