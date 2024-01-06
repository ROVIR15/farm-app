from db_connection import db
from sqlalchemy import desc, asc
from frameworks.sqlalchemy.Record.Milk.models import MilkRecord

class MilkRepositiories:

  def create(payload):
    query = MilkRecord(
      livestock_id = payload.livestock_id,
      date = payload.date,
      score = payload.score,
      remarks = payload.remarks
    )
    return query

  def getByLivestockId(livestock_id):
    return MilkRecord.query.filter_by(livestock_id=livestock_id).order_by(asc(MilkRecord.date))

  def getById(milk_record_id):
    return MilkRecord.query.get(milk_record_id)

  def getAll():
    query = MilkRecord.query.all()
    return query

  def update(payload):
    record = MilkRecord.query.get(payload.id)

    if (record):
      record.date = payload.date
      record.score = payload.score
      record.remarks = payload.remarks
      return True
    else: 
      return False
    
  def delete(id):
    record = MilkRecord.query.get(id)

    if(record):
      db.session.delete(record)
      db.session.commit()

      return True
    else:
      return False

