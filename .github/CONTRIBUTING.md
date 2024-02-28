# How to contribute to WebFX

Thank you so much for considering contributing to WebFX! There are many areas where help is needed, and we have listed some suggestions below, but we are also open to your own suggestions.


## Reporting bugs

- **You can report bugs found in the [WebFX Kit](../webfx-kit).** Issues are open, and we will do our best to fix them.
- **You can report bugs found in the [WebFX CLI][webfx-cli-repo]** as well.
- **You can report bugs found in the [WebFX Platform][webfx-platform-repo]**, which is our foundation layer & API for cross-platform development (it contains all Java classes whose package starts with `dev.webfx.platform`).


## Fixing bugs

If you found a bug in the [WebFX Kit](../webfx-kit) (in this repository), either in the emulated JavaFX classes (package starting with `javafx` or `com.sun.javafx`), or in the WebFX classes (package starting with `dev.webfx.kit`), you can fork [WebFX](https://github.com/webfx-project/webfx) and try to fix it.

- **We are accepting pull requests should you wish to submit a bug fix from your fork.**

We are also aware of a bug in the WebFX CLI (see the [Known limitation](https://github.com/webfx-project/webfx-cli#known-limitation) section), requiring some expertise in Java language parsing to be fixed.

- **We would be happy to receive help to fix the corresponding [issue](https://github.com/webfx-project/webfx-cli/issues/1).**


## Extending the JavaFX coverage

If you want to use a JavaFX control or feature that is not yet covered, please feel free to fork, add the missing OpenJFX code, and patch it to make it GWT compatible (you can look to see how we did this with other classes).

- **We are accepting pull requests should you wish to submit your fix.**


## Porting JavaFX libraries

We have a dedicated space for WebFX libraries called [WebFX Libs](https://github.com/webfx-libs). We started to port some JavaFX libraries to WebFX, for example the Odometer, Enzo and Medusa libraries that we used for the demos. You can use these libraries as well in your WebFX applications. But because we did a partial port (of only the features required for the demos), you may need to extend their coverage.

- **We are accepting pull requests should you wish to submit a coverage extension of an existing WebFX library.**

If you want to use another JavaFX library which is not listed, you can try to patch it and write your own WebFX port.

- **We can then reference your library port in WebFX Libs (please [contact us][webfx-contactus])**


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

***

Thank you so much for joining the WebFX community!

[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[webfx-platform-repo]: https://github.com/webfx-project/webfx-platform
[webfx-maven-plugin-repo]: https://github.com/webfx-project/webfx-platform
[webfx-contactus]: mailto:maintainer@webfx.dev