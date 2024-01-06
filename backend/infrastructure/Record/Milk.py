import json
from entities.Record.Milk import MilkRecordEntities

class MilkRecordEncoder(json.JSONEncoder):
    def default(self, obj):
        if hasattr(obj, 'to_dict'):
            return obj.to_dict()
        else:
            return super().default(obj)
