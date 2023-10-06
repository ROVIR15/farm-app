from marshmallow import Schema, fields
from utils.remove_duplcate import remove_duplicates
from utils.get_feed_category_label import get_feed_category_label

# Define a custom DateTime field that serializes and deserializes datetime objects


class CustomDateTimeField(fields.DateTime):
    def _serialize(self, value, attr, obj, **kwargs):
        if value is None:
            return None
        return super()._serialize(value, attr, obj, **kwargs)
