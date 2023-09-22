from flask import Flask
from dotenv import load_dotenv
import os

from auth import AuthManager

from db_connection import db
from Livestock.views import views_bp
from BlockAreaSledLivestock.views import views_livestock_details_bp
from Sled.views import views_sled_bp
from BlockArea.views import views_block_area_bp
from Category.views import views_category_bp
from Product.views import views_product_bp
from Feature.views import views_feature_bp

from Record.BCSRecord.views import views_bcs_record_bp
from Record.WeightRecord.views import views_weight_record_bp
from Record.HealthRecord.views import views_health_record_bp


from FarmProfile.views import views_farm_profile_bp
# from Finance.Budget.views import views_budget_bp
# from Finance.BudgetItem.views import views_budget_item_bp
# from Finance.BudgetCategory.views import views_budget_category_bp

# Load enviroment variable
load_dotenv()

# ----------------------------------------- #
# Auth
# ----------------------------------------- #
from flask_login import LoginManager

# Model
from User.models import User as UserModel
from User.views import views_auth_bp

app = Flask(__name__)
app.debug = os.environ.get('DEBUG') == 'True'  # Turn off debug mode
app.secret_key = os.environ.get('SECRET_KEY')  # Replace with a strong secret key
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)

app.login_manager = AuthManager(app, app.config['SECRET_KEY'])

# @login_manager.user_loader
# def load_user(user_id):
#     return UserModel.query.get(int(user_id))

# Auth
# 0. Authentication
app.register_blueprint(views_auth_bp)

# 1. Block Area register and management
app.register_blueprint(views_block_area_bp, url_prefix='/api')

# 2. Sled register 
app.register_blueprint(views_sled_bp, url_prefix='/api')
# 
# 3. Category for product register and management
# app.register_blueprint(views_category_bp)

# 4. Product register
app.register_blueprint(views_product_bp, url_prefix='/api')

# 5. Feature register
app.register_blueprint(views_feature_bp, url_prefix='/api')

# 6. Livestock
app.register_blueprint(views_bp, url_prefix='/api')

# Record
# 7. BCS Record 
app.register_blueprint(views_bcs_record_bp, url_prefix='/api')
# 8. Weight Record
app.register_blueprint(views_weight_record_bp, url_prefix='/api')
# 9. Health Record
app.register_blueprint(views_health_record_bp, url_prefix='/api')

# Finance
# 10. Budget
# app.register_blueprint(views_budget_bp)
# 11. Budget Item
# app.register_blueprint(views_budget_item_bp)
# 12. Budget Category
# app.register_blueprint(views_budget_category_bp)

# Farm Profile
# 13. Farm Profile API
app.register_blueprint(views_farm_profile_bp, url_prefix='/api')


# app.register_blueprint(views_bp)
# app.register_blueprint(views_sled_bp)
# app.register_blueprint(views_livestock_details_bp)

@app.route("/")
def hello():
    return "<h1 style='color:blue'>Hello There!</h1>"

# Add the following block at the end of the file
# if __name__ == '__main__':
#    app.run(host='0.0.0.0', port=8000, debug=True)