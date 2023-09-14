from marshmallow import Schema, fields

class SledSchema(Schema):
    block_area_id = fields.Int(allow_none=True)
    sled_id = fields.Int(allow_none=True)


class BlockAreaSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    description = fields.Str(required=False)


class LivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    name = fields.Str(required=True)
    gender = fields.Int(required=True)
    bangsa = fields.Str(required=True)
    description = fields.Str(required=True)


class BlockAreaSledLivestockSchema(Schema):
    id = fields.Int(primary_key=True, dump_only=True)
    sled = fields.Nested(SledSchema, allow_none=True)
    livestock = fields.Nested(LivestockSchema, allow_none=True)
    block_area = fields.Nested(BlockAreaSchema, allow_none=True)
