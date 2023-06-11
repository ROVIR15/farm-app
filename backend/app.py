from flask import Flask, jsonify
from db import db
from BlockArea.models import BlockArea
from BlockAreaSledLivestock.models import BlockAreaSledLivestock
from Budget.models import Budget
from BudgetItem.models import BudgetItem
from ConsumptionRecord.models import ConsumptionRecord
from Expenditure.models import Expenditure
from Feature.models import Feature
from Product.models import Product
from ProductFeature.models import ProducFeature
from ProductHasCategory.models import ProductHasCategory
from Sled.models import Sled
from flask_sqlalchemy import SQLAlchemy

from sqlalchemy.orm import joinedload

from Livestock.views import views_bp

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost:3306/farm'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db.init_app(app)

app.register_blueprint(views_bp)

@app.route('/hello')
def hello():
    return 'Hello'


@app.route('/block-area')
def block_area():
    query = BlockArea.query.all()
    return jsonify(query)


@app.route('/sled')
def sled():
    query = Sled.query.all()
    return jsonify(query)


@app.route('/budget')
def budget():
    query = Budget.query.all()
    return jsonify(query)


@app.route('/budget-item')
def budget_item():
    query = BudgetItem.query.all()
    return jsonify(query)


@app.route('/consumption')
def consumption():
    query = ConsumptionRecord.query.all()
    return jsonify(query)


@app.route('/expenditure')
def expenditure():
    query = Expenditure.query.all()
    return jsonify(query)


@app.route('/feature')
def feature():
    query = Feature.query.all()
    return jsonify(query)

# @app.route('/product')
# def product():
#   query = Product.query.all()
#   return jsonify(query)

@app.route('/product-feature')
def product():
    query = Product.query.all()
    return jsonify(query)
