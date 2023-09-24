import jwt
import datetime

from flask import request
from flask import jsonify
from flask import current_app

from functools import wraps


def encode_token(user_id, farm_profile_id):
    secret_key = current_app.secret_key
    current_datetime = datetime.datetime.now()
    formatted_datetime = current_datetime.strftime('%Y-%m-%d %H:%M:%S')

    payload = {'user_id': user_id, 'farm_profile_id': farm_profile_id, 'created_at': formatted_datetime}
    return jwt.encode(payload, secret_key, algorithm='HS256')


def decode_token(token):
    try:
        secret_key = current_app.secret_key
        payload = jwt.decode(token, secret_key, algorithms=['HS256'])
        return payload
    except jwt.ExpiredSignatureError:
        return None  # Token has expired
    except jwt.InvalidTokenError:
        return None  # Token is invalid


def get_token_from_request():
    auth_header = request.headers.get('Authorization')
    if auth_header:
        try:
            token_type, token = auth_header.split()
            if token_type.lower() == 'bearer':
                return token
        except ValueError:
            pass  # Invalid header format
    return None


def current_user():
    token = get_token_from_request()
    payload = decode_token(token)
    return payload['user_id']

def current_farm_profile():
    token = get_token_from_request()
    payload = decode_token(token)
    return payload['farm_profile_id']

def login_required(func):
    @wraps(func)
    def decorated_function(*args, **kwargs):
        token = get_token_from_request()
        if token and not is_token_revoked(token):
            payload = decode_token(token)
            if payload and 'user_id' in payload:
                return func(*args,**kwargs)
        return jsonify(message='Authentication required'), 401
    return decorated_function


def revoke_token(token):
    # Implement token revocation logic here
    # You can add the token to a blacklist or remove it from an active tokens list
    # For simplicity, we'll assume the token is revoked by removing it from the list
    current_app.login_manager.revoked_tokens.add(token)


def is_token_revoked(token):
    # Check if a token is revoked
    return token in current_app.login_manager.revoked_tokens


def logout_user():
    # Get the token from the request
    token = get_token_from_request()

    if token:
        # Check if the token is already revoked
        if not is_token_revoked(token):
            # Revoke the token
            revoke_token(token)
        else:
            return jsonify(message='Token is already revoked'), 400
    else:
        return jsonify(message='No token found in request'), 400
