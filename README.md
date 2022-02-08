# WebFX

WebFX uses [GWT][gwt-website] to compile your Java code into JavaScript.
Normally GWT can only compile the java code (your application logic) and not the JavaFX code (your application UI).
WebFX solves this problem by providing a web port of JavaFX (the [webfx-kit][webfx-kit-link] module)
that can be compiled by GWT together with your application code.

You will have 2 builds of your application.
A pure JavaFX build that you will use for the development, testing and debugging in your preferred Java IDE.
And a web build resulting from the compilation of your application code together with the WebFX code by GWT.

## Live demos

### Basics

* [Colorful circles][webfx-colorfulcircles-demo-link] ([single source file][webfx-colorfulcircles-code-link] - [Github repository][webfx-colorfulcircles-repo-link])

* [Particles][webfx-particles-demo-link] ([single source file][webfx-particles-code-link] - [Github repository][webfx-particles-repo-link])

### Custom controls

* [Tally Counter][webfx-tallycounter-demo-link] ([single source file][webfx-tallycounter-code-link] - [Github repository][webfx-tallycounter-repo-link])

* [Modern gauge][webfx-moderngauge-demo-link] ([single source file][webfx-moderngauge-code-link] - [Github repository][webfx-moderngauge-repo-link])

* [Enzo clocks][webfx-enzoclocks-demo-link] ([Github repository][webfx-enzoclocks-repo-link])

### Games

* [FX2048][webfx-fx2048-demo-link] ([Github repository][webfx-fx2048-repo-link])

* [SpaceFX][webfx-spacefx-demo-link] ([Github repository][webfx-spacefx-repo-link])

### Heavy computation
#### (Web workers & WebAssembly)

* [Ray tracer][webfx-raytracer-demo-link] ([Github repository][webfx-raytracer-repo-link])

* [Mandelbrot][webfx-mandelbrot-demo-link] ([Github repository][webfx-mandelbrot-repo-link])
  
## Status

The project is still in incubation phase.
We are currently building the website, and the next step will be a guide to get you started.

You can't use the project before these steps are completed, but don't hesitate to star the repository to let us know you are potentially interested in this technology for your future projects.

Your questions and comments are also very welcome in this [discussion thread][webfx-discussion-link].

[gwt-website]: http://www.gwtproject.org/
[webfx-kit-link]: https://github.com/webfx-project/webfx/blob/main/webfx-kit
[webfx-colorfulcircles-demo-link]: https://colorfulcircles.webfx.dev
[webfx-colorfulcircles-repo-link]: https://github.com/webfx-project/webfx-demo-colorfulcircles
[webfx-colorfulcircles-code-link]: https://github.com/webfx-demos/webfx-demo-colorfulcircles/blob/main/webfx-demo-colorfulcircles-application/src/main/java/dev/webfx/demo/colorfulcircles/ColorfulCircles.java
[webfx-particles-demo-link]: https://particles.webfx.dev
[webfx-particles-repo-link]: https://github.com/webfx-project/webfx-demo-particles
[webfx-particles-code-link]: https://github.com/webfx-demos/webfx-demo-particles/blob/main/webfx-demo-particles-application/src/main/java/dev/webfx/demo/particles/ParticlesApplication.java
[webfx-tallycounter-demo-link]: https://tallycounter.webfx.dev
[webfx-tallycounter-repo-link]: https://github.com/webfx-project/webfx-demo-tallycounter
[webfx-tallycounter-code-link]: https://github.com/webfx-demos/webfx-demo-tallycounter/blob/main/webfx-demo-tallycounter-application/src/main/java/dev/webfx/demo/tallycounter/TallyCounterApplication.java
[webfx-moderngauge-demo-link]: https://moderngauge.webfx.dev
[webfx-moderngauge-repo-link]: https://github.com/webfx-project/webfx-demo-moderngauge
[webfx-moderngauge-code-link]: https://github.com/webfx-demos/webfx-demo-moderngauge/blob/main/webfx-demo-moderngauge-application/src/main/java/dev/webfx/demo/moderngauge/ModernGaugeApplication.java
[webfx-enzoclocks-demo-link]: https://enzoclocks.webfx.dev
[webfx-enzoclocks-repo-link]: https://github.com/webfx-project/webfx-demo-enzoclocks
[webfx-fx2048-demo-link]: https://fx2048.webfx.dev
[webfx-fx2048-repo-link]: https://github.com/webfx-project/webfx-demo-fx2048
[webfx-spacefx-demo-link]: https://spacefx.webfx.dev
[webfx-spacefx-repo-link]: https://github.com/webfx-project/webfx-demo-spacefx
[webfx-raytracer-demo-link]: https://raytracer.webfx.dev
[webfx-raytracer-repo-link]: https://github.com/webfx-project/webfx-demo-raytracer
[webfx-mandelbrot-demo-link]: https://mandelbrot.webfx.dev
[webfx-mandelbrot-repo-link]: https://github.com/webfx-project/webfx-demo-mandelbrot
[webfx-discussion-link]: https://github.com/webfx-project/webfx/discussions/4
