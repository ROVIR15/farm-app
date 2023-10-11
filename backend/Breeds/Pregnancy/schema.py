from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
# from BlockArea.schema import BlockAreaSchema

class PregnancySchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    breeding_id = fields.Int(required=True)
    is_active = fields.Boolean(required=True)
    created_at = CustomDateTimeField()
