from marshmallow import Schema, fields
from Livestock.schema import LivestockSchema_new
from Record.FeedingRecord.schema import FeedListSchema

class SledSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    block_area_id = fields.Int(required=True)
    name = fields.Str(required=True)
    description = fields.Str(required=True)

class BlockAreaSchema(Schema):
  id = fields.Int(primary_key=True, dump_only=True)
  name = fields.Str(required=True)
  info = fields.Str(required=False)
  description = fields.Str(required=False)
  sleds = fields.Nested(SledSchema, many=True)
  livestock = fields.Nested(LivestockSchema_new, many=True)
  feeding_records = fields.Nested(FeedListSchema, many=True)