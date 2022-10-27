# How to contribute to WebFX

Thank you so much for your wish to contribute to WebFX! There are many areas where help is needed, and we have listed some suggestions below, but we are also open to your own suggestions.


## Reporting bugs

We are aware that there are bugs in the [WebFX Kit](../webfx-kit), but we are not accepting bug reports at this time (issues are not open). The reason is that we have some deep work to do first (see our [roadmap](ROADMAP.md)). However, there are some exceptions:

- **You can report bugs found in the [WebFX CLI][webfx-cli-repo].** Issues are open for the CLI, and we will do our best to fix them.
- **You can report bugs found in the [WebFX Platform][webfx-platform-repo]**, which is our foundation layer & API for cross-platform development (it contains all Java classes whose package starts with `dev.webfx.platform`).


## Fixing bugs

If you found a bug in the [WebFX Kit](../webfx-kit) (in this repository), either in the emulated JavaFX classes (package starting with `javafx` or `com.sun.javafx`), or in the WebFX classes (package starting with `dev.webfx.kit`), you can fork [WebFX](https://github.com/webfx-project/webfx) and try to fix it.

- **We are accepting pull requests should you wish to submit a bug fix from your fork.**

We are also aware of a bug in the WebFX CLI (see the [Known limitation](https://github.com/webfx-project/webfx-cli#known-limitation) section), requiring some expertise in Java language parsing to be fixed.

- **We would be happy to receive help to fix the corresponding [issue](https://github.com/webfx-project/webfx-cli/issues/1).**


## Extending the JavaFX coverage

Extending the JavaFX coverage is not our priority at the moment for the same reason explained above. However, if you want to use a JavaFX control or feature that is not yet covered, please feel free to fork, add the missing OpenJFX code, and patch it to make it GWT compatible (you can look to see how we did this with other classes).

- **We are accepting pull requests should you wish to submit your fix.**


## Porting JavaFX libraries

We have a dedicated space for WebFX libraries called [WebFX Libs](https://github.com/webfx-libs). We started to port some JavaFX libraries to WebFX, for example the Odometer, Enzo and Medusa libraries that we used for the demos. You can use these libraries as well in your WebFX applications. But because we did a partial port (of only the features required for the demos), you may need to extend their coverage.

- **We are accepting pull requests should you wish to submit a coverage extension of an existing WebFX library.**

If you want to use another JavaFX library which is not listed, you can try to patch it and write your own WebFX port.

- **We can then reference your library port in WebFX Libs (please [contact us][webfx-contactus])**


## Writing the Maven plugin

We need to develop a Maven Plugin to speed up the import of large WebFX libraries into WebFX applications. The feature is already implemented in the WebFX CLI, we just need to embed it in a Maven plugin.

- **If you are happy to write the plugin, please [contact us][webfx-contactus].**


## Designing a logo

WebFX would benefit from a unique, well-designed logo.

- **If you have any WebFX logo ideas or drafts, you can [send them to us][webfx-contactus]. We will be delighted to consider your propositions.**


## Writing an Apple signing guide

New WebFX users with no experience in Apple development may have difficulties building and signing their macOS or iOS native apps. It would be helpful to write a guide to assist them in the process, including how to enroll in the Apple Developer Program, how to create a Provisioning Profile, how to set up Xcode etc.

- **If you would like to write the guide, please [contact us][webfx-contactus].**


## Writing JavaDoc

Most of the [WebFX Kit](../webfx-kit) doesn't require JavaDoc as its API is compatible with the JavaFX API, which is already largely documented. However, the [WebFX Platform][webfx-platform-repo] needs to be documented.

- **Please [contact us][webfx-contactus] if you would like to write some JavaDoc.**


## Writing unit tests

There are currently no unit tests, which is something we need to change as soon as possible. Our intention is to start adding tests to the [WebFX Platform][webfx-platform-repo] first.

- **If you are happy to help write some unit tests, please [contact us][webfx-contactus].**


## Writing XML Schema

One of the next steps in our [roadmap](ROADMAP.md) is to document the syntax of `webfx.xml` files. It would be helpful to also write an XSD to assist WebFX developers when they modify these files.

- **If you would like to help with this, please [contact us][webfx-contactus].**


## Reviewing our documentation

Where our [documentation](https://docs.webfx.dev) is not clear or detailed enough, or where you would like additional documentation for other aspects of WebFX that are not planned in the [roadmap](ROADMAP.md), please let us know.

- **You can open an issue for this in our [WebFX Docs](https://github.com/webfx-project/webfx-docs) repository.**


## Code review

Because we are in the incubation phase, now is the best time to review our code, especially our API (breaking changes will be much more painful after that time). If you find it difficult to use, or feel it could be improved, don't hesitate to give us your feedback.

- **If the code is in the [WebFX Platform][webfx-platform-repo], you can open an issue.**
- **If the code is in the [WebFX Kit](../webfx-kit), please [contact us][webfx-contactus].**


## Investigating J2CL

As mentioned in our [roadmap](ROADMAP.md), we will investigate the possibility of supporting alternative transpilers, beginning with [J2CL](https://github.com/google/j2cl).

- **If you already have some experience with J2CL and would like to help, please [contact us][webfx-contactus].**


## Investigating TeaVM

Similarly, we would like to investigate [TeaVM](https://teavm.org). We have already demonstrated in the [Mandelbrot demo](https://mandelbrot.webfx.dev) how to use TeaVM to transpile a part of the application logic into WebAssembly, but here it's about going further by transpiling the whole application.

- **If you are happy to help us investigate the TeaVM support, please [contact us][webfx-contactus].**


## Preparing the Kotlin demo

The TeaVM support will open the door to alternative languages such as Kotlin. To demonstrate this, we will need to make a Kotlin demo. The demo will take a Kotlin app with a JavaFX UI and transpile it into JS with TeaVM. The Kotlin app can be prepared in advance.

- **If you are happy to develop an attractive Kotlin app for the demo, please [contact us][webfx-contactus].**


## Prototyping an Android mapper

This is not really planned in our roadmap, but an interesting parallel development would be to implement an Android mapper (in addition to the DOM mapper). This would allow us to develop Android applications directly in Android Studio with an Android WebFX runtime, providing direct access to the entire Android API at the same time. We could make a first prototype with the ColourfulCircles demo (just needs a Circle mapper), or the Particles demo (just needs a Canvas mapper).

- **If you would like to work on an Android mapper prototype, please [contact us][webfx-contactus].** 


## Prototyping an iOS mapper

We can't really do the same with iOS (developing in Java with Xcode), but we may still be able to realise a transpilable iOS mapper using a transpiler such as [J2ObjC](https://github.com/google/j2objc).

- **If you would like to work on an iOS mapper prototype, please [contact us][webfx-contactus].**


***


Thank you so much for joining the WebFX community!

[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[webfx-platform-repo]: https://github.com/webfx-project/webfx-platform
[webfx-maven-plugin-repo]: https://github.com/webfx-project/webfx-platform
[webfx-contactus]: mailto:maintainer@webfx.dev