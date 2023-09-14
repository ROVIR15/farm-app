from db_connection import db


class Budget (db.Model):
    __tablename__ = "budget"

    id = db.Column(db.Integer(), primary_key=True)
    month = db.Column(db.Date(), nullable=False)

    # Define the 'budget item' relationship
    items = db.relationship(
        'BudgetItem', back_populates='budget_item', lazy=True)

    def __repr__(self):
        return f'<Budget {self.name}>'
