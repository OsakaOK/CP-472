;Define function
(define (filter-even lst)
  ;Check for even number
  (define (even? x) (= (remainder x 2) 0))
  (define evens (filter even? lst))
  (if (null? evens)
      0
      (apply * evens)))

;Input
(define numbers '(1 2 3 4 5 6))

;Output
(define result (filter-even numbers))
(display "Product of even numbers: ")
(display result)
(newline)
