from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class WeightRecordSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    livestock_id = fields.Int(required=True)
    date = fields.Date(required=True)
    score = fields.Float(required=True)
    prev_score = fields.Float(required=False)
    growth = fields.Str(required=False)
    remarks = fields.Str(required=True)
    created_at = CustomDateTimeField()
