(ns as.data)

(def orders
  [{:id 1
    :customer "Ana"
    :delivery? true
    :payment :card
    :items [{:cake :coko-torta
             :qty 1
             :price 3200}
            {:cake :cheesecake
             :qty 2
             :price 2800}]}

   {:id 2
    :customer "Marko"
    :delivery? false
    :payment :cash
    :items [{:cake :coko-torta
             :qty 1
             :price 3200}
            {:cake :plazma-torta
             :qty 1
             :price 3000}]}

   {:id 3
    :customer "Ana"
    :delivery? true
    :payment :cash
    :items [{:cake :cheesecake
             :qty 1
             :price 2800}]}

   {:id 4
    :customer "Jelena"
    :delivery? false
    :payment :card
    :items [{:cake :mix-kolaci
             :qty 3
             :price 1500}]}])

