Description [![Build Status](https://travis-ci.org/seroperson/itstimetoact.svg?branch=master)](https://travis-ci.org/seroperson/itstimetoact) [![Coverage Status](https://coveralls.io/repos/github/seroperson/itstimetoact/badge.svg?branch=master)](https://coveralls.io/github/seroperson/itstimetoact?branch=master)
===========
itstimetoact - it's an android library, inspired by [once](https://github.com/jonfinerty/Once), that can help you with planning events in your awesome application. It requires *api level >= 1*, so can be used in any project.

Structure
-------
As it's a very lightweight library, the core module hasn't anything except the base classes, but there is several additional modules with some tasty implementations included. Moreover, if you feel the lack of it, then flexible, awesome, simple and documentated API will help you with creating your own! For now the following modules are available:

* **itstimetoact-core** - provides several base classes. Actually you may use only this module if you are going to implement all of your events and serialization/deserialization mechanism by yourself.
* **itstimetoact-serializable** - implementation that uses `java.io` serializables to serialize/deserialize events. The main pros of this module that it hasn't any external dependencies (unlike the next one).
* **itstimetoact-gson** - another one implementation that uses gson library to serialize/deserialize events. It is preferable to use this one over of previous one (especially if you already use gson in your project or if you are using your own event implementations).
* **itstimetoact-extras** - the set of some useful event implementations, that, I hope, will satisfy most of your needs.

Usage
=====
It will be great start to check the **itstimetoact-sample** module. If you too lazy for it, read the stuff below.

Initializing
------------
Okay, let's start. Initially, you need to create TimeToAct instance. It can be performed in `Application#onCreate` or in `Activity#onCreate(Bundle)`. Anyway, it fits great with something like dagger, so there is no much difference.

```java
TimeToAct timeToActInstance = new GsonTimeToAct(this); // or SerializableTimeToAct
```

Restoring previous state
------------------------
The next thing which you need to do is restore previous state. Even if it's a first run ever, you need to call this method:

```java
timeToActInstance.loadEventData();
```

Keep in mind, that this method isn't async, so can stuck your UI-thread.

Action
------
Now you can write your code. Imagine, that all of next stuff were written in `Activity#onCreate(Bundle)`.

For example, the following code will be executed after each application update:

```java
ActEvent afterUpdateEvent = timeToActInstance.watchEvent(new AfterUpdateEvent(this, "key0"));
if(afterUpdateEvent.isHappened()) {
    // ...
    // show changelog or something like that
    // ...
    timeToActInstance.watchLastDropped("key0");
}
```

The following code will be executed after five days:

```java
ActEvent timeEvent = timeToActInstance.watchEvent(new TimeEvent(TimeUnit.DAYS.toMillis(5), "key1"));
if(timeEvent.isHappened()) {
    // ...
    // show 'rate me' dialog
    // ...
}
```

Next one will be executed only once per install:

```java
OneShotEvent oneShotEvent = timeToActInstance.watchEvent(new OneShotEvent("key2"));
if(!oneShotEvent.isHappened()) {
    // ...
    // show application tutorial
    // ...
    oneShotEvent.step();
}
```

You also can combine they together to describe more difficult situations. And as I said above, there is a flexible API, so you can implement your own awesome 'is happened' logic!

Now imagine the following situation. You have 'rate me' dialog (that must appears after two days since install) with three buttons: "**Don't show again**", "**Reming me later**", "**Rate now**". With the library it can be implemented like this (example from real application):

```java
OneShotEvent neverShowClicked = timeToActInstance.watchEvent(new OneShotEvent(KEY_SHOW_RATE_ME));
TimeEvent cycle = timeToActInstance.watchEvent(new TimeEvent(TimeUnit.DAYS.toMillis(2), KEY_SHOW_RATE_ME_CYCLE));
if(!neverShowClicked.isHappened() && cycle.isHappened()) {
    // ...
    // some dialog creation logic
    // ...
    dialog.show(new DialogListener() {
        @Override
        protected void onViewCreated(View view) {
            applyListener(view, R.id.rate_no, this::dontShowAgain);
            applyListener(view, R.id.rate_later, () -> timeToActInstance.watchLastDropped(KEY_SHOW_RATE_ME_CYCLE));
            applyListener(view, R.id.rate_yes, () -> {
                openMarketPage();
                dontShowAgain();
            });
        }

        private void dontShowAgain() {
            neverShowClicked.step();
        }
    });
}
```


Working with sources
========
To assemble all of available targets:

```sh
./gradlew assemble
```

Or the following to run tests:

```sh
./gradlew test
```

Pull requests are welcome.

Download
========
Just include required line in your **build.gradle**

```groovy
compile 'com.seroperson.itstimetoact:itstimetoact-core:0.2'
compile 'com.seroperson.itstimetoact:itstimetoact-serializable:0.2'
compile 'com.seroperson.itstimetoact:itstimetoact-gson:0.2'
compile 'com.seroperson.itstimetoact:itstimetoact-extras:0.2'
```

Be sure that you included `jcenter()` in list of your repositories.

License
=======

```
The MIT License (MIT)

Copyright (c) 2016 Daniil Sivak

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
