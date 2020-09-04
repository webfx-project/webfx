# WebFx
WebFx uses [GWT][gwt-website] to compile your Java code into JavaScript. Normally GWT can only compile the java code (your application logic) and not the JavaFx code (your application UI). WebFx solves this problem by providing a web port of JavaFx (the [webfx-kit][webfx-kit-link] module) that can be compiled by GWT together with your application code.

Because JavaFx was not originally designed for web applications, WebFx also provides an application framework (the [webfx-framework][webfx-framework-link] module) with web oriented features (such as a UI router), as well as many other features (such as authn, authz, i18n, orm) for building modern web applications.

For low level features such as json, timers, websockets, etc... WebFx provides some APIs (the [webfx-platform][webfx-platform-link] module) that work both in the JVM and the browser.

You will have 2 builds of your application. A pure JavaFx build that you will use for the development, testing and debugging in your preferred Java IDE. And a web build resulting from the compilation of your application code together with the WebFx code by GWT.


## Live demos

* [Colorful circles][webfx-colorfulcircles-demo-link]

An Oracle demo when JavaFx was first launched (slightly modified to better fit in the browser window)

* [Tally Counter][webfx-tallycounter-demo-link]

A Tally counter using the [Odometer][hansolo-odometer-link] control

* [Clock][webfx-clock-demo-link]

A Clock control with different skins (from [Medusa][hansolo-medusa-link] library)

* [Gauge][webfx-gauge-demo-link]

A Gauge control with the Modern skin (from [Medusa][hansolo-medusa-link] library)

[gwt-website]: http://www.gwtproject.org
[webfx-kit-link]: https://github.com/webfx-project/webfx/blob/master/webfx-kit
[webfx-framework-link]: https://github.com/webfx-project/webfx/blob/master/webfx-framework
[webfx-platform-link]: https://github.com/webfx-project/webfx/blob/master/webfx-platform
[webfx-colorfulcircles-demo-link]: https://webfx-project.github.io/webfx-demos-colorfulcircles-website/
[webfx-tallycounter-demo-link]: https://webfx-project.github.io/webfx-demos-tallycounter-website/
[webfx-clock-demo-link]: https://webfx-project.github.io/webfx-demos-clock-website/
[webfx-gauge-demo-link]: https://webfx-project.github.io/webfx-demos-gauge-website/
[hansolo-odometer-link]: https://github.com/HanSolo/odometer
[hansolo-medusa-link]: https://github.com/HanSolo/Medusa