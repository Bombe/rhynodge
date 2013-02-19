# Rhynodge

## Description

Rhynodge is a tool that lets you periodically execute tasks that depend on certain conditions.

Its concept is quite similar to websites like ifttt (“if this then that”): you evaluate an input condition (e. g. data from a website, Facebook or Twitter posts, incoming emails, existence of a file), and if it evaluates to “yes” you execute a certain action.

## Configuration

Rhynodge’s configuration uses JSON files (I tried using XML first but apparently polymorphic deserialization is something that is not easily done with XML parsers). The format of a ``Chain`` configuration is pretty straight-forward and can be seen in the example configuration files.

## Internal Concepts

The core of Rhynodge comprises ``Reaction``s which in turn consist of ``Query``s, ``Filter``s, ``Trigger``s, and ``Action``s.

### Query

A query is a component that determines the state of a system: it can determine whether a file exists, or retrieves the latest 5 Twitter posts, or loads data from a website.

The result of a ``Query`` is a ``State``.

### Filter

A filter is an optional component that turns a ``State`` into a different ``State``. It is used to process data and extract more useful information; e. g. a filter could take the raw data from a website and parse its HTML into a DOM tree, or a list of files could be filtered for CD images larger than 400 MiB.

The result of a ``Filter`` is a ``State``, again.

### Trigger

A trigger decides if, given the current state and the previous state, a noteworthy change has occured. It could calculate the difference between two file lists, or of two Facebook post lists.

The result of a ``Trigger`` is an ``Output``.

### Watcher

A watcher combines a query, a list of filters, and a trigger into a single unit which can be configured a lot easier than the separate components. For example, you could have a “Twitter” that finds new status updates for a username and that only needs to be configured with that username; the rest of the configuration is contained in the watcher.

A ``Watcher`` does not do any processing but instead offers a ``Query``, a list of ``Filter``s, and a ``Trigger``.

### Action

If a trigger found a change, the action is then executed. Again, an action can be almost anything: it can send an email, it can execute programs, print documents, initiate phone calls, take a picture from a webcam — anything you can program can be used an an action.

