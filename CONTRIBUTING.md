# How to contribute to WebFX

## Reporting bugs

We are aware that there are bugs in the [WebFX kit](webfx-kit), but we don't accept bug reports at the moment (issues are not open). The reason is that we have some deep work to do first (see our [roadmap](ROADMAP.md)). However, there are some exceptions:

**You can report bugs in the [WebFX CLI][webfx-cli-repo].** Issues are open for the CLI. We will do our best to fix them.

**You can report bugs in the [WebFX platform][webfx-platform-repo]**, which is our foundation layer & API for cross-platform development (it contains all Java classes whose package starts with `dev.webfx.platform`).

## Fixing bugs

If you found a bug in the [WebFX kit](webfx-kit) (this repository), either in the emulated JavaFX classes (package starting with `javafx` or `com.sun.javafx`), or in the WebFX classes (package starting with `dev.webfx.kit`), you can fork the [WebFX repository]() and try to fix it.

**We are accepting pull requests in case you submit a bug fix.**

We are also aware of a bug in the WebFX CLI (see the [Known limitation](https://github.com/webfx-project/webfx-cli#known-limitation) section). It requires some expertise in Java language parsing to be fixed.

**We would be happy to receive help to fix that [issue](https://github.com/webfx-project/webfx-cli/issues/1).**

## Extending the coverage

Extending the JavaFX coverage is not our priority at the moment for the same reason explained above. But in the same way, if you want to use a JavaFX control or feature not yet covered, you can try to fork us, add the missing OpenJFX code, and patch it to make it GWT compatible (you can look how we did with other classes).

**We are accepting pull requests in case you submit a new patched JavaFX control or feature.**

## Porting libraries

We have a dedicated space for WebFX libraries called [webfx-libs](https://github.com/webfx-libs). We started to port some JavaFX libraries to WebFX, for example the Odometer, Enzo and Medusa libraries that we used for the demos. You can use these libraries as well in your WebFX applications. But because we did a partial port (of only the features required for the demos), you may need to extend their coverage.

**We are accepting pull requests in case you submit a coverage extension of an existing WebFX library.**

If you want to use another JavaFX library which is not listed, your can try to patch it and write your own WebFX port.

**We can reference your library port in webfx-libs (please [contact us][webfx-contactus])**

## Writing the Maven plugin

We need to develop a [WebFX Maven plugin][webfx-maven-plugin-repo] to make large WebFX libraries (with many modules, like those we provide) quicker to import in WebFX applications. The feature is already implemented in the WebFX CLI, we just need to embed it in a Maven plugin.

**If you are happy to write this Maven plugin, please [contact us][webfx-contactus].**

## Designing a logo

Let's give a face to WebFX with a logo, the little touch that make any communication more professional.

**If you have any logo idea or draft, you can [send it to us][webfx-contactus], we will be delighted to consider your proposition.**

## Writing an Apple signing guide

New WebFX users with no experience in Apple development may have difficulties to build their macOS or iOS native app. It would be helpful to write a guide to assist them in the process (enrolling the Apple Developer Program, creating a Provisioning Profile, setting up Xcode,...) for a successful signing of their macOS & iOS app.

**If you are happy to write this Apple signing guide, please [contact us][webfx-contactus].**


## Writing JavaDoc

Most of the [webfx-kit](webfx-kit) doesn't require JavaDoc as its API is the JavaFX API which is already largely documented. However, the [webfx-platform][webfx-platform-repo] needs to be documented. 

**If you are happy to help with writing the JavaDoc, please [contact us][webfx-contactus].**

## Writing unit tests

Like for any long-term software, it will be necessary to write unit tests. We will start with the [webfx-platform][webfx-platform-repo].

**If you are happy to help with writing these unit tests, please [contact us][webfx-contactus].**

## Writing XML Schema

One of the next step in our [roadmap](ROADMAP.md) is to document the syntax of `webfx.xml` files. It would be helpful to also write a XSD to assist WebFX developers when they modify these  files. 

**If you are happy to help with writing the XSD, please [contact us][webfx-contactus].**


## Reviewing our code

Because we are in the incubation phase, now is the best time to review our code, especially our API (breaking changes are much less painful during that time). If you find it difficult to use, or feel it could be improved, don't hesitate to give us your feedback.

**If the code is in the [webfx-platform][webfx-platform-repo], you can open an issue.**

**If the code is in the [webfx-kit](webfx-kit), please [contact us][webfx-contactus].**

## Investigating J2CL

As mentioned in our [roadmap](ROADMAP.md), we will investigate the possibility of supporting alternative transpilers, starting with [J2CL](https://github.com/google/j2cl). If you already have some experience with J2CL and would like to help, you are very welcome.

**If you are happy to help us investigate the J2CL support, please [contact us][webfx-contactus].**

## Investigating TeaVM

In the same way, we would like to investigate [TeaVM](https://teavm.org). We already demonstrated in the Mandelbrot demo how to use TeaVM to transpile a part of the application logic in WebAssembly, but here it's about going further by transpiling the whole application.

**If you are happy to help us investigate the TeaVM support, please [contact us][webfx-contactus].**

## Preparing the Kotlin demo

The TeaVM support will open the door to alternative languages such as Kotlin. To demonstrate this, we will need to make a Kotlin demo. This demo will take a Kotlin app with a JavaFX UI and transpile it into JS with TeaVM. This Kotlin app can be prepared in advance.

**If you are happy to develop a nice Kotlin app for the demo, please [contact us][webfx-contactus].**


## Prototyping an Android mapper

This is not really planned in our roadmap, but an interesting parallel development would be to implement an Android mapper (in addition to the DOM mapper). This would let us develop Android applications directly in Android Studio with that Android WebFX runtime, giving direct access to all the Android API at the same time. We could make a first prototype with the ColourfulCircles demo (just needs a Circle mapper), or the Particles demo (just needs a Canvas mapper).

**If you are happy to work on an Android mapper prototype, please [contact us][webfx-contactus].** 

## Prototyping an iOS mapper

We can't really do the same with iOS (developing in Java with Xcode), but we may still be able to realise a transpilable iOS mapper using a transpiler such as [J2ObjC](https://github.com/google/j2objc).

**If you are happy to work on an iOS mapper prototype, please [contact us][webfx-contactus].**

[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[webfx-platform-repo]: https://github.com/webfx-project/webfx-platform
[webfx-maven-plugin-repo]: https://github.com/webfx-project/webfx-platform
[webfx-contactus]: mailto:maintainer@webfx.dev