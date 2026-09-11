(ns sheets-mcp-component.app
  "sheets-mcp-component — reagent + re-frame view built from jp-go-dds hiccup.

  Faithful port of the former Svelte scaffold, which was **two** files:

  - `svelte/src/App.svelte` — the actual scaffold markup (centered `<h1>` +
    `<p>`, page-level `<style>` for body/main).
  - `svelte/src/routes/+page.svelte` — SvelteKit's file-based route for `/`,
    which did nothing but `import App from '../App.svelte'; <App />`.

  There was exactly one route (`src/routes/+page.svelte`, no siblings), and
  it rendered exactly one view (`App`), so this is not a case of two
  distinct screens to switch between — the two files were one logical page
  split across SvelteKit's routing convention. Per this workspace's SPA rule
  (ADR-2608080100, \"views are held as data, nav generated from it\"), the
  route → view relationship is preserved structurally as a `:route` key in
  app-db plus a `views` lookup table, rather than silently dropped by
  hard-coding the single view. A second route can be added to `views` later
  without introducing a second document/bundle/mount.

  State lives in re-frame (`::initialize` event, `::route`/`::title`/
  `::subtitle` subs) so the scaffold is honest about the stack it now runs
  on, not because this screen needs mutable state yet."
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [jp-go-dds.core :as dds]))

(def initial-db
  {:route :index
   :title "sheets-mcp-component"
   :subtitle "ClojureScript entry scaffold (reagent + re-frame + jp-go-dds)."})

(rf/reg-event-db
 ::initialize
 (fn [_ _] initial-db))

(rf/reg-sub ::route (fn [db _] (:route db)))
(rf/reg-sub ::title (fn [db _] (:title db)))
(rf/reg-sub ::subtitle (fn [db _] (:subtitle db)))

(defn index-view
  "Port of `App.svelte`'s markup: a centered heading + message."
  []
  (let [title @(rf/subscribe [::title])
        subtitle @(rf/subscribe [::subtitle])]
    (dds/container
     (dds/section {}
       [:div {:class "dds-ext-hero dds-ext-center"}
        (dds/heading 1 title)
        [:p {:class "dds-ext-lead"} subtitle]]))))

(def views
  "route -> view. Mirrors `svelte/src/routes/` (one file, `+page.svelte`,
  serving `/`). Adding a second SvelteKit route later becomes adding a
  second entry here, not a second HTML document."
  {:index index-view})

(defn view []
  (let [route @(rf/subscribe [::route])
        render (get views route index-view)]
    [:main (render)]))

(defn ^:dev/after-load render! []
  (rdom/render [view] (.getElementById js/document "app")))

(defn ^:export main []
  (rf/dispatch-sync [::initialize])
  (render!))
