ARRAY_SIZE = 7  # Constant for the size of the fixed-size array


# Check if elements in the array are numeric
def is_num(arr):
    for element in arr:
        if not isinstance(element, (int, float)):
            return False
    return True


# Check array size
def valid_size(arr):
    if len(arr) != ARRAY_SIZE:
        print(
            f"Error: Array size is not as expected. Expected size: {ARRAY_SIZE}, Actual size: {len(arr)}."
        )
        return False
    return True


def bubble_sort(arr):
    # Check input
    if not isinstance(arr, list):
        print("Error: Input is not an array.")
        return
    if arr is None:
        print("Error: Array is null.")
        return
    if not valid_size(arr):
        return
    if not is_num(arr):
        print("Error: Array contains non-numeric data.")
        return

    n = ARRAY_SIZE

    # Loop through array
    for i in range(n - 1):
        # Loop to compare element
        for j in range(0, n - i - 1):
            # Swap element if it is greater
            if arr[j] > arr[j + 1]:
                temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp

    print("Sorted array:", arr)


# Testing
data = [16, 2, 94, 8, 27, 11, 5]

# No errors
print("Original array:", data)
bubble_sort(data)

"""Uncommment if want to test errors
# With errors
invalid_data = [16, 2, "94", 8, 27, 11, 5]
print("Original array:", invalid_data)
bubble_sort(invalid_data)
"""
