from .utils import login_required
from .utils import encode_token
from .utils import decode_token
from .utils import get_token_from_request
from .utils import current_user
from .utils import logout_user
from .utils import current_farm_profile

class AuthManager:
    def __init__(self, app=None, secret_key=None):
        if app is not None:
            self.init_app(app)
        self.secret_key = secret_key
        self.revoked_tokens = set()

    def init_app(self, app):
        self.app = app
        self.secret_key = 'your-secret-key'  # Replace with your actual secret key

