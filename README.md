# WebFx
WebFx uses [GWT][gwt-website] to compile your Java code into JavaScript. Normally GWT can only compile the java code (your application logic) and not the JavaFx code (your application UI). WebFx solves this problem by providing a web port of JavaFx (the webfx-fxkit module) that can be compiled by GWT together with your application code.

Because JavaFx was not originally designed for web applications, WebFx also provides an application framework (webfx-framework) with useful web oriented features such as a UI router.

For low level features such as json, timers, websockets, etc... WebFx provides a few APIs that work both in the JVM and the browser.

You will have 2 builds of your application. A pure JavaFx build that you will use for the development, testing and debugging in your preferred Java IDE. And a web build when compiling your application together with the WebFx ecosystem.

[gwt-website]: http://www.gwtproject.org