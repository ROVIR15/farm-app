from marshmallow import Schema, fields

class SledSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    block_area_id = fields.Int(required=True)
    name = fields.Str(required=True)
    description = fields.Str(required=True)

class BlockAreaSchema(Schema):
  id = fields.Int(primary_key=True, dump_only=True)
  name = fields.Str(required=True)
  description = fields.Str(required=False)
  sleds = fields.Nested(SledSchema, many=True)