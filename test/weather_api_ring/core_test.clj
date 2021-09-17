(ns weather-api-ring.core-test
  (:require [clojure.test :refer :all]
            [weather-api-ring.core :refer :all]))

(deftest start-server-test
  (testing "if server can be started on a port and is an instance of jetty server."
    (is (instance? org.eclipse.jetty.server.Server (start-server 3001)))
    (stop-server)))
