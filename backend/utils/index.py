from marshmallow import Schema, fields

# Define a custom DateTime field that serializes and deserializes datetime objects
class CustomDateTimeField(fields.DateTime):
    def _serialize(self, value, attr, obj, **kwargs):
        if value is None:
            return None
        return super()._serialize(value, attr, obj, **kwargs)
