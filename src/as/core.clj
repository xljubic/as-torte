(ns as.core
  (:require [as.data :as data]))


;; Helper: total for a single order


(defn order-total
  "Returns total amount (in RSD) for a single order."
  [order]
  (reduce
    (fn [sum item]
      (+ sum (* (:item/qty item) (:item/price item))))
    0
    (:order/items order)))


;; 1) total-revenue  (reduce over orders)


(defn total-revenue
  "Returns total revenue for all orders."
  [orders]
  (reduce
    (fn [sum order]
      (+ sum (order-total order)))
    0
    orders))


;; 2) cakes-sold-per-type (nested reduce)


(defn cakes-sold-per-type
  "Returns a map {cake-type total-qty} for all orders."
  [orders]
  (reduce
    (fn [acc order]
      (reduce
        (fn [acc2 item]
          (update acc2 (:item/cake item)
                  (fnil + 0)
                  (:item/qty item)))
        acc
        (:order/items order)))
    {}
    orders))


;; 3) revenue-by-delivery (reduce grouping)


(defn revenue-by-delivery
  "Returns {:delivery total-delivery-revenue
            :pickup   total-pickup-revenue}."
  [orders]
  (reduce
    (fn [acc order]
      (let [total (order-total order)
            k     (if (:order/delivery? order)
                    :delivery
                    :pickup)]
        (update acc k (fnil + 0) total)))
    {:delivery 0
     :pickup   0}
    orders))


;; 4) top-customer (two reduces)


(defn spending-per-customer
  "Returns a map {customer-name total-spent}."
  [orders]
  (reduce
    (fn [acc order]
      (let [total (order-total order)]
        (update acc (:order/customer order)
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

(defn customer-segments
  "Returns a map {customer-name {:total total-spent
                                 :orders order-count
                                 :segment :vip/:regular/:new}}.
   Segment logika (možeš da menjaš granice kako ti odgovara):
   - :vip     => ukupno >= 15000 RSD
   - :regular => ukupno >= 8000 RSD
   - :new     => sve ispod toga."
  [orders]
  ;; prvo računamo total i broj porudžbina po mušteriji
  (let [stats (reduce
                (fn [acc order]
                  (let [cust  (:order/customer order)
                        total (order-total order)]
                    (-> acc
                        (update-in [cust :total]  (fnil + 0) total)
                        (update-in [cust :orders] (fnil inc 0)))))
                {}
                orders)]
    ;; zatim za svaku mušteriju određujemo segment uz if / else logiku
    (into {}
          (map (fn [[cust {:keys [total orders] :as info}]]
                 (let [segment (if (>= total 15000)
                                 :vip
                                 (if (>= total 8000)
                                   :regular
                                   :new))]
                   [cust (assoc info :segment segment)]))
               stats))))


;; Optional: main for quick demo


(defn -main
  [& _]
  (let [orders data/orders]
    (println "Total revenue:" (total-revenue orders))
    (println "Cakes sold per type:" (cakes-sold-per-type orders))
    (println "Revenue by delivery:" (revenue-by-delivery orders))
    (println "Customer segments:" (customer-segments orders))

  (println "Top customer:" (top-customer orders))))


