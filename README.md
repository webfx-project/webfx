# WebFX

WebFX uses [GWT][gwt-website] to compile your Java code into JavaScript.
Normally GWT can only compile the Java code (your application logic) and not the JavaFX code (your application UI).
WebFX solves this problem by providing a GWT compatible version of OpenJFX (the [webfx-kit][webfx-kit] module)
that can be transpiled by GWT together with your application code.

For more explanation, please visit the [website][webfx-website] and read the [documentation][webfx-docs].

## Live demos

### Basics

* [Colorful circles][webfx-colorfulcircles-demo] ([single source file][webfx-colorfulcircles-code] - [Github repository][webfx-colorfulcircles-repo])

* [Particles][webfx-particles-demo] ([single source file][webfx-particles-code] - [Github repository][webfx-particles-repo])

### Custom controls

* [Tally Counter][webfx-tallycounter-demo] ([single source file][webfx-tallycounter-code] - [Github repository][webfx-tallycounter-repo])

* [Modern gauge][webfx-moderngauge-demo] ([single source file][webfx-moderngauge-code] - [Github repository][webfx-moderngauge-repo])

* [Enzo clocks][webfx-enzoclocks-demo] ([Github repository][webfx-enzoclocks-repo])

### Games

* [FX2048][webfx-fx2048-demo] ([Github repository][webfx-fx2048-repo])

* [SpaceFX][webfx-spacefx-demo] ([Github repository][webfx-spacefx-repo])

### Web workers

* [Ray tracer][webfx-raytracer-demo] ([Github repository][webfx-raytracer-repo])

### WebAssembly

* [Mandelbrot][webfx-mandelbrot-demo] ([Github repository][webfx-mandelbrot-repo])


## Fully cross-platform

WebFX doesn't target only the Web. WebFX applications can be compiled to run natively on all major platforms from a single code base:

- [x] Desktop (Windows, macOS & Linux) - 64-bit support for native, 32/64-bit support for embed JRE
- [x] Tablet & mobiles (Android & iOS) - 64-bit support for native, no 32-bit support
- [x] Browser (Chrome, FireFox, Edge, etc...)
- [ ] Embed (Raspberry Pi) - 64-bit support for native, 32/64-bit support for embed JRE

You can check out the demos to see how a GitHub workflow can generate these executables.
For example, here is the [GitHub workflow][webfx-fx2048-workflow] for the FX2048 demo and the [executables][webfx-fx2048-release] that it generated (just expand the release Assets to see them).

## Getting started

You can follow the [guide to getting started][webfx-guide] included in the documentation.

## Status

Although the project successfully passed the Proof of Concept and the prototype phases, it's still in the incubation phase. We are working to make it a Minimal Viable Product.

WebFX is not yet ready for production.
At this stage, we provide only snapshot releases, and breaking changes may occur until the first official release.
But you are very welcome to try WebFX and play with it.

## History

The project started in 2015 with the idea to develop a new UI toolkit for GWT inspired from JavaFX. When Java 9 arrived 2 years later with the new module system, it became possible to reuse the javafx package name. This led to the idea of emulating JavaFX rather than building a new toolkit. After refactoring the initial prototype to align it with the JavaFX API, the new features were taken from the OpenJFX code instead, and patched to make them GWT compatible.

After writing some demos, it became clear that WebFX had a great potential. Our context was a complex enterprise application (Java back-end, JavaFX back-office & JS web app), and we were looking for a Java-based cross-platform solution to solve our need to share a lot of logic and UI code between the 2 front-ends, and also to target the mobiles in the future. After a successful prototype, the decision was finally made this year (2022) to rewrite the system onto WebFX! Both projects (WebFX and the enterprise application) are being open-sourced on GitHub (we will communicate more on this later). Great news for WebFX!

## Roadmap

Here is the roadmap (past and future) up to the General Availability:

- [x] Proof of Concept
- [x] Prototype
- [x] Demos
- [x] CI pipeline (using GitHub workflow)
- [x] Enterprise application prototype
- [x] [WebFX CLI][webfx-cli-repo]
- [x] [Website][webfx-website]
- [x] [Documentation][webfx-docs]
- [x] [Blog][webfx-blog]
- [ ] FAQ
- [ ] Customization (application name, icon, etc...)
- [ ] GitHub workflow automatic generation by the CLI
- [ ] WebFX maven plugin
- [ ] Investigating work
    - [ ] [JxBrowser][jxbrowser-website] support
    - [ ] [J2CL][j2cl-repo] support
    - [ ] [TeaVM][teavm-website] support
    - [ ] [Kotlin][kotlin-website] support
    - [ ] SVG support
- [ ] Additional CLI features for contributors
- [ ] Improvement of the OpenJFX patch process
    - [ ] Automate the process to ease a rebase on OpenJFX
    - [ ] Rebase WebFX on OpenJFX latest version
    - [ ] Add code masking to allow coverage customization
- [ ] Creation of initial coverages
    - [ ] Minimal coverage
    - [ ] CSS coverage
- [ ] Open issues
- [ ] General Availability

The JxBrowser support will offer a web view that developers can use to run, test and debug the web version of their WebFX app directly from Java (no GWT compilation required).
The IDE will simply run your app with the WebFX runtime (instead of OpenJFX) in the JVM, and the DOM generated by WebFX will be rendered in JxBrowser (a Chromium-based browser controlled by Java).
This will considerably speed up the development cycle of web apps.

An effort will also be made to make WebFX transpiler agnostic, as much as possible.
Alternative transpilers (such as J2CL & TeaVM) will be considered.
Whereas GWT is a Java source transpiler, TeaVM is a byte-code transpiler, which will also open the door to alternative JVM languages such as Kotlin.

Some redesign work is also necessary to transit from the current patching process (which is mainly a manual patch of OpenJFX 8) to a more professional patch process with tooling allowing regular rebase on OpenJFX.

Finally, WebFX will allow you to customize your JavaFX coverage.
The minimal coverage will prioritize performance and exclude features having a big impact on the resulted JS, such as JavaFX CSS (look at the [FX2048 demo][webfx-fx2048-repo] to see how to make a separate HTML version and JavaFX version of your CSS instead).
But if you still wish to use JavaFX CSS in your web app, you will be able to enable the CSS coverage.
More coverages (such as FXML, 3D, etc...) can be planned in the future.

Issues will be opened once the redesign process has been completed, and the project has reached a more stable state.


## Keep updated

You can follow us by subscribing to our [blog][webfx-blog] where we will post regular news and updates on the progress made.

## Get involved!

You can greatly help the project by:

- Following the [guide][webfx-guide] and start experimenting with WebFX
- Reporting any issues you may have with the [WebFX CLI][webfx-cli-repo], which we will try to fix
- Starring the project if you like it, or wish to encourage us
- Sharing the [website][webfx-website] with people who might be interested
- Giving us feedback in our GitHub [discussions][webfx-discussions]
- Sharing your first WebFX applications (we might add some to our demo list)
- [Contacting us][webfx-contact] if you wish to contribute to the development
- In particular, if you have some expertise in Java language syntax parsing, we can entrust some jobs to you, starting with fixing the current limitation of the [WebFX CLI][webfx-cli-repo].

Thank you for your interest and supporting the project!

## License

WebFX is a free, open-source software licensed under the [Apache License 2.0][webfx-license]

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-license]: https://github.com/webfx-project/webfx/blob/main/LICENSE
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
[j2cl-repo]: https://github.com/google/j2cl
[teavm-website]: https://teavm.org
[jxbrowser-website]: https://www.teamdev.com/jxbrowser
[kotlin-website]: https://kotlinlang.org
[webfx-kit]: https://github.com/webfx-project/webfx/blob/main/webfx-kit
[webfx-colorfulcircles-demo]: https://colorfulcircles.webfx.dev
[webfx-colorfulcircles-repo]: https://github.com/webfx-project/webfx-demo-colorfulcircles
[webfx-colorfulcircles-code]: https://github.com/webfx-demos/webfx-demo-colorfulcircles/blob/main/webfx-demo-colorfulcircles-application/src/main/java/dev/webfx/demo/colorfulcircles/ColorfulCircles.java
[webfx-particles-demo]: https://particles.webfx.dev
[webfx-particles-repo]: https://github.com/webfx-project/webfx-demo-particles
[webfx-particles-code]: https://github.com/webfx-demos/webfx-demo-particles/blob/main/webfx-demo-particles-application/src/main/java/dev/webfx/demo/particles/ParticlesApplication.java
[webfx-tallycounter-demo]: https://tallycounter.webfx.dev
[webfx-tallycounter-repo]: https://github.com/webfx-project/webfx-demo-tallycounter
[webfx-tallycounter-code]: https://github.com/webfx-demos/webfx-demo-tallycounter/blob/main/webfx-demo-tallycounter-application/src/main/java/dev/webfx/demo/tallycounter/TallyCounterApplication.java
[webfx-moderngauge-demo]: https://moderngauge.webfx.dev
[webfx-moderngauge-repo]: https://github.com/webfx-project/webfx-demo-moderngauge
[webfx-moderngauge-code]: https://github.com/webfx-demos/webfx-demo-moderngauge/blob/main/webfx-demo-moderngauge-application/src/main/java/dev/webfx/demo/moderngauge/ModernGaugeApplication.java
[webfx-enzoclocks-demo]: https://enzoclocks.webfx.dev
[webfx-enzoclocks-repo]: https://github.com/webfx-project/webfx-demo-enzoclocks
[webfx-fx2048-demo]: https://fx2048.webfx.dev
[webfx-fx2048-repo]: https://github.com/webfx-project/webfx-demo-fx2048
[webfx-fx2048-workflow]: https://github.com/webfx-demos/webfx-demo-fx2048/blob/main/.github/workflows/builds.yml
[webfx-fx2048-release]: https://github.com/webfx-demos/webfx-demo-fx2048/releases
[webfx-spacefx-demo]: https://spacefx.webfx.dev
[webfx-spacefx-repo]: https://github.com/webfx-project/webfx-demo-spacefx
[webfx-raytracer-demo]: https://raytracer.webfx.dev
[webfx-raytracer-repo]: https://github.com/webfx-project/webfx-demo-raytracer
[webfx-mandelbrot-demo]: https://mandelbrot.webfx.dev
[webfx-mandelbrot-repo]: https://github.com/webfx-project/webfx-demo-mandelbrot
