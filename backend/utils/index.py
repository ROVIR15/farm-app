from marshmallow import Schema, fields
from utils.remove_duplcate import remove_duplicates
from utils.get_feed_category_label import get_feed_category_label
from datetime import datetime

# Define a custom DateTime field that serializes and deserializes datetime objects
class CustomDateTimeField(fields.DateTime):
    def _serialize(self, value, attr, obj, **kwargs):
        if value is None:
            return None
        return super()._serialize(value, attr, obj, **kwargs)


def validate_date_format(date_string):
    date_formats_to_check = ['%d-%m-%Y', '%Y-%m-%d']

    for date_format in date_formats_to_check:
        try:
            datetime.strptime(date_string, date_format)
            return True
        except ValueError:
            pass

    return False

def validate_and_transform_date(date_string):
    try_formats = ['%d-%m-%Y', '%Y-%m-%d', '%m-%d-%Y']

    for date_format in try_formats:
        try:
            # Attempt to parse the date using the current format
            parsed_date = datetime.strptime(date_string, date_format)
            # Return the date in the desired format
            return parsed_date.strftime('%Y-%m-%d')
        except ValueError:
            pass

    # If none of the formats match, return None or raise an exception
    return None