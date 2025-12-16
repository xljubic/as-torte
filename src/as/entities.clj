(ns as.entities)

(defn item
  "Kreira stavku porudžbine."
  [cake qty price]
  {:item/cake cake
   :item/qty qty
   :item/price price})

(defn order
  "Kreira porudžbinu."
  [id customer delivery? payment items]
  {:order/id id
   :order/customer customer
   :order/delivery? delivery?
   :order/payment payment
   :order/items items})
