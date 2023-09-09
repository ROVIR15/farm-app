from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class BCSRecordSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    livestock_id = fields.Int(required=True)
    date = fields.Date(required=True)
    score = fields.Float(required=True)
    remarks = fields.Str(required=True)
    created_at = CustomDateTimeField()
