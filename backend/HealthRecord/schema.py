from marshmallow import Schema, fields

class HealthRecordSchema(Schema):
    id = fields.Int(primary_key=True)
    livestock_id = fields.Int(required=True)
    disease_type = fields.Int(required=True)
    treatment_methods = fields.Str(required=True)
    remarks = fields.text(required=False)
    created_at = fields.DateTime()
    