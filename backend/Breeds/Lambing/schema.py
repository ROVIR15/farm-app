from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

from Livestock.schema import LivestockSchema_new

class LambingSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    breeding_id = fields.Int(required=True)
    livestock_id = fields.Int(required=True)
    created_at = CustomDateTimeField()
    livestock = fields.Nested(LivestockSchema_new, allow_none=False)