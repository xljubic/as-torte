(ns as.data
  (:require [as.entities :as e]))

(def orders
  [(e/order 1 "Ana" true :card
            [(e/item :coko-torta 1 3200)
             (e/item :cheesecake 2 2800)])

   (e/order 2 "Marko" false :cash
            [(e/item :coko-torta 1 3200)
             (e/item :plazma-torta 1 3000)])

   (e/order 3 "Ana" true :cash
            [(e/item :cheesecake 1 2800)])

   (e/order 4 "Jelena" false :card
            [(e/item :mix-kolaci 3 1500)])])
