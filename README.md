# Naga Core

This is the main repository of the Naga project and the core of the [Naga stack][naga-stack-md]. 

## The Naga project

The Naga project is a set of components for building cross-platform backend and frontend applications in java (supported platforms: JavaSE, Web, Android and iOS for the client & Vert.x, Akka, Node.js or Servlet for the server). Its main focus is on the non UI part. It provides all the plumbing to bring the data from the database to the application and prepare its rendering for the platform user interface. For example, it can load the data and transform it into a set of JavaFX properties, ready to be displayed in a backend JavaFX table. Server push is supported, so applications can always display up-to-date data without the need to refresh (even for the web). Some helpers are provided to facilitate the data binding with some UI toolkits.

## The Naga core 

This repository contains the java code of the core for both the client and the database microservice. SPI are provided to port it in different environments and platforms. 

The code has been carefully written in a way that it can be transpiled from Java to Javascript (through [GWT][gwt-website] or [TeaVM][teavm-website]) and Objective-C (through [J2ObjC][j2objc-website] or [XMLVM][xmlvm-website]).  So the client code can be used as is for JavaSE and Android ports or transpiled for web and iOS ports. In the same way the microservice code can be used as is for Java based servers ports or transpiled for Javascript based servers ports. You can see the different ports provided for both the client and the microservice as part of the [Naga stack][naga-stack-repo].  

## Features

The features have been chosen to build fast, safe, powerful, upgradable, scalable & long-term applications. 

* Speed & comfort
	* Client query cache with differential server push (automatic refresh)
	* Archive/live database split (for keeping a fast lightweight live database)
	* In-memory microservice database cache
	* LAN Proxy server for backend full speed access
	* Offline client (embed database)
* Safety
	* Fine users/roles/pages/actions/scopes security model
	* Database history (event sourcing)
	* Database undo
	* Remote hot backup (database replication)
* Power
	* Dynamic filters
	* Dynamic groups
	* Dynamic alerts
* Upgradability
	* Hot model redeploy with automatic database schema update
	* Hot model refactoring support
 	* Automatic update (for JavaSE applications)
* Scalability
	* Reactive microservice architecture
* Long-term
	* Clear separation bewteen what to display (non UI) and how to display (UI)
	* Built-in features that don't rely on the UI toolkit
		* i18n
		* Data sorting
	* Easy migration from one UI toolkit to another 

For more information, please visit the [website][naga-website].

[naga-home]: https://github.com/naga-project
[naga-repo]: https://github.com/naga-project/naga
[naga-jre-repo]: https://github.com/naga-project/naga/blob/master/naga-jre
[naga-teavm-repo]: https://github.com/naga-project/naga/blob/master/naga-teavm
[naga-gwt-repo]: https://github.com/naga-project/naga/blob/master/naga-gwt
[naga-android-repo]: https://github.com/naga-project/naga/blob/master/naga-android
[naga-j2objc-repo]: https://github.com/naga-project/naga/blob/master/naga-j2objc
[naga-cn1-repo]: https://github.com/naga-project/naga/blob/master/naga-cn1
[naga-vertx-repo]: https://github.com/naga-project/naga/blob/master/naga-vertx
[naga-akka-repo]: https://github.com/naga-project/naga/blob/master/naga-akka
[naga-nodejs-repo]: https://github.com/naga-project/naga/blob/master/naga-nodejs
[naga-ui-javafx-repo]: https://github.com/naga-project/naga/blob/master/naga-ui-javafx
[naga-ui-gwt-repo]: https://github.com/naga-project/naga/blob/master/naga-ui-gwt
[naga-ui-angular-repo]: https://github.com/naga-project/naga/blob/master/naga-ui-angular
[naga-ui-zebra-repo]: https://github.com/naga-project/naga/blob/master/naga-ui-zebra
[naga-examples-repo]: https://github.com/naga-project/naga/blob/master/naga-examples
[naga-project.github.io-repo]: https://github.com/naga-project/naga-project.github.io
[naga-website]: http://naga-project.github.io
[naga-stack-md]: https://github.com/naga-project/naga/blob/master/STACK.md

[gwt-website]: http://www.gwtproject.org
[teavm-website]: http://teavm.org
[j2objc-website]: http://j2objc.org
[xmlvm-website]: http://xmlvm.org
[codenameone-website]: https://www.codenameone.com
[vertx-website]: http://vertx.io
[akka-website]: http://akka.io
[nodejs-website]: http://nodejs.org