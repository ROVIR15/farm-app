from flask import Blueprint, request, jsonify
from PIL import Image
import os
import imghdr
from datetime import datetime

views_upload_bp = Blueprint('views_upload', __name__)


def resize_image(file, scale_factor):
    # Open the image using Pillow (PIL)
    img = Image.open(file)

    # Calculate new dimensions based on the scale factor
    width, height = img.size
    new_width = int(width * scale_factor)
    new_height = int(height * scale_factor)

    # Resize the image
    resized_img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)

    return resized_img

def resize_image(file, scale_factor):
    image = Image.open(file)
    width, height = image.size
    new_width = int(width * scale_factor)
    new_height = int(height * scale_factor)
    resized_image = image.resize((new_width, new_height))
    return resized_image

@views_upload_bp.route('/upload-livestock', methods=['POST'])
def upload_file():
    UPLOAD_FOLDER = 'static/uploads/livestocks'

    try:
        if 'file' not in request.files:
            return jsonify({'message': 'No file part'}), 400

        file = request.files['file']

        if file.filename == '':
            return jsonify({'message': 'No selected file'}), 400

        # Check the file size
        max_file_size = 2 * 1024 * 1024  # 2MB (adjust as needed)
        file.seek(0, os.SEEK_END)
        file_size = file.tell()
        file.seek(0)  # Reset file pointer after getting file size

        if file_size > max_file_size:
            return jsonify({'message': 'File size exceeds the maximum allowed size'}), 400

        # Check the file type using imghdr
        file_type = imghdr.what(None, h=file.read(32))
        file.seek(0)  # Reset file pointer after checking file type

        if file_type not in ['jpeg', 'jpg', 'png', 'gif']:
            return jsonify({'message': 'Unsupported file type. Only JPEG, PNG, and GIF images are allowed'}), 400

        # Generate a unique identifier based on the current date and time
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

         # Replace spaces with underscores in the original filename
        original_filename = file.filename.replace(" ", "_")

        # Construct the new filename with the unique identifier
        filename, file_extension = os.path.splitext(original_filename)
        new_filename = f"{filename}_{timestamp}{file_extension}"

        # Resize the uploaded image to 70% of its original size
        resized_img = resize_image(file, scale_factor=0.7)

        # Save the resized image with the new filename
        # file_path = os.path.join(UPLOAD_FOLDER, new_filename)
        file_path = os.path.join(os.path.abspath(UPLOAD_FOLDER), new_filename)
        resized_img.save(file_path)

        return jsonify({'url_link': f'{UPLOAD_FOLDER}/{new_filename}','message': 'File uploaded and resized to 70% successfully'}), 200

    except Exception as e:
      error_message = str(e)
      response = {
        'status': 'error',
        'message': f'Sorry! Failed to upload error: {error_message}'
      }

      return jsonify(response), 500

@views_upload_bp.route('/upload-sled', methods=['POST'])
def upload_file_sled():
    UPLOAD_FOLDER = 'static/uploads/sleds'

    try:
        if 'file' not in request.files:
            return jsonify({'message': 'No file part'}), 400

        file = request.files['file']

        if file.filename == '':
            return jsonify({'message': 'No selected file'}), 400

        # Check the file size
        max_file_size = 2 * 1024 * 1024  # 2MB (adjust as needed)
        file.seek(0, os.SEEK_END)
        file_size = file.tell()
        file.seek(0)  # Reset file pointer after getting file size

        if file_size > max_file_size:
            return jsonify({'message': 'File size exceeds the maximum allowed size'}), 400

        # Check the file type using imghdr
        file_type = imghdr.what(None, h=file.read(32))
        file.seek(0)  # Reset file pointer after checking file type

        if file_type not in ['jpeg', 'jpg', 'png', 'gif']:
            return jsonify({'message': 'Unsupported file type. Only JPEG, PNG, and GIF images are allowed'}), 400

        # Generate a unique identifier based on the current date and time
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

         # Replace spaces with underscores in the original filename
        original_filename = file.filename.replace(" ", "_")

        # Construct the new filename with the unique identifier
        filename, file_extension = os.path.splitext(original_filename)
        new_filename = f"{filename}_{timestamp}{file_extension}"

        # Resize the uploaded image to 70% of its original size
        resized_img = resize_image(file, scale_factor=0.7)

        # Save the resized image with the new filename
        file_path = os.path.join(UPLOAD_FOLDER, new_filename)
        resized_img.save(file_path)

        return jsonify({'url_link': file_path,'message': 'File uploaded and resized to 70% successfully'}), 200

    except Exception as e:
      error_message = str(e)
      response = {
        'status': 'error',
        'message': f'Sorry! Failed to upload error: {error_message}'
      }

      return jsonify(response), 500


@views_upload_bp.route('/upload-block-area', methods=['POST'])
def upload_file_block_area():
    UPLOAD_FOLDER = 'static/uploads/block-area'

    try:
        if 'file' not in request.files:
            return jsonify({'message': 'No file part'}), 400

        file = request.files['file']

        if file.filename == '':
            return jsonify({'message': 'No selected file'}), 400

        # Check the file size
        max_file_size = 2 * 1024 * 1024  # 2MB (adjust as needed)
        file.seek(0, os.SEEK_END)
        file_size = file.tell()
        file.seek(0)  # Reset file pointer after getting file size

        if file_size > max_file_size:
            return jsonify({'message': 'File size exceeds the maximum allowed size'}), 400

        # Check the file type using imghdr
        file_type = imghdr.what(None, h=file.read(32))
        file.seek(0)  # Reset file pointer after checking file type

        if file_type not in ['jpeg', 'jpg', 'png', 'gif']:
            return jsonify({'message': 'Unsupported file type. Only JPEG, PNG, and GIF images are allowed'}), 400

        # Generate a unique identifier based on the current date and time
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

         # Replace spaces with underscores in the original filename
        original_filename = file.filename.replace(" ", "_")

        # Construct the new filename with the unique identifier
        filename, file_extension = os.path.splitext(original_filename)
        new_filename = f"{filename}_{timestamp}{file_extension}"

        # Resize the uploaded image to 70% of its original size
        resized_img = resize_image(file, scale_factor=0.7)

        # Save the resized image with the new filename
        file_path = os.path.join(UPLOAD_FOLDER, new_filename)
        resized_img.save(file_path)

        return jsonify({'url_link': file_path,'message': 'File uploaded and resized to 70% successfully'}), 200

    except Exception as e:
      error_message = str(e)
      response = {
        'status': 'error',
        'message': f'Sorry! Failed to upload error: {error_message}'
      }

      return jsonify(response), 500


@views_upload_bp.route('/upload-farm-profile', methods=['POST'])
def upload_file_farm_profile():
    UPLOAD_FOLDER = 'static/uploads/farm-profile'

    try:
        if 'file' not in request.files:
            return jsonify({'message': 'No file part'}), 400

        file = request.files['file']

        if file.filename == '':
            return jsonify({'message': 'No selected file'}), 400

        # Check the file size
        max_file_size = 2 * 1024 * 1024  # 2MB (adjust as needed)
        file.seek(0, os.SEEK_END)
        file_size = file.tell()
        file.seek(0)  # Reset file pointer after getting file size

        if file_size > max_file_size:
            return jsonify({'message': 'File size exceeds the maximum allowed size'}), 400

        # Check the file type using imghdr
        file_type = imghdr.what(None, h=file.read(32))
        file.seek(0)  # Reset file pointer after checking file type

        if file_type not in ['jpeg', 'jpg', 'png', 'gif']:
            return jsonify({'message': 'Unsupported file type. Only JPEG, PNG, and GIF images are allowed'}), 400

        # Generate a unique identifier based on the current date and time
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

         # Replace spaces with underscores in the original filename
        original_filename = file.filename.replace(" ", "_")

        # Construct the new filename with the unique identifier
        filename, file_extension = os.path.splitext(original_filename)
        new_filename = f"{filename}_{timestamp}{file_extension}"

        # Resize the uploaded image to 70% of its original size
        resized_img = resize_image(file, scale_factor=0.7)

        # Save the resized image with the new filename
        file_path = os.path.join(UPLOAD_FOLDER, new_filename)
        resized_img.save(file_path)

        return jsonify({'url_link': file_path,'message': 'File uploaded and resized to 70% successfully'}), 200

    except Exception as e:
      error_message = str(e)
      response = {
        'status': 'error',
        'message': f'Sorry! Failed to upload error: {error_message}'
      }

      return jsonify(response), 500