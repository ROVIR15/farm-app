from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class FeedingRecordSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    feed_category = fields.Int(required=True)
    sku_id = fields.Int(required=True)
    block_area_id = fields.Int(required=True)
    date = fields.Date(required=True)
    score = fields.Int(required=True)
    left = fields.Int(required=True)
    remarks = fields.Str(required=True)
    created_at = CustomDateTimeField()