;Define function
(define (bubble-sort lst)
;Swapping element
  (define (swap lst)
    (if (or (null? lst) (null? (cdr lst)))
        lst
        (let ((x (car lst))
              (y (cadr lst)))
          (if (> x y)
              (cons y (swap (cons x (cddr lst))))
              (cons x (swap (cdr lst)))))))

;Loop and get new sorted list
  (let loop ((lst lst) (changed? #t))
    (if changed?
        (let ((new-list (swap lst)))
          (loop new-list (not (equal? lst new-list))))
        lst)))

;Input
(define numbers '(6 3 5 7 9 1))

;Output
(display "Original list: ")
(display numbers)
(newline)

(display "Sorted list: ")
(display (bubble-sort numbers))
(newline)
