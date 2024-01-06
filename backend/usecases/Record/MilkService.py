import json
from db_connection import db
from interfaces.repositories.Record.Milk import MilkRepositiories
from entities.Record.Milk import MilkRecordEntities
from infrastructure.Record.Milk import MilkRecordEncoder


class MilkService:

    # get milk production record filtered by livestock_id
    def getMilkProductionByLivestock(livestock_id):
        try:
            query = MilkRepositiories.getByLivestockId(
                livestock_id=livestock_id)

            if not query:
                return {
                    'status': 404,
                    'message': f'Failed to get milk production record by livestock_id = {livestock_id}',
                }
            
            # Fetch data from the query result
            # data = [record.to_dict() for record in query]
            # data = [MilkRecordEntities(**record_data.to_dict()) for record_data in query]
            data = []
            prev_score = None

            for record in query:
                instance = MilkRecordEntities(**record.to_dict())
                result = instance.to_dict()

                if prev_score is not None:
                    percentage = instance.growth_calculation(prev_score)
                    result['growth'] = f'{percentage:.2f}%'
                    result['prev_score'] = 0
                else:
                    result['growth'] = f'{0:.2f}%'  # Format the percentage with two decimal places
                    result['prev_score'] = 0  # Format the percentage with two decimal places
                
                prev_score = result['score']
                data.append(result)
                
            return {
                'status': 200,
                'data': data,
            }

        except Exception as e:
            error_message = str(e)

            response = {
                'status': 500,
                'message': f'Sorry error to get Milk Record, due to {error_message}'
            }

            return response

    def createMilkRecord(
        livestock_id,
        date,
        score,
        remarks
    ):
        try:
            payload = MilkRecordEntities(
                None, livestock_id=livestock_id, date=date, score=score, remarks=remarks)

            query = MilkRepositiories.create(payload)
            db.session.add(query)
            db.session.commit()
            
            if query:
                print(f'Milk Production has recorded: {query.id}')

                response = {
                    'status': 200,
                    'message': 'Milk Production has recorded'
                }

                return response

        except Exception as e:

            error_message = str(e)

            response = {
                'status': 500,
                'message': f'Sorry error to store Milk Record1, due to {error_message}'
            }

            return response

    def updateMilkRecord(milk_record_id, new_date, new_score):
        try:
            query = MilkRepositiories.getById(milk_record_id)

            if not query:
                return {
                    'status': 404,
                    'message': f'Failed to get milk production record by livestock_id = {livestock_id}',
                }

            entity = MilkRecordEntities(
                query.id, query.livestock_id, query.date, query.score, query.remarks)
            entity.setNewDate(new_date)
            entity.setNewScore(new_score)

            MilkRepositiories.update(entity)

        except Exception as e:
            error_message = str(e)

            response = {
                'status': 500,
                'message': f'Sorry error to update Milk Record, due to {error_message}'
            }

            return response

        return {
            'status': 200,
            'message': 'Data has been updated!'
        }

    def deleteMilkRecord(milk_record_id):
        try:
            query = MilkRepositiories.delete(milk_record_id)

            if query:
                response = {
                    'status': 200,
                    'message': 'Milk record succesfully deleted!'
                }

                return response
            else:
                return {
                    'status': 404,
                    'message': 'Failed to delete milk production record'
                }

        except Exception as e:
            error_message = str(e)

            response = {
                'status': 500,
                'message': error_message
            }

            return response
