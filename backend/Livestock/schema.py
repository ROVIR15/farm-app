from marshmallow import Schema, fields

class InfoSchema(Schema):
  block_area_id = fields.Int(allow_none=True)
  sled_id = fields.Int(allow_none=True)

class LivestockSchema(Schema):
    id = fields.Int(dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Int(required=True)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)
    info = fields.Nested(InfoSchema, allow_none=True)