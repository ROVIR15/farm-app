from marshmallow import Schema, fields

class LivestockSchema_options(Schema):
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
