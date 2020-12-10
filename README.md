# WebFx
WebFx uses [GWT][gwt-website] to compile your Java code into JavaScript.
Normally GWT can only compile the java code (your application logic) and not the JavaFx code (your application UI).
WebFx solves this problem by providing a web port of JavaFx (the [webfx-kit][webfx-kit-link] module)
that can be compiled by GWT together with your application code.

Because JavaFx was not originally designed for web applications, WebFx also provides an application framework
(the [webfx-framework][webfx-framework-link] module) with web-oriented features (such as a UI router, Web workers, WebAssembly modules, etc...),
as well as many other features (such as authn, authz, i18n, orm) for building modern web applications.

For low level features such as json, timers, websockets, web workers, etc... WebFx provides some cross-platform APIs
(the [webfx-platform][webfx-platform-link] module) that work both in the JVM and the browser.

You will have 2 builds of your application.
A pure JavaFx build that you will use for the development, testing and debugging in your preferred Java IDE.
And a web build resulting from the compilation of your application code together with the WebFx code by GWT.

## Live demos

### Basics

* [Colorful circles][webfx-colorfulcircles-demo-link] ([Github repository][webfx-colorfulcircles-repo-link])

* [Particles][webfx-particles-demo-link] ([Github repository][webfx-particles-repo-link])

### Custom controls

* [Tally Counter][webfx-tallycounter-demo-link] ([Github repository][webfx-tallycounter-repo-link])

* [Modern gauge][webfx-moderngauge-demo-link] ([Github repository][webfx-moderngauge-repo-link])

* [Enzo clocks][webfx-enzoclocks-demo-link] ([Github repository][webfx-enzoclocks-repo-link])

### Games

* [Fx2048][webfx-fx2048-demo-link] ([Github repository][webfx-fx2048-repo-link])

* [SpaceFx][webfx-spacefx-demo-link] ([Github repository][webfx-spacefx-repo-link])

### Heavy computation

* [Mandelbrot][webfx-mandelbrot-demo-link] ([Github repository][webfx-mandelbrot-repo-link])
  
* [Ray tracer][webfx-raytracer-demo-link] ([Github repository][webfx-raytracer-repo-link])

[gwt-website]: http://www.gwtproject.org/
[teavm-website]: http://teavm.org/
[webfx-kit-link]: https://github.com/webfx-project/webfx/blob/master/webfx-kit
[webfx-framework-link]: https://github.com/webfx-project/webfx/blob/master/webfx-framework
[webfx-platform-link]: https://github.com/webfx-project/webfx/blob/master/webfx-platform
[webfx-colorfulcircles-demo-link]: https://webfx-colorfulcircles-demo.netlify.app
[webfx-colorfulcircles-repo-link]: https://github.com/webfx-project/webfx-demo-colorfulcircles
[webfx-particles-demo-link]: https://webfx-particles-demo.netlify.app
[webfx-particles-repo-link]: https://github.com/webfx-project/webfx-demo-particles
[webfx-tallycounter-demo-link]: https://webfx-tallycounter-demo.netlify.app
[webfx-tallycounter-repo-link]: https://github.com/webfx-project/webfx-demo-tallycounter
[webfx-moderngauge-demo-link]: https://webfx-moderngauge-demo.netlify.app
[webfx-moderngauge-repo-link]: https://github.com/webfx-project/webfx-demo-moderngauge
[webfx-enzoclocks-demo-link]: https://webfx-enzoclocks-demo.netlify.app
[webfx-enzoclocks-repo-link]: https://github.com/webfx-project/webfx-demo-enzoclocks
[webfx-fx2048-demo-link]: https://webfx-fx2048-demo.netlify.app
[webfx-fx2048-repo-link]: https://github.com/webfx-project/webfx-demo-fx2048
[webfx-spacefx-demo-link]: https://webfx-spacefx-demo.netlify.app
[webfx-spacefx-repo-link]: https://github.com/webfx-project/webfx-demo-spacefx
[webfx-mandelbrot-demo-link]: https://webfx-mandelbrot-demo.netlify.app
[webfx-mandelbrot-repo-link]: https://github.com/webfx-project/webfx-demo-mandelbrot
[webfx-raytracer-demo-link]: https://webfx-raytracer-demo.netlify.app
[webfx-raytracer-repo-link]: https://github.com/webfx-project/webfx-demo-raytracer
