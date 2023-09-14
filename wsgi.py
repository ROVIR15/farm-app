#!/usr/bin/python3
import sys
import logging
logging.basicConfig(stream=sys.stderr)
sys.path.insert(0, '/var/www/farm-app')

from backend.app import app

if __name__ == "__main__":
    app.run()
