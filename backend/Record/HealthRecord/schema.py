from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class HealthRecordSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    livestock_id = fields.Int(required=True)
    date = fields.Date(required=True)
    disease_type = fields.Int(required=True)
    treatment_methods = fields.Str(required=True)
    remarks = fields.Str(required=True)
    created_at = CustomDateTimeField()
