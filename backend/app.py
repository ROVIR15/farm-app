from flask import Flask
from db import db

from Livestock.views import views_bp
from BlockAreaSledLivestock.views import views_livestock_details_bp
from Sled.views import views_sled_bp
from BlockArea.views import views_block_area_bp
from Category.views import views_category_bp
from Product.views import views_product_bp
from Feature.views import views_feature_bp

from Record.BCSRecord.views import views_bcs_record_bp
from Record.WeightRecord.views import views_weight_record_bp

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:kucing%401@localhost:5432/'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db.init_app(app)

# 1. Block Area register and management
app.register_blueprint(views_block_area_bp)

# 2. Sled register 
app.register_blueprint(views_sled_bp)

# 3. Category for product register and management
app.register_blueprint(views_category_bp)

# 4. Product register
app.register_blueprint(views_product_bp)

# 5. Feature register
app.register_blueprint(views_feature_bp)

# 6. Block Area Sled and Livestock
app.register_blueprint(views_livestock_details_bp)

# Record
# 7. BCS Record 
app.register_blueprint(views_bcs_record_bp)
# 8. Weight Record
app.register_blueprint(views_weight_record_bp)
# 9. Health Record


# app.register_blueprint(views_bp)
# app.register_blueprint(views_sled_bp)
# app.register_blueprint(views_livestock_details_bp)


# Add the following block at the end of the file
if __name__ == '__main__':
    app.run(debug=True)
