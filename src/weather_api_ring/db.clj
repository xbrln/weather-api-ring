(ns weather-api-ring.db
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]))

(def db (env :database-url))

(defn migrated?
  []
  (-> (sql/query db ["select count(*) from information_schema.tables where table_name='api_calls'"])
      first :count pos?))

(defn migrate
  []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands db
      (sql/create-table-ddl
       :api_calls
       [[:id :serial "PRIMARY KEY"]
        [:city :varchar "NOT NULL"]
        [:temperature :varchar "NOT NULL"]
        [:created_at :timestamp
         "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]]))
    (println " done")))

(defn save
  [c t]
  (sql/insert! db :api_calls {:city c :temperature t}))

(defn all
  []
  (sql/query db ["SELECT * FROM api_calls ORDER BY id DESC"]))
