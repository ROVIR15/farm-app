from marshmallow import Schema, fields
from BlockArea.schema import BlockAreaSchema

class SledSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    block_area_id = fields.Int(required=True)
    name = fields.Str(required=True)
    description = fields.Str(required=True)
    block_area = fields.Nested(BlockAreaSchema, allow_none=True)