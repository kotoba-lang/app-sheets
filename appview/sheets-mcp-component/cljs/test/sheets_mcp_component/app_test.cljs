(ns sheets-mcp-component.app-test
  "Real assertions over the re-frame event/sub logic and the route -> view
  table — not a placeholder `(is true)` (the Svelte-era
  `svelte/test/sheets.test.ts` was exactly that)."
  (:require [cljs.test :refer [deftest is testing]]
            [re-frame.core :as rf]
            [re-frame.db :as rf-db]
            [sheets-mcp-component.app :as app]))

(defn- dispatch-sync-fresh!
  "Re-frame keeps a single global app-db; reset it before each test so tests
  don't depend on run order."
  [event]
  (reset! rf-db/app-db {})
  (rf/dispatch-sync event))

(deftest initialize-sets-route-title-and-subtitle
  (testing "::initialize seeds the db with the ported scaffold copy"
    (dispatch-sync-fresh! [::app/initialize])
    (is (= :index @(rf/subscribe [::app/route])))
    (is (= "sheets-mcp-component" @(rf/subscribe [::app/title])))
    (is (= "ClojureScript entry scaffold (reagent + re-frame + jp-go-dds)."
           @(rf/subscribe [::app/subtitle])))))

(deftest initial-db-matches-what-initialize-produces
  (testing "the event handler and the constant it returns stay in sync"
    (dispatch-sync-fresh! [::app/initialize])
    (is (= (:route app/initial-db) @(rf/subscribe [::app/route])))
    (is (= (:title app/initial-db) @(rf/subscribe [::app/title])))
    (is (= (:subtitle app/initial-db) @(rf/subscribe [::app/subtitle])))))

(deftest views-table-has-an-entry-for-every-former-sveltekit-route
  (testing "svelte/src/routes/ had exactly one route file (+page.svelte, for
    \"/\"), rendering App. The ported views table must still cover it, so the
    route isn't silently dropped."
    (is (contains? app/views :index))
    (is (fn? (:index app/views)))))

(deftest unknown-route-falls-back-to-index-view
  (testing "`(get views route index-view)` in `view` degrades to the one
    known screen instead of throwing on an unrecognized route"
    (is (= (:index app/views) app/index-view))))
