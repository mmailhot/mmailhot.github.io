(use '[leiningen.exec :only (deps)])

(deps '[[hiccup "1.0.5"]])

(require '[hiccup.core :refer :all]
         '[clojure.edn :as edn]
         '[clojure.java.io :as io])

(defn project-html [project]
  [:section.dated-item.project
   [:div.dated-item-head
    [:h3.name [:a {:href (str "http://" (:link project))} (:name project)]]]
   [:p (:description project)]])

(defn contact-html [contact]
  [:div.contact-container
   [:span.email [:a {:href (str "mailto:" (:email contact))} (:email contact)]] " "
   [:span.phone (:phone contact)] " "
   [:span.web [:a {:href (str "http://" (:web contact))} (:web contact)]] " "
   [:span.twitter [:a {:href (str "http://twitter.com/" (:twitter contact))} "@" (:twitter contact)]] " "
   [:span.github [:a {:href (str "http://github.com/" (:github contact))} "github.com/" (:github contact)]]])

(defn year-html [[year listing]]
  [:div.dated-item.other-year
   [:div.dated-item-heard
    [:h3.year year]]
   [:ul (map #(identity [:li [:p %]]) listing)]]
  )

(defn resume-html []
  [:a {:href "http://mlht.ca/resume"} [:p "See it here"]])

(defn generate-website [data]
  [:html
   [:head
    [:title "Marc Mailhot"]
    [:link {:href "main.css" :rel "stylesheet"}]
    [:link {:href "https://fonts.googleapis.com/css?family=Lato:400,700,300,300italic,400italic,700italic" :rel "stylesheet"}]]
   [:body
    [:header
     [:h1 "Marc Mailhot"]
     [:p "Software Developer"]
     (contact-html (:contact data))]
    [:div.container
     [:section#about
      [:h2 "About"]
      [:p (:about data)]]
     [:section#resume
      [:h2 "Resume"]
      (resume-html)]
     [:section#projects.dated-list
      [:h2 "Selected Projects"]
      [:div (map project-html (:selected-projects data))]]
     [:section#other
      [:h2 "Other Things"]
      [:div (map year-html (seq (:other data)))]]]]])


(with-open [rdr (java.io.PushbackReader. (io/reader "data.edn"))
            wrtr (io/writer "index.html")]
  (.write wrtr (html (generate-website (edn/read rdr)))))
