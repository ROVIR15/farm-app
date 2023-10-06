def remove_duplicates(arr, key):
    seen = set()
    result = []
    for item in arr:
        if item[key] not in seen:
            seen.add(item[key])
            result.append(item)
    return result
