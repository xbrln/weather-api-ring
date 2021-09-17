(ns weather-api-ring.core-test
  (:require [clojure.test :refer :all]
            [weather-api-ring.core :refer :all]))

(deftest start-server-test
  (testing "if server can be started"
    (is (= 0 (start-server 3000)))))
