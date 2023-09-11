from db import db
from flask import Blueprint, request, jsonify
from User.models import User
from flask_login import login_user, login_user, login_required, logout_user, current_user
from werkzeug.security import generate_password_hash, check_password_hash

views_auth_bp = Blueprint('views_auth', __name__)

# Registration route
@views_auth_bp.route('/register', methods=['GET', 'POST'])
def register():
    data = request.json
    username = data.get('username')
    password = data.get('password')

    # Check if the username is already taken
    existing_user = User.query.filter_by(username=username).first()
    if existing_user:
        response = {
            'status': 'success',
            'message': 'Username already exists. Please choose another.'
        }
        return jsonify(response)

    # Hash the password before storing it in the database
    hashed_password = generate_password_hash(password, method='sha256')

    new_user = User(username=username, password=hashed_password)
    db.session.add(new_user)
    db.session.commit()

    response = {
        'status': 'success',
        'message': 'Account created successfully. You can now log in.'
    }
    return jsonify(response), 201


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