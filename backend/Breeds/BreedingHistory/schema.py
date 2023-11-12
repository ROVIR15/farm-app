from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class BreedingHistorySchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    breeding_id = fields.Int(required=True)
    remarks = fields.Str(required=True)
    date = fields.Str(required=True)
    created_at = CustomDateTimeField()
