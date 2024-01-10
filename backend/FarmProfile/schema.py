from marshmallow import Schema, fields
from utils.index import CustomDateTimeField

class FarmProfileSchema(Schema):
    id = fields.Int(required=True)
    name = fields.Str(required=True)
    address_one = fields.Str(required=True)
    address_two = fields.Str(required=True)
    city = fields.Str(required=True)
    province = fields.Str(required=True)
    imageurl = fields.Str(required=False)
    created_at = CustomDateTimeField()
