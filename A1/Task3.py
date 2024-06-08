def matrix_multiply_for_loop(A, B):
    result = [[0 for _ in range(len(B[0]))] for _ in range(len(A))]

    for i in range(len(A)):
        for j in range(len(B[0])):
            for k in range(len(B)):
                result[i][j] += A[i][k] * B[k][j]

    return result


def matrix_multiply_while_loop(A, B):
    result = [[0] * len(B[0]) for _ in range(len(A))]

    i = 0
    while i < len(A):
        j = 0
        while j < len(B[0]):
            k = 0
            while k < len(B):
                result[i][j] += A[i][k] * B[k][j]
                k += 1
            j += 1
        i += 1

    return result


# Testing
A = [[1, 2, 3], [4, 5, 6]]
B = [[7, 8], [9, 10], [11, 12]]

result_for_loop = matrix_multiply_for_loop(A, B)
print("Matrix Multiplication using For Loop:")
for row in result_for_loop:
    print(row)

result_while_loop = matrix_multiply_while_loop(A, B)
print("\nMatrix Multiplication using While Loop:")
for row in result_while_loop:
    print(row)
