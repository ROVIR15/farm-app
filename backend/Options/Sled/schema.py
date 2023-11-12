from marshmallow import Schema, fields

class SledSchema_options(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    block_area_id = fields.Int(required=True)
    name = fields.Str(required=True)
    info = fields.Str(required=True)
    block_area_name = fields.Str(required=True)
