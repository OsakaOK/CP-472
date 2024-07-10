;Define function
(define (simple-interest principal rate time)
  (let ((interest (* principal rate time))) (/ interest 100)))

; Take input principle, rate, time in order below
(define result (simple-interest 1000 5 2))
(display "Simple Interest: ")
(display result)
(newline)