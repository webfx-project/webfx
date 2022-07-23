# WebFX

WebFX uses [GWT][gwt-website] to compile your Java code into JavaScript.
Normally GWT can only compile the Java code (your application logic) and not the JavaFX code (your application UI).
WebFX solves this problem by providing a GWT compatible version of OpenJFX (the [webfx-kit](webfx-kit) module)
that can be transpiled by GWT together with your application code.

For more explanation, please visit the [website][webfx-website] and read the [documentation][webfx-docs].

## Live demos

We have a dedicated space for the [demos](https://github.com/webfx-demos). You will find all the demos presented on the website, plus a couple of additional live demos, with their source code and GitHub workflow.

## Fully cross-platform

WebFX doesn't target only the Web. WebFX applications can be compiled to run natively on all major platforms from a single code base:

- [x] Desktop (Windows, macOS & Linux) - 64-bit support for native, 32/64-bit support for embed JRE
- [x] Tablet & mobiles (Android & iOS) - 64-bit support for native, no 32-bit support
- [x] Browser (Chrome, FireFox, Edge, etc...)
- [ ] Embed (Raspberry Pi) - 64-bit support for native, 32/64-bit support for embed JRE

You can check out the demos to see how a GitHub workflow can generate these executables.
For example, here is the [GitHub workflow](https://github.com/webfx-demos/webfx-demo-fx2048/blob/main/.github/workflows/builds.yml) for the FX2048 demo and the [executables](https://github.com/webfx-demos/webfx-demo-fx2048/releases) that it generated (just expand the release Assets to see them).

## Getting started

You can follow the [guide to getting started][webfx-guide] included in the documentation.

## Status

Although the project successfully passed the Proof of Concept and the prototype phases, it's still in the incubation phase. We are working to make it a Minimal Viable Product.

WebFX is not yet ready for production.
At this stage, we provide only snapshot releases, and breaking changes may occur until the first official release.
But you are very welcome to try WebFX and play with it.

## Roadmap

You can consult our [roadmap](ROADMAP.md). Issues will be open a when approaching the General Availability, and the project will have reached a more stable state.


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

WebFX is a free open source software under [Apache License 2.0](LICENSE)

## History

The project started in 2015 with the idea to develop a new UI toolkit for GWT inspired from JavaFX. When Java 9 arrived 2 years later with the new module system, it became possible to reuse the javafx package name. This is how the idea to finally emulate JavaFX instead of creating a new toolkit arose. After the refactoring of the initial prototype to get it aligned with the JavaFX API was completed, the new features were taken from the OpenJFX code instead, and patched to make them GWT compatible.

After writing some demos, it became clear that WebFX had a great potential. The context was a complex enterprise application (Java back-end, JavaFX back-office & JS web app), and we were looking for a Java-based cross-platform solution to solve our need to share a lot of logic and UI code between the 2 front-ends, and also target the mobiles in the future. After a successful prototype, the decision was finally made this year to rewrite the system onto WebFX! Both projects (WebFX and that enterprise application) are being open-sourced on GitHub (we will communicate more on this later). Great news for WebFX!


[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
