from flask import Blueprint, request, jsonify
from PIL import Image
import os

UPLOAD_FOLDER = 'backend/static/uploads'
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


@views_upload_bp.route('/upload-livestock', methods=['POST'])
def upload_file():

    try:
      if 'file' not in request.files:
          return jsonify({'message': 'No file part'}), 400

      file = request.files['file']

      if file.filename == '':
          return jsonify({'message': 'No selected file'}), 400

      # Check the file size
      max_file_size = 2 * 1024 * 1024  # 10MB (adjust as needed)
      file_size = os.path.getsize(file)

      if file_size > max_file_size:
          return jsonify({'message': 'File size exceeds the maximum allowed size'}), 400

      # Check the file type using imghdr
      file_type = imghdr.what(file)
      if file_type not in ['jpeg', 'jpg', 'png', 'gif']:
          return jsonify({'message': 'Unsupported file type. Only JPEG, PNG, and GIF images are allowed'}), 400

      if file:
        # Resize the uploaded image to 70% of its original size
        resized_img = resize_image(file, scale_factor=0.7)

        # Save the resized image
        filename = os.path.join(UPLOAD_FOLDER, file.filename)
        resized_img.save(filename)

        return jsonify({'message': 'File uploaded and resized to 70% successfully'}), 200

    except Exception as e:
      error_message = str(e)
      response = {
        'status': 'error',
        'message': f'Sorry! Failed to upload error: {error_message}'
      }

      return jsonify(response), 500