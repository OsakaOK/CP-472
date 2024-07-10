;Input
(define matrix1 '((1 2 3) (4 5 6))) ; First matrix
(define matrix2 '((7 8) (9 10) (11 12))) ; Second matrix

:Define function
(define (transpose matrix)
  (apply map list matrix))

(define (dot-product vec1 vec2)
  (apply + (map * vec1 vec2)))

(define (matrix-row matrix i)
  (list-ref matrix i))

(define (matrix-col matrix j)
  (transpose (list-ref (transpose matrix) j)))

(define (matrix-element i j matrix1 matrix2)
  (dot-product (matrix-row matrix1 i) (list-ref matrix2 j)))

(define (generate-row i n matrix1 matrix2)
  (if (= i n)
      '()
      (cons (matrix-element i 0 matrix1 matrix2)
            (generate-row (+ i 1) n matrix1 matrix2))))

(define (generate-matrix i m n matrix1 matrix2)
  (if (= i m)
      '()
      (cons (generate-row i n matrix1 matrix2)
            (generate-matrix (+ i 1) m n matrix1 matrix2))))

;Output
(define result (generate-matrix 0 (length matrix1) (length (car matrix2)) matrix1 matrix2))

(display "Matrix 1:\n")
(for-each (lambda (row) (display row) (newline)) matrix1)
(newline)

(display "Matrix 2:\n")
(for-each (lambda (row) (display row) (newline)) matrix2)
(newline)

(display "Matrix product:\n")
(for-each (lambda (row) (display row) (newline)) result)
