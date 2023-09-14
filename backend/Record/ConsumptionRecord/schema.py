from marshmallow import Schema, fields

class ConsumptionReacord(Schema):
    id = fields.Int(primary_key=True)
    sku_id = fields.Int(required=True)
    block_area_id = fields.Int(required=True)
    score = fields.Float(required=True)
    remarks = fields.text(required=False)
    created_at = fields.DateTime()
    