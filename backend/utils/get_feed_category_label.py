def get_feed_category_label(feed_category):
    feed_category_mapping = {1: 'Hijauan', 2: 'Konsentrat', 3: 'Vitamin', 4: 'Tambahan'}
    return feed_category_mapping.get(feed_category, None)
