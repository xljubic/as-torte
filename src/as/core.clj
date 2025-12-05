(ns as.core
  (:require [as.data :as data]))

;; ----------------------------------------
;; Helper: total for a single order
;; ----------------------------------------

(defn order-total
  "Returns total amount (in RSD) for a single order."
  [order]
  (reduce
    (fn [sum item]
      (+ sum (* (:qty item) (:price item))))
    0
    (:items order)))

;; ----------------------------------------
;; 1) total-revenue  (reduce over orders)
;; ----------------------------------------

(defn total-revenue
  "Returns total revenue for all orders."
  [orders]
  (reduce
    (fn [sum order]
      (+ sum (order-total order)))
    0
    orders))

;; ----------------------------------------
;; 2) cakes-sold-per-type (nested reduce)
;; ----------------------------------------

(defn cakes-sold-per-type
  "Returns a map {cake-type total-qty} for all orders."
  [orders]
  (reduce
    (fn [acc order]
      (reduce
        (fn [acc2 item]
          (update acc2 (:cake item)
                  (fnil + 0)
                  (:qty item)))
        acc
        (:items order)))
    {}
    orders))

;; ----------------------------------------
;; 3) revenue-by-delivery (reduce grouping)
;; ----------------------------------------

(defn revenue-by-delivery
  "Returns {:delivery total-delivery-revenue
            :pickup   total-pickup-revenue}."
  [orders]
  (reduce
    (fn [acc order]
      (let [total (order-total order)
            k     (if (:delivery? order)
                    :delivery
                    :pickup)]
        (update acc k (fnil + 0) total)))
    {:delivery 0
     :pickup   0}
    orders))

;; ----------------------------------------
;; 4) top-customer (two reduces)
;; ----------------------------------------

(defn spending-per-customer
  "Returns a map {customer-name total-spent}."
  [orders]
  (reduce
    (fn [acc order]
      (let [total (order-total order)]
        (update acc (:customer order)
                (fnil + 0)
                total)))
    {}
    orders))

(defn top-customer
  "Returns [customer-name total-spent] for the best customer."
  [orders]
  (let [by-customer (spending-per-customer orders)]
    (reduce
      (fn [[best-name best-total :as best] [cust total]]
        (cond
          (nil? best)           [cust total]      ; prvi kandidat
          (> total best-total)  [cust total]      ; novi bolji
          :else                 best))            ; zadržavamo starog
      nil
      by-customer)))

;; ----------------------------------------
;; Optional: main for quick demo
;; ----------------------------------------

(defn -main
  [& _]
  (let [orders data/orders]
    (println "Total revenue:" (total-revenue orders))
    (println "Cakes sold per type:" (cakes-sold-per-type orders))
    (println "Revenue by delivery:" (revenue-by-delivery orders))
    (println "Top customer:" (top-customer orders))))

