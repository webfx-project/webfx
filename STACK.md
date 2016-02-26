# The Naga stack

An overview of the different repositories and modules under the [naga-project][naga-home]:

* [naga][naga-repo] - The naga core written in java
* The naga client core ports 
	* [naga-plat-jre][naga-plat-jre-repo] - The naga client core port for JavaSE applications
	* Web ports - The naga client core ports for web applications 
		* [naga-teavm][naga-teavm-repo] - transpiled to Javascript through [TeaVM][teavm-website] compiler 
		* [naga-gwt][naga-gwt-repo] - transpiled to Javascript through [GWT][gwt-website] compiler
	* Mobile ports 
		* [naga-android][naga-android-repo] - The naga client core port for Android applications
		* [naga-j2objc][naga-j2objc-repo] - The naga client core port for iOS applications (transpiled through [J2ObjC][j2objc-website] compiler)
	* Abstract (intermediate) ports
	    * [naga-abstractplat-java][naga-abstractplat-java-repo] - The common code shared by java client platforms (ie [naga-plat-jre][naga-plat-jre-repo] and [naga-android][naga-android-repo])  
	    * [naga-abstractplat-web][naga-abstractplat-web-repo] - The common code shared by web client platforms (ie [naga-teavm][naga-teavm-repo] and [naga-gwt][naga-gwt-repo])
* The naga microservice ports 
	* [naga-vertx][naga-vertx-repo] - The naga miroservice port for Vert.x
	* [naga-akka][naga-akka-repo] - The naga miroservice port for Akka
	* [naga-nodejs][naga-nodejs-repo] - The naga miroservice port for Node.js
* Data binding helpers for some UI toolkits
	* JavaSE 
		* [naga-ui-javafx][naga-ui-javafx-repo] - Bindings for JavaFX
	* Browser
		* [naga-ui-gwt][naga-ui-gwt-repo] - Bindings for GWT
		* [naga-ui-angular][naga-ui-angular-repo] - Bindings for Angular
		* [naga-ui-zebra][naga-ui-zebra-repo] - Bindings for Zebra
* Tools for naga applications
	* [naga-modeler]() - A schema modeler with export in the naga model json format
	* [naga-designer]() - An activity designer to declare the queries and bindings with the user interface, also exported in json format.
	* [naga-admin]() - A microservices & databases administration & monitoring tool
- [naga-examples][naga-examples-repo] - Simple naga applications examples
- [naga-project.github.io][naga-project.github.io-repo] - The naga project [website][naga-website] hosted on Github


[naga-home]: https://github.com/naga-project
[naga-repo]: https://github.com/naga-project/naga
[naga-abstractplat-java-repo]: https://github.com/naga-project/naga/blob/master/naga-abstractplat-java
[naga-abstractplat-web-repo]: https://github.com/naga-project/naga/blob/master/naga-abstractplat-web
[naga-plat-jre-repo]: https://github.com/naga-project/naga/blob/master/naga-plat-jre
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