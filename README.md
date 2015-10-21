# Naga Core

This is the main repository of the naga project (please visit the [naga stack][naga-stack-repo] to have an overview of all other repositories). 

## The naga project

The naga project is a set of components for building cross-platform backend and frontend applications in java (supported platforms: JavaSE, Web, Android and iOS for the client & Vert.x, Akka, Node.js or Servlet for the server). Its main focus is on the non UI part. It provides all the plumbing to bring the data from the database to the application and prepare its rendering for the user interface. For example, it can load the data and transform it into a set of JavaFX properties, ready to be displayed in a JavaFX table for a backend application. Server push is supported, so all applications can always display up-to-date data without the need to refresh. Some helpers are provided to facilitate the data binding with some UI toolkits.

## The naga core 

This repository contains the java code of the core for both the client application and the database microservice. SPI are provided to port it in different environments and platforms. 

The code has been carefully written in a way that it can be transpiled from Java to Javascript (through [GWT][gwt-website] or [TeaVM][teavm-website]) and Objective-C (through [J2ObjC][j2objc-website]).  So the client code can be used as is for JavaSE and Android ports or transpiled for web and iOS ports. In the same way the microservice code can be used as is for Java based servers ports or transpiled for Javascript based servers ports. You can see the different ports provided for both the client and the microservice as part of the [Naga stack][naga-stack-repo].  

## Features

The features have been chosen with the idea to build fast, safe, powerful, scalable and upgradable applications. 

* Speed & comfort
	* Client query cache with differential server push (automatic refresh)
	* LAN Proxy server for backend full speed access
	* Archive/live database split
	* In-memory microservice database cache
	* Offline client (embed database)
* Safety
	* Fine users/roles/pages/actions/scope security model
	* Database history
	* Database undo
	* Remote hot backup (database replication)
* Power
	* Dynamic filters
	* Dynamic groups
	* Dynamic alerts
* Scalability
	* reactive microservice architecture
* Upgradability
	* Hot model redeploy with automatic database schema update
	* Hot model refactoring support
 	* Automatic update (for JavaSE applications)

For more information, please visit the [website][naga-website].

[naga-home]: https://github.com/naga-project
[naga-repo]: https://github.com/naga-project/naga
[naga-jre-repo]: https://github.com/naga-project/naga-jre
[naga-browser-repo]: https://github.com/naga-project/naga-browser
[naga-android-repo]: https://github.com/naga-project/naga-android
[naga-ios-repo]: https://github.com/naga-project/naga-ios
[naga-vertx-repo]: https://github.com/naga-project/naga-vertx
[naga-akka-repo]: https://github.com/naga-project/naga-akka
[naga-nodejs-repo]: https://github.com/naga-project/naga-nodejs
[naga-ui-javafx-repo]: https://github.com/naga-project/naga-ui-javafx
[naga-ui-gwt-repo]: https://github.com/naga-project/naga-ui-gwt
[naga-ui-angular-repo]: https://github.com/naga-project/naga-ui-angular
[naga-ui-zebra-repo]: https://github.com/naga-project/naga-ui-zebra
[naga-project.github.io-repo]: https://github.com/naga-project/naga-project.github.io
[naga-runtimes-repo]: https://github.com/naga-project/naga-runtimes
[naga-bindings-repo]: https://github.com/naga-project/naga-bindings
[naga-examples-repo]: https://github.com/naga-project/naga-examples
[naga-tools-repo]: https://github.com/naga-project/naga-tools
[naga-roadmap-repo]: https://github.com/naga-project/naga-roadmap
[naga-stack-repo]: https://github.com/naga-project/naga-stack
[naga-website]: http://naga-project.github.io
[gwt-website]: http://www.gwtproject.org
[teavm-website]: http://teavm.org
[j2objc-website]: http://j2objc.org
[vertx-website]: http://vertx.io
[akka-website]: http://akka.io
[nodejs-website]: http://nodejs.org
