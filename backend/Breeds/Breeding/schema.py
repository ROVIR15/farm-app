from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
from Livestock.schema import LivestockSchema_new
# from BlockArea.schema import BlockAreaSchema

class BreedingSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    farm_profile_id = fields.Int(required=True)
    livestock_male_id = fields.Int(required=True)
    livestock_male_name = fields.Str(required=True)
    livestock_female_name = fields.Str(required=True)
    livestock_female_id = fields.Int(required=True)
    sled_id = fields.Str(required=True)
    is_active = fields.Boolean(required=True)

    livestock_male = fields.Nested(LivestockSchema_new, required=False)
    livestock_female = fields.Nested(LivestockSchema_new, required=False)

    created_at = CustomDateTimeField()


