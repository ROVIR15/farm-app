from flask import Flask, send_from_directory
from dotenv import load_dotenv
import os
import logging

from auth import AuthManager

from db_connection import db

from Upload.views import views_upload_bp

# v1 - version 1 without 'move to sled'
from Livestock.views import views_bp 
# v1.1 
from Livestock.v1b1.views import views_bp as views_bp_new

from BlockAreaSledLivestock.views import views_livestock_details_bp

# v1
from Sled.views import views_sled_bp
# v1.1 - feature move to block area is included
from Sled.v1b1.views import views_sled_bp as views_sled_bp_new

from BlockArea.views import views_block_area_bp
from Category.views import views_category_bp
from Product.views import views_product_bp
from Feature.views import views_feature_bp

from Record.BCSRecord.views import views_bcs_record_bp
from Record.HeightRecord.views import views_height_record_bp
from Record.WeightRecord.views import views_weight_record_bp
from Record.HealthRecord.views import views_health_record_bp
from Record.FeedingRecord.views import views_consumption_bp

from Breeds.Breeding.views import views_breeding_bp
from Breeds.Breeding.v1b1.views import views_breeding_bp as views_breeding_bp_new

from FarmProfile.views import views_farm_profile_bp
from Finance.Budget.views import views_budget_bp
from Finance.BudgetItem.views import views_budget_item_bp
from Finance.Expenditure.views import views_expenditure_bp
from Finance.Income.views import views_income_bp

from Finance.BudgetCategory.views import views_budget_category_bp

from Dashboard.views import views_dashboard_bp

# Set the locale to Bahasa Indonesia
from babel import Locale
locale = Locale.parse('id_ID')

# from Finance.BudgetCategory.views import views_budget_category_bp

#  OPITOSN 
from Options import views_opts_livestock, views_opt_sled_bp

# Load enviroment variable
load_dotenv()

# ----------------------------------------- #
# Auth
# ----------------------------------------- #
# from flask_login import LoginManager

# Model
from User.views import views_auth_bp

app = Flask(__name__)
app.debug = os.environ.get('DEBUG') == 'True'  # Turn off debug mode
app.secret_key = os.environ.get('SECRET_KEY')  # Replace with a strong secret key
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)

# Configure logging
app.logger.setLevel(logging.INFO)  # Set the logging level to INFO or any desired level
handler = logging.StreamHandler()
handler.setFormatter(logging.Formatter('%(asctime)s [%(levelname)s] - %(message)s'))
app.logger.addHandler(handler)

@app.route('/static/<filename>', methods=['GET'])
def serve_file(filename):
    return send_from_directory('static/uploads', filename)

app.login_manager = AuthManager(app, app.config['SECRET_KEY'])

# @login_manager.user_loader
# def load_user(user_id):
#     return UserModel.query.get(int(user_id))

app.register_blueprint(views_upload_bp, url_prefix='/api')

# Auth
# 0. Authentication
app.register_blueprint(views_auth_bp)

# 1. Block Area register and management
app.register_blueprint(views_block_area_bp, url_prefix='/api')

# 2. Sled register 
app.register_blueprint(views_sled_bp, url_prefix='/api')
# 
# 3. Category for product register and management
app.register_blueprint(views_category_bp, url_prefix='/api')

# 4. Product register
app.register_blueprint(views_product_bp, url_prefix='/api')

# 5. Feature register
app.register_blueprint(views_feature_bp, url_prefix='/api')

# 6. Livestock
app.register_blueprint(views_bp, url_prefix='/api')

# 7. Store Livestock on Sled
app.register_blueprint(views_livestock_details_bp, url_prefix='/api')

# Record
# 8. BCS Record 
app.register_blueprint(views_bcs_record_bp, url_prefix='/api')
# 9. Weight Record
app.register_blueprint(views_weight_record_bp, url_prefix='/api')

# 10. Health Record
app.register_blueprint(views_health_record_bp, url_prefix='/api')
# 11. Consumption Record 
app.register_blueprint(views_consumption_bp, url_prefix='/api')
# 12. Feeding Record
app.register_blueprint(views_breeding_bp, url_prefix='/api')

# Finance
# 10. Budget
app.register_blueprint(views_budget_bp, url_prefix='/api')
# 11. Budget Item
app.register_blueprint(views_budget_item_bp, url_prefix='/api')
# 12. Budget Category
# app.register_blueprint(views_budget_category_bp)
# 13. Expenditure
app.register_blueprint(views_expenditure_bp, url_prefix='/api')
# 14. Income
app.register_blueprint(views_income_bp, url_prefix='/api')
# 15. Budget categories, sub categories and income categories
app.register_blueprint(views_budget_category_bp, url_prefix='/api')

# Farm Profile
# 13. Farm Profile API
app.register_blueprint(views_farm_profile_bp, url_prefix='/api')

# 14. Farm Profile
app.register_blueprint(views_dashboard_bp, url_prefix='/api')

# V1.1 - 15. Height Record
app.register_blueprint(views_height_record_bp, url_prefix='/api/v1.1')

# V1.1 - 16. Options Livestock
app.register_blueprint(views_opts_livestock, url_prefix='/api/v1.1/options')

# V1.1 - 17. Options Sleds
app.register_blueprint(views_opt_sled_bp, url_prefix='/api/v1.1/options')

# V1.1 - 18. Livestock new version with move to sled feature
app.register_blueprint(views_bp_new, url_prefix='/api/v1.1')

# V1.1 - 19. Sled new version with move to block area feature
app.register_blueprint(views_sled_bp_new, url_prefix='/api/v1.1')

# V1.1 - 20. Breeding new version with move to block area feature
app.register_blueprint(views_breeding_bp_new, url_prefix='/api/v1.1')

# app.register_blueprint(views_bp)
# app.register_blueprint(views_sled_bp)
# app.register_blueprint(views_livestock_details_bp)

@app.route("/")
def hello():
    return "<h1 style='color:blue'>Hello There!</h1>"

# Add the following block at the end of the file
# if __name__ == '__main__':
#    app.run(host='0.0.0.0', port=8000, debug=True)