from db import db
from sqlalchemy import Numeric
from datetime import datetime


class User (db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer(), primary_key=True)
    username = db.Column(db.String(50), nullable=True)
    password = db.Column(db.String(120), nullable=True)
    email = db.Column(db.String(100), nullable=True)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    # Implement these methods and attributes from UserMixin
    @property
    def is_active(self):
        return True  # You can implement custom logic to determine if a user is active or not

    @property
    def is_authenticated(self):
        return True  # Return True if the user is authenticated

    @property
    def is_anonymous(self):
        return False  # Return False since this user is not anonymous

    def get_id(self):
        return str(self.id)  # Return a string of the user's ID

    def __repr__(self):
        return f'<User {self.name}>'
