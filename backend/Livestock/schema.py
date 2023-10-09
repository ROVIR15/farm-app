from marshmallow import Schema, fields
from Record.WeightRecord.schema import WeightRecordSchema
from Record.BCSRecord.schema import BCSRecordSchema
from Record.HealthRecord.schema import HealthRecordSchema
from Record.FeedingRecord.schema import FeedListSchema

class LivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Str(required=True)
    bangsa = fields.Str(required=True)
    birth_date = fields.Str(required=False)
    info = fields.Str(required=False)
    description = fields.Str(required=True)
    weight_records = fields.Nested(WeightRecordSchema, allow_none=True, many=True)
    bcs_records = fields.Nested(BCSRecordSchema, allow_none=True, many=True)
    health_records = fields.Nested(HealthRecordSchema, allow_none=True, many=True)
    feeding_records = fields.Nested(FeedListSchema, many=True)

class LivestockSchema_new(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Str(required=True)
    birth_date = fields.Str(required=False)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)
    info = fields.Str(required=False)
    created_at = fields.Str(required=True)