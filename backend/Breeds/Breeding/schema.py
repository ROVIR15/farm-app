from marshmallow import Schema, fields
from utils.index import CustomDateTimeField
from Livestock.schema import LivestockSchema_new
from Livestock.schema import LivestockSchema_new
from Breeds.Pregnancy.schema import PregnancySchema
from Breeds.Lambing.schema import LambingSchema
from Breeds.BreedingHistory.schema import BreedingHistorySchema
from Breeds.BreedingStatus.schema import BreedingStatusSchema
# from BlockArea.schema import BlockAreaSchema
from Sled.schema import SledSchema

class BreedingSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    farm_profile_id = fields.Int(required=True)
    livestock_male_id = fields.Int(required=True)
    livestock_male_name = fields.Str(required=True)
    livestock_female_name = fields.Str(required=True)
    livestock_female_id = fields.Int(required=True)
    sled_id = fields.Int(required=True)
    is_active = fields.Boolean(required=True)

    livestock_male = fields.Nested(LivestockSchema_new, required=False)
    livestock_female = fields.Nested(LivestockSchema_new, required=False)

    pregnancy = fields.Nested(PregnancySchema, required=False)
    lambing = fields.Nested(LambingSchema, many=True, required=False)
    breeding_history = fields.Nested(BreedingHistorySchema, many=True, required=False)
    sled = fields.Nested(SledSchema, required=False)
    breeding_status = fields.Nested(BreedingStatusSchema, required=False)

    date = fields.Str(required=True)
    created_at = fields.Str(required=True)


