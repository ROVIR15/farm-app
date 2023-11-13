from marshmallow import Schema, fields
from Record.HeightRecord.schema import HeightRecordSchema
from Record.WeightRecord.schema import WeightRecordSchema
from Record.BCSRecord.schema import BCSRecordSchema
from Record.HealthRecord.schema import HealthRecordSchema
from Record.FeedingRecord.schema import FeedListSchema
from Descendant.schema import DescendantSchema


class LivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Str(required=True)
    gender_name = fields.Str(required=False)
    bangsa = fields.Str(required=True)
    birth_date = fields.Str(required=False)
    info = fields.Str(required=False)
    description = fields.Str(required=True)
    sled_id = fields.Int(required=False)
    block_area_id = fields.Int(required=False)
    height_records = fields.Nested(
        HeightRecordSchema, allow_none=True, many=True)
    weight_records = fields.Nested(
        WeightRecordSchema, allow_none=True, many=True)
    bcs_records = fields.Nested(BCSRecordSchema, allow_none=True, many=True)
    health_records = fields.Nested(
        HealthRecordSchema, allow_none=True, many=True)
    feeding_records = fields.Nested(FeedListSchema, many=True)
    descendant = fields.Nested(DescendantSchema, required=False)


class LivestockSchema_new(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Str(required=True)
    gender_name = fields.Str(required=False)
    sled_id = fields.Int(required=False)
    block_area_id = fields.Int(required=False)
    label = fields.Str(required=False)
    birth_date = fields.Str(required=False)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)
    info = fields.Str(required=False)
    created_at = fields.Str(required=True)
    descendant = fields.Nested(DescendantSchema, required=False)
