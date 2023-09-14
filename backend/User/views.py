from db import db
from flask import Blueprint, request, jsonify
from User.models import User
from FarmProfile.models import FarmProfile
from FarmProfile.models import FarmProfileHasUsers

from flask_login import login_user, login_user, login_required, logout_user, current_user
from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy import and_

views_auth_bp = Blueprint('views_auth', __name__)

# Registration route
@views_auth_bp.route('/register', methods=['GET', 'POST'])
def register():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    email = data.get('email')


    # Check if the username is already taken
    existing_user = User.query.filter(and_(User.username == username, User.email == email)).first()
    if existing_user:
        response = {
            'status': 'success',
            'message': 'Username or Email already exists. Please choose another.'
        }
        return jsonify(response)

    # Hash the password before storing it in the database
    hashed_password = generate_password_hash(password, method='sha256')

    try:
        data_farm_profile = data.get('farm_profile')
        new_user = User(username=username, password=hashed_password, email=email)
        db.session.add(new_user)
        db.session.commit()

        # Create new farm profile
        # Get data from request body
        name = data_farm_profile.get('name')
        address_one = data_farm_profile.get('address_one')
        address_two = data_farm_profile.get('address_two')
        city = data_farm_profile.get('city')
        province = data_farm_profile.get('province')

        query = FarmProfile(
            name=name,
            address_one=address_one,
            address_two=address_two,
            city=city,
            province=province)
        db.session.add(query)
        db.session.commit()

        query2 = FarmProfileHasUsers(farm_profile_id=query.id, user_id=new_user.id)
        db.session.add(query2)
        db.session.commit()

        response = {
            'status': 'success',
            'message': 'Account created successfully. You can now log in.'
        }
        return jsonify(response), 201

    except Exception as e:
        # Handling the exception if storing the data fails
        error_message = str(e)
        response = {
            'status': 'error',
            'message': error_message
        }
        return jsonify(response), 500


# Login route
@views_auth_bp.route('/login', methods=['GET', 'POST'])
def login():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    user = User.query.filter_by(username=username).first()

    if user and check_password_hash(user.password, password):
        login_user(user)
        return jsonify({"message": "Logged in successfully."}), 200
    else:
        return jsonify({"message": "Login failed. Check your username and password."}), 401

# Profile route (protected API)
@views_auth_bp.route('/api/profile', methods=['GET'])
@login_required
def api_profile():
    return jsonify({"username": current_user.username}), 200

# Logout route (protected API)
@views_auth_bp.route('/api/logout', methods=['POST'])
@login_required
def api_logout():
    logout_user()
    return jsonify({"message": "Logged out successfully."}), 200