from marshmallow import Schema, fields

class DescendantSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    livestock_name = fields.Str(required=True)
    livestock_id = fields.Int(required=True)
    parent_male_id = fields.Int(required=True)
    parent_male_name = fields.Str(required=True)
    parent_female_name = fields.Str(required=True)
    parent_female_id = fields.Int(required=True)

class DescendantSchema_new(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    livestock_name = fields.Str(required=True)
    livestock_id = fields.Int(required=True)
    parent_male_id = fields.Int(required=True)
    parent_male_name = fields.Str(required=True)
    parent_female_name = fields.Str(required=True)
    parent_female_id = fields.Int(required=True)

    # livestock = fields.Nested(LivestockSchema_new, required=False)
    # parent_male = fields.Nested(LivestockSchema_new, required=False)
    # parent_female = fields.Nested(LivestockSchema_new, required=False)
