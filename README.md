# Naga Core

This is the main repository of the naga project. It contains the java code of the core for both the client application and the database microservice. Since it's a core, it can't be used as is but SPI are provided to port it in different environments and platforms.

This core is especially designed for building systems with backend and frontend applications and share the same logic code between all its parts. The implemented [features](#Features) has been chosen with the intention to build fast, safe, powerful, scalable and upgradable applications. 

## Cross-platform

The code has been carefully written in a way that it can be transpiled from Java to Javascript (through [GWT][gwt-website] or [TeaVM][teavm-website]) and Objective-C (through [J2ObjC][j2objc-website]).  So the client code can be used as is for JavaSE and Android ports or transpiled for web and iOS ports. In the same way the microservice code can be used as is for Java based servers ports or transpiled for Javascript based servers ports. You can see the different ports provided for both the client and the microservice as part of the [Naga stack][naga-stack-repo], which also gives an overview of all other repositories of the naga project.  

## Features

* Speed & comfort
	* Client query cache with differential server push (automatic refresh)
	* Proxy server for backend LAN speed access
	* Archive/live database split
	* In-memory database cache
	* Offline client (embed database)
* Safety
	* Fine users/roles/actions/scope security model
	* Database history
	* Database undo
	* Remote hot backup (database replication)
* Power
	* Dynamic filters
	* Dynamic groups
	* Dynamic alerts
* Scalability
	* reactive microservice
* Upgradability
	* Hot model  with automatic database schema update
	* Model refactoring support
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
