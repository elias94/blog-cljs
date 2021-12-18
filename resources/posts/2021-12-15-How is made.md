---
title: The Language
categories: clojure programming blog
---

This website is written in Clojure. Blogs usually use CMS
like Wordpress, Hugo or my favourite one, Jekyll. I liked Jekyll
because of the high level of customization allowed. But is still limited to
markdown and easily expandable.

If you want to do web seriously you need to create your own
platform. So this website is my personal space and will be made
step by step every time additional features are needed.

I decided to write it in Clojure. [Clojure](https://clojure.org) is a
functional programming language that is hosted, that means it is specifically
designed to be run on top of another language's platform.
Clojure is currently running on top of the JVM, and in the browser with
the language version called _Clojurescript_.
This means that the server and the client are using the same
language.

Like Elisp did for Emacs, with Clojure we
can easily integrate a REPL in any application. As I did here it's
just a matter of a couple of lines.
In specific, this app uses React. Wait, React on top of Clojure?
No a wrapper of React funcionalities in Clojure, called _Reagent_.
You will see Clojure in action in a minute. But first let me recap.

- **Clojure** is running on the server, replacing Ruby, Python on Java server.
- **Clojure** is running on the client, replacing Javascript
- **Clojure** is running the UI library and state management (react + redux)
- **Clojure** is available on a REPL inside the web page

Here is the idea that you have in your mind in this precise moment.

> _One language to rule them all!_

But first, let me try to introduce you to some Clojure core key concepts.

In Clojure we have the standars datatypes supported

```clojure
1    ; Integer
"s"  ; String
true ; Boolean
:key ; Keyword
```

and we have 3 main data structures:

```clojure
;; List
(1 2 3)

;; Vectors
[1 2 3]

;; Maps
{:key "value"}
```

LISP languages have the code syntax the same as the one used by the
data structures. In Clojure, the code is written using lists and
vectors. In LISP, code lists are called _forms_. And the first rule;

_The first argument of a form has to be a function._

So if I write a list like `(+ 22 37)` means apply the function `+` 22 to 37.
If I write `(if (= a 2) "two" "diff")` we can see that the first form has 4
items. The first is a _function_, the second is a _list_, the third and the
forth are _strings_. In Js will be

```javascript
if (a === 2) {
  return "two";
} else {
  return "diff";
}
```

In functional programming

Let's see some code.

```clojure run readonly
(def planet "Earth")

(defn welcome []
  (str "Hello " planet "!"))

(welcome)
```

`planet` is a costant string, `welcome` is a function that refer to
`planet`. `(welcome)` is the invocation of the function. The return value is
printed in output.

```clojure run
(defn fib [n]
  ;; (loop [c  0
  ;;        sq []]
  ;;   (if (< c n)
  ;;     (let [last (peek sq)
  ;;           v (+ last c)]
  ;;       (recur v (conj sq v)))
  ;;     sq)))

(fib 5)
```
