from marshmallow import Schema, fields
from Record.WeightRecord.schema import WeightRecordSchema
from Record.BCSRecord.schema import BCSRecordSchema
from Record.HealthRecord.schema import HealthRecordSchema

class LivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Int(required=True)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)
    weight_records = fields.Nested(WeightRecordSchema, allow_none=True, many=True)
    bcs_records = fields.Nested(BCSRecordSchema, allow_none=True, many=True)
    health_records = fields.Nested(HealthRecordSchema, allow_none=True, many=True)