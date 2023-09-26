from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
# from BlockArea.schema import BlockAreaSchema

class BreedingSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    farm_profile_id = fields.Int(required=True)
    livestock_male_id = fields.Int(required=True)
    livestock_female_id = fields.Int(required=True)
    sled_id = fields.Str(required=True)
    is_active = fields.Boolean(required=True)
    created_at = CustomDateTimeField()
