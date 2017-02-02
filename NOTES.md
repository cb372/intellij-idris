# Implementation notes

## What I've done so far

I started with the simplest thing possible, using the Idris REPL's `--client` mode. This is what the vim plugin does. I managed to get a simple "show type" action working with this after only an hour or two of playing around. I tweeted a couple of screenshots, which generated more excitement than I expected!

Then I discovered the Idris IDE Protocol. Apparently this is the recoommended way to do things these days. It's more complex than `idris --client`, but a lot more powerful. e.g. it can do syntax highlighting for us, so it seemed like it was worth pursuing.

I've started implementing an IDE Protocol client that can send commands and parse the responses. This is basically working, but I need to extend the parser to support all the response types.

## Things to explore next

* We are starting a REPL process inside IntelliJ in order to use the IDE Protocol. What should the lifecycle of this REPL process be? Do we need a separate process for each Idris file you have open in an editor? (This seems pretty heavyweight.) Or have just one process shared across all editor panes? But nasty things could happen if we try to call `:load-file` for two files at once, so do we need some kind of locking?

* When are we supposed to call `:load-file`? Once when the editor pane opens, I guess, so we can do syntax highlighting and error highlighting. But do we also need to run it again immediately before running commands such as `:show-type`? Or does the Idris REPL start tracking all changes to the file after you call `:load-file`?

* How do we do syntax highlighting? The plugin SDK doesn't seem to be designed for the possibility of using an external tool for highlighting. It expects us to write a grammar to generate a parser and lexer.

* I need to learn a lot more about how IntelliJ plugins work. e.g. how can you get notified when a user opens an Idris file in an editor pane? All I've played around with so far is making an Action, triggered by a keyboard shortcut.

## References

The JetBrains Scala plugin for IntelliJ has been my main reference. It's in Scala, so I can understand it, and it's large enough to exercise a lot of the features of the plugin SDK.

Emacs idris-mode will probably be another useful reference because it uses the Idris IDE Protocol. Need to brush up on my lisp...

The vim plugin is nice just because it's so simple. But it uses the REPL's client mode, which is not that useful to us given that we want to use the IDE Protocol instead.
