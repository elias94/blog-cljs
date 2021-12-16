---
title: The Language
categories: clojure programming blog
---

This blog is written in Clojure. Blogs usually use CMS
like Wordpress, Hugo or my favourite one, Jekyll. I liked Jekyll
because of the high level of customization allowed.

But if you want to do web seriously you need to create your own 
platform. So this website is my personal space and will be constructed
step by step every time additional features are needed.

I decided to write it in Clojure. [Clojure](https://clojure.org) is a 
functional programming language that could be
compiled targeting both Java (JVM) and Javascript (Google Closure).
This means that the server and the client will use the same 
language. At the moment the server just has an API with some basic
routes for displaying the post. The version running in the browser
is called Clojurescript.

Clojure is a modern LISP with a different approach to the classic
functional programming. Like Elisp did for Emacs, with Clojure we
can easily integrate a REPL in your application. As I did here it's
just a matter of a couple of lines. (Sorry Ruby).
This entire app has been created in React. Whait, React on top of Clojure?
No a wrapper of React funcionalities in Clojure.
You will see Clojure in action in a minute. But first let me rewind
everything.

- **Clojure** is running on the server, replacing your Ruby, Python on Java server.
- **Clojure** is running on the client, replacing Javascript
- **Clojure** is running the Javascript UI library (react + redux)
- **Clojure** is available on a REPL inside the web page

Here is the idea that you have in your mind in this precise moment.

> *One language to rule them all!* 

Let me try to introduce you to the LISP key concepts.

In Clojure we have the standars datatypes:

```clojure
1    ; Integer
"s"  ; String
true ; Boolean
:key ; Keyword
```

and we have 3 main data structures:

```clojure
;; List
(list 1 2 3)

;; Vectors
[1 2 3]

;; Maps
{:key "value"}
```

Let's see some code.

```clojure
(def planet "Earth") 

(defn welcome []
  (str "Hello " (append-mark planet) "!")

(welcome)
```
