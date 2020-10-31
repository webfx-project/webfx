# WebFx
WebFx uses [GWT][gwt-website] to compile your Java code into JavaScript.
Normally GWT can only compile the java code (your application logic) and not the JavaFx code (your application UI).
WebFx solves this problem by providing a web port of JavaFx (the [webfx-kit][webfx-kit-link] module)
that can be compiled by GWT together with your application code.

Because JavaFx was not originally designed for web applications, WebFx also provides an application framework
(the [webfx-framework][webfx-framework-link] module) with web-oriented features (such as a UI router),
as well as many other features (such as authn, authz, i18n, orm) for building modern web applications.

For low level features such as json, timers, websockets, web workers, etc... WebFx provides some cross-platform APIs
(the [webfx-platform][webfx-platform-link] module) that work both in the JVM and the browser.

You will have 2 builds of your application.
A pure JavaFx build that you will use for the development, testing and debugging in your preferred Java IDE.
And a web build resulting from the compilation of your application code together with the WebFx code by GWT.

## Live demos

### Basics

* [Colorful circles][webfx-colorfulcircles-demo-link] ([source code][webfx-colorfulcircles-source-link])

One of the simplest JavaFx application you can write beyond Hello World.
The [original code][colorfulcircles-oracle-code-link] was written by Oracle when JavaFx was first launched
(the code has been slightly modified for this demo to better fit in the browser window).
In this example, WebFx maps the JavaFx scene graph into the browser DOM.

* [Particles][webfx-particles-demo-link] ([source code][webfx-particles-source-link])

It was very easy (1/2h) to rewrite this [sketch.js demo][sketch-particles-demo-link] in JavaFx
and recompile it to JavaScript with WebFx.
Of course the JS size is bigger (87k vs 7k) due to the JavaFx layer (javafx-base + javafx-graphics modules), but it's not that bad!
In this example, WebFx maps a JavaFx canvas into a browser canvas.

### Custom controls

You can easily write custom controls with the JavaFx API and even design several skins for the same control.
You can also reuse or port existing Java/JavaFx libraries as demonstrated below.
Note that all these demos are still very simple applications written with just a single file of source code. 

* [Tally Counter][webfx-tallycounter-demo-link] ([source code][webfx-tallycounter-source-link])

Let's write something a bit more useful but still simple: a Tally counter.
To display the counter, this demo is using an odometer control which was already implemented in JavaFx on this [Github repository][hansolo-odometer-link].
The layout is more designed for mobiles.

* [Clock][webfx-clock-demo-link] ([source code][webfx-clock-source-link])

The same author (Hansolo) developed a clock control with different skins in another JavaFx library called [Medusa][hansolo-medusa-link].
Let's just use it!

* [Gauge][webfx-gauge-demo-link] ([source code][webfx-gauge-source-link])

In that same Medusa library Hansolo also developed a gauge control with many skins.
Let's see what the Modern skin looks like.

### Games

Ok all demos so far were just a few pieces of code.
Let's see now what WebFx can do when it's about compiling an entire application. 
Let's start with a couple of games! :-)

* [FX2048][webfx-fx2048-demo-link]

This is the game 2048 (try to sum 2048 on a single tile).
There was already a [JavaFx version][fx2048-link] and WebFx transpiled it for the browser.
This is a scene graph based game styled with CSS.
Also good to play on mobiles and tablets
(use the arrow keys or touch screen to play). 

* [SpaceFX][webfx-spacefx-demo-link]

This space game was also already written [in JavaFx][hansolo-spacefx-link] and is an example of a canvas based game.
To be played using the keyboard (instructions on start screen) on a computer, or a tablet with a bluetooth keyboard.
Since it loads much more resources (images & sounds), it may not start well with slow internet connections.

### Heavy computation

Since JavaFx was not originally designed for the web, WebFx provides some additional APIs to work with web-specific concepts
such as web workers and WebAssembly modules, which can be useful for applications requiring heavy background tasks.
WebFx can interact with third-party web workers and WebAssembly modules or you can write your own in Java.
In the following demos, they are written in Java and compiled with [TeaVM][teavm-website]. 

* [Mandelbrot][webfx-mandelbrot-demo-link]

This application proposes 9 Mandelbrot places to visit and computes for each a series of frames to play a zoom effect.
You can adjust the number of web workers and switch between JavaScript and WebAssembly in the settings.
The Mandelbrot computation code was taken from this [Java Swing application][mandelbrot-computation-source].  

* [Ray tracer][webfx-raytracer-demo-link]

A very similar application but with ray tracing, which requires even more computation resources.
The ray tracing computation code was taken from this [JavaFx application][raytracer-computation-source].  


[gwt-website]: http://www.gwtproject.org/
[teavm-website]: http://teavm.org/
[webfx-kit-link]: https://github.com/webfx-project/webfx/blob/master/webfx-kit
[webfx-framework-link]: https://github.com/webfx-project/webfx/blob/master/webfx-framework
[webfx-platform-link]: https://github.com/webfx-project/webfx/blob/master/webfx-platform
[webfx-colorfulcircles-demo-link]: https://webfx-colorfulcircles-demo.netlify.app
[webfx-colorfulcircles-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demo/webfx-demo-colorfulcircles/webfx-demo-colorfulcircles-application/src/main/java/webfx/demo/colorfulcircles/ColorfulCircles.java
[webfx-particles-demo-link]: https://webfx-particles-demo.netlify.app
[webfx-particles-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demo/webfx-demo-particles/webfx-demo-particles-application/src/main/java/webfx/demo/colorfulcircles/ColorfulCircles.java
[webfx-tallycounter-demo-link]: https://webfx-tallycounter-demo.netlify.app
[webfx-tallycounter-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demo/webfx-demo-tallycounter/webfx-demo-tallycounter-application/src/main/java/webfx/demo/tallycounter/TallyCounterApplication.java
[webfx-clock-demo-link]: https://webfx-clock-demo.netlify.app
[webfx-clock-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demo/webfx-demo-clock/webfx-demo-clock-application/src/main/java/webfx/demo/clock/ClockApplication.java
[webfx-gauge-demo-link]: https://webfx-gauge-demo.netlify.app
[webfx-gauge-source-link]: https://github.com/webfx-project/webfx/blob/master/webfx-demo/webfx-demo-gauge/webfx-demo-gauge-application/src/main/java/webfx/demo/gauge/GaugeApplication.java
[webfx-spacefx-demo-link]: https://webfx-spacefx-demo.netlify.app
[webfx-fx2048-demo-link]: https://webfx-fx2048-demo.netlify.app
[webfx-mandelbrot-demo-link]: https://webfx-mandelbrot-demo.netlify.app
[mandelbrot-computation-source]: http://math.hws.edu/eck/js/mandelbrot/java/xMandelbrotSource-1-2/edu/hws/eck/umb/
[webfx-raytracer-demo-link]: https://webfx-raytracer-demo.netlify.app
[raytracer-computation-source]: https://github.com/steventrowland/JavaFX-Ray-Tracer
[colorfulcircles-oracle-code-link]: https://docs.oracle.com/javafx/2/get_started/ColorfulCircles.java.html
[sketch-particles-demo-link]: https://soulwire.github.io/sketch.js/examples/particles.html
[hansolo-odometer-link]: https://github.com/HanSolo/odometer
[hansolo-medusa-link]: https://github.com/HanSolo/Medusa
[hansolo-spacefx-link]: https://github.com/HanSolo/SpaceFX
[fx2048-link]: https://github.com/brunoborges/fx2048