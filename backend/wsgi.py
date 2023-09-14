#!/usr/bin/python3
import sys
import logging
logging.basicConfig(stream=sys.stderr)
sys.path.insert(0, '/var/www/farm-app')

from backend.app import app
# from backend.db_connection import db

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8000, debug=True)
