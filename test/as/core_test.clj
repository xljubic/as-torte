(ns as.core-test
  (:require [midje.sweet :refer :all]
            [as.core :as core]
            [as.data :as data]))

(fact "order-total sums items"
  (core/order-total {:items [{:qty 2 :price 100}
                             {:qty 1 :price 50}]})
  => 250)

(fact "total-revenue returns a number"
  (core/total-revenue data/orders)
  => number?)

(fact "revenue-by-delivery has both keys"
  (core/revenue-by-delivery data/orders)
  => (contains {:delivery number?
                :pickup number?}))

(fact "top-customer returns [name total]"
  (core/top-customer data/orders)
  => (contains [string? number?]))
