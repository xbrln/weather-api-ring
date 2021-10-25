(ns weather-api-ring.models.api-calls
  (:require [environ.core :refer [env]]
            [next.jdbc :as jdbc]))

(def db-spec {:dbtype "postgres"
              :dbname "weather_api_ring"
              :user "weather_api_ring"
              :password (env :db-password)
              :serverTimezone "CET"})

(def ds (jdbc/get-datasource db-spec))

(defn migrated?
  "Predicate to check if table is migrated"
  []
  (-> (jdbc/execute!
       ds
       ["select count(*) from information_schema.tables where table_name='api_calls'"])
      first :count pos?))

(defn migrate
  "Migrate table"
  []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (jdbc/execute-one!
     ds
     ["CREATE TABLE api_calls ( id SERIAL PRIMARY KEY, city character varying NOT NULL, temperature character varying NOT NULL, created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP );"])
    (println " done")))

(defn save
  "Save weather api call"
  [c t]
  (jdbc/execute-one!
   ds
   ["INSERT INTO api_calls (city,temperature) VALUES (?,?)" c t]))

(defn all
  "Get all records"
  []
  (jdbc/execute! ds ["SELECT * FROM api_calls ORDER BY id DESC"]))

(comment
  (migrate))
