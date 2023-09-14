from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class HealthRecordSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    sku_id = fields.Int(required=True)
    block_area_id = fields.Int(required=True)
    date = fields.Date(required=True)
    score = fields.Int(required=True)
    remarks = fields.Str(required=True)
    created_at = CustomDateTimeField()
