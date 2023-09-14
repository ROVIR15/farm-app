from db import db
from datetime import datetime

class FarmProfile (db.Model):
    __tablename__ = "farm_profile"

    id = db.Column(db.Integer(), primary_key=True)
    name = db.Column(db.String(50), nullable=True)
    address_one = db.Column(db.Text, nullable=True)
    address_two = db.Column(db.String(256), nullable=True)
    city = db.Column(db.String(100), nullable=True)
    province = db.Column(db.String(100), nullable=True)
    created_at = db.Column(db.DateTime(timezone=True), default=datetime.utcnow)

    users = db.relationship('FarmProfileHasUsers', back_populates='farm_profiles')


class FarmProfileHasUsers (db.Model):
    __tablename__ = "farm_profile_has_users"

    farm_profile_id = db.Column(db.Integer(), db.ForeignKey(
        'farm_profile.id'), primary_key=True, nullable=False)
    user_id = db.Column(db.Integer(), db.ForeignKey(
        'users.id'), primary_key=True, nullable=False)

    users = db.relationship('User', back_populates='farm_profiles', foreign_keys=[user_id])
    farm_profiles = db.relationship('FarmProfile', back_populates='users', foreign_keys=[farm_profile_id])
