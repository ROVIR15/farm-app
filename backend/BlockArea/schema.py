from marshmallow import Schema, fields

class SledSchema(Schema):
  block_area_id = fields.Int(allow_none=True)
  sled_id = fields.Int(allow_none=True)

class BlockAreaSchema(Schema):
  id = fields.Int(primary_key=True, dump_only=True)
  name = fields.Str(required=True)
  description = fields.text(required=False)
  sleds = fields.Nested(SledSchema, allow_none=True)