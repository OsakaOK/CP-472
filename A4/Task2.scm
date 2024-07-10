;Define function
(define (square x) (* x x))

(define (sum-of-squares numbers)
  (fold-right + 0 (map square numbers)))

;Input
(define numbers '(1 2 3 4 5))

;Output
(define result (sum-of-squares numbers))
(display "Sum of squares: ")
(display result)
(newline)

