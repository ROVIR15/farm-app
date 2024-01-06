
class MilkRecordEntities:

    def __init__(self, id, livestock_id, date, score, remarks):
        self.id = id,
        self.livestock_id = livestock_id,
        self.date = date,
        self.score = score,
        self.remarks = remarks

    def create(self, cls):
        # Validation and any additional business logic can be added here
        return cls(
            id=self.id,
            livestock_id=self.livestock_id,
            date=self.date,
            score=self.score,
            remarks=self.remarks
        )

    def to_dict(self):
        return {
            'id': self.id[0] if self.id else None,
            'livestock_id': self.livestock_id[0] if self.livestock_id else None,
            'date': str(self.date[0]) if self.date else None,
            'score': self.score[0] if self.score else None,
            'remarks': str(self.remarks[0]) if self.remarks else None,
        }

    def diff_calculation(self, yesteday_score):
      return self.score[0] - yesteday_score

    def growth_calculation(self, yesteday_score):
        diff = self.score[0] - yesteday_score
        return (diff / yesteday_score) * 100

    def setNewDate(self, new_date):
        self.date = new_date

    def setNewScore(self, new_score):
        self.score = new_score

    def setNewRemarks(self, new_remarks):
        self.remarks = new_remarks

    def haha(self):
        return self.id[0]
