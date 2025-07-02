
<div align="center">

[![JavaFX compatible](https://img.shields.io/badge/JavaFX-compatible-brightgreen.svg)](https://openjfx.io)
[![GWT compatible](https://img.shields.io/badge/GWT-compatible-brightgreen.svg)][gwt-website]
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)][j2cl-repo]
[![Gluon compatible](https://img.shields.io/badge/Gluon-compatible-brightgreen.svg)](https://gluonhq.com)
[![Vert.x compatible](https://img.shields.io/badge/Vert.x-compatible-brightgreen.svg)][vertx-website]

<a href="https://webfx.dev"><img src="WebFX.png"/></a>
&nbsp;

[![JFXCentral](https://img.shields.io/badge/Find_me_on-JFXCentral-blue?logo=googlechrome&logoColor=white)](https://www.jfx-central.com/tools/webfx)
[![Twitter](https://img.shields.io/badge/follow-%40WebFXProject-0f80c0?logo=x)](https://twitter.com/WebFXProject) 
[![Discord](https://img.shields.io/badge/join-support_chat-0f80c0?logo=discord&logoColor=white)][webfx-discord]

<i>
<strong>⚠️ Notice (July 1st, 2025):</strong> WebFX builds are currently broken due to the OSSRH sunset and a bug in the central-publishing-maven-plugin, which prevents publishing the essential webfx.xml metadata file to the new Central Portal. We have reported this issue to central-support@sonatype.com and are awaiting a response from Sonatype.
</i>

</div>

# WebFX
WebFX is a JavaFX application transpiler powered by [GWT][gwt-website]. It can transpile a JavaFX application into a traditional self-contained pure JavaScript web app (with no plugin or server required for its execution in the browser).

For more explanation, please visit the [website][webfx-website].

## Graphical demos

<div align="center">
<table>
<tr>
<td align="center"><a href="https://tallycounter.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/TallyCounter.webp"/><br/>Tally Counter</a>
</td>
<td align="center"><a href="https://enzoclocks.webfx.dev"> <img src="https://webfx-demos.github.io/webfx-demos-videos/EnzoClocks.webp"/><br/> Enzo Clocks</a></td>
<td align="center"><a href="https://demofx.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/DemoFX.webp"/><br/>DemoFX</a> ♪</td>
<td align="center"><a href="https://moderngauge.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/ModernGauge.webp"/><br/>Modern Gauge</a></td>
</tr>
<tr>
<td align="center"><a href="https://raytracer.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/RayTracer.webp"/><br/>Ray Tracer</a></td>
<td align="center"><a href="https://mandelbrot.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/Mandelbrot.webp"/><br/>Mandelbrot</a></td>
<td align="center"><a href="https://cube.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/Cube.webp"/><br/>Cube</a></td>
<td align="center"><a href="https://spacefx.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/SpaceFX.webp"/><br/>SpaceFX</a> ♪</td>
</tr>
<tr>
<td colspan="4" align="center">
<a href="https://github.com/webfx-demos">
<img width="100%" src='MoreDemos.svg'/>
</a>
</td>
</tr>
</table>
</div>

## Enterprise demo (coming soon)

[Modality](https://github.com/modalityone/modality) is the first real-world Enterprise-level WebFX application in development. Here are a few wireframes of the back-office (WebFX will provide all the components required for these UIs):

<table>
<tr>
<td><a href="https://modality.one/wireframes/Modality-wireframe-01.png"><img src="https://modality.one/wireframes/Modality-wireframe-01-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-02.png"><img src="https://modality.one/wireframes/Modality-wireframe-02-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-03.png"><img src="https://modality.one/wireframes/Modality-wireframe-03-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-04.png"><img src="https://modality.one/wireframes/Modality-wireframe-04-thumbnail.png"/></a></td>
</tr>
<tr>
<td><a href="https://modality.one/wireframes/Modality-wireframe-05.png"><img src="https://modality.one/wireframes/Modality-wireframe-05-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-06.png"><img src="https://modality.one/wireframes/Modality-wireframe-06-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-07.png"><img src="https://modality.one/wireframes/Modality-wireframe-07-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-08.png"><img src="https://modality.one/wireframes/Modality-wireframe-08-thumbnail.png"/></a></td>
</tr>
<tr>
<td><a href="https://modality.one/wireframes/Modality-wireframe-09.png"><img src="https://modality.one/wireframes/Modality-wireframe-09-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-10.png"><img src="https://modality.one/wireframes/Modality-wireframe-10-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-11.png"><img src="https://modality.one/wireframes/Modality-wireframe-11-thumbnail.png"/></a></td>
<td><a href="https://modality.one/wireframes/Modality-wireframe-12.png"><img src="https://modality.one/wireframes/Modality-wireframe-12-thumbnail.png"/></a></td>
</tr>
</table>

## Universal Platform Compatibility

WebFX doesn't target only the Web. WebFX applications can be compiled to run natively on 7 platforms from a single code base!

<div align="center">

<table>
<tr>
<td><img src="html5.svg"/></td>
<td><img src="android.svg"/></td>
<td><img src="ios.svg"/></td>
<td><img src="windows.svg"/></td>
<td><img src="apple-dark.svg"/></td>
<td><img src="linux-dark.svg"/></td>
<td><img src="raspberry-pi.svg"/></td>
</tr>
</table>

| Platform                                    |       32 or 64-bit JRE        |         64-bit Native         |
|---------------------------------------------|:-----------------------------:|:-----------------------------:|
| Desktops (Windows, macOS & Linux)           |               ✅               |               ✅               |
| Tablets & mobiles (Android & iOS)           |               ❌               |               ✅               |
| Embed (Raspberry Pi) ~ *not yet documented* |               ✅               |               ✅               |
| Web (Chrome, FireFox, Edge, etc...)         | <img height=24 src="JS.svg"/> | <img height=24 src="JS.svg"/> |

</div>


You can check out the demos to see how a GitHub workflow can generate these executables.
For example, here is the [GitHub workflow](https://github.com/webfx-demos/webfx-demo-fx2048/blob/webfx/.github/workflows/builds.yml) for the FX2048 demo and the [executables](https://github.com/webfx-demos/webfx-demo-fx2048/releases) that it generated.

## How it works

WebFX compiles your JavaFX application together with the WebFX Kit - a GWT compatible version of OpenJFX.

<div align="center">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://docs.webfx.dev/webfx-readmes/webfx-kit-dark.svg">
      <img src="https://docs.webfx.dev/webfx-how-it-works.svg">
    </picture>
</div>

For more explanation, please read the [documentation][webfx-docs].

## Getting started

The [guide to getting started][webfx-guide] is included in the documentation.

## Ecosystem

<div align="center">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://docs.webfx.dev/webfx-readmes/webfx-project-dark.svg">
      <img src="https://docs.webfx.dev/webfx-readmes/webfx-project-light.svg" />
    </picture>
</div>

Although the WebFX Kit (this repository) is the very heart of the project, there are other important repositories that together comprise the WebFX ecosystem. More info about them is given at the [organization level](https://github.com/webfx-project).

## Status

![Sonatype](https://img.shields.io/badge/Sonatype-0.1.0--SNAPSHOT-brightgreen.svg)

WebFX is still in the incubation phase. You can consult our roadmap below. At this stage, we provide only snapshot releases, and breaking changes may occur until the first official release. 

<details>
  <summary>Roadmap</summary>

- [x] Proof of Concept
- [x] Prototype
- [x] [WebFX CLI][webfx-cli-repo]
- [x] [WebFX Demos](https://github.com/webfx-demos)
- [x] [WebFX Website][webfx-website]
- [x] [WebFX Docs][webfx-docs]
- [x] [WebFX Blog][webfx-blog]
- [ ] <img src="plot.svg"></img> ![JavaFX](https://img.shields.io/badge/JavaFX-compatible-brightgreen.svg)
    - [ ] <img src="plot.svg"></img> javafx-base ![](https://geps.dev/progress/80)
    - [ ] <img src="plot.svg"></img> javafx-graphics ![](https://geps.dev/progress/70) ⓵
    - [ ] <img src="plot.svg"></img> javafx-controls ![](https://geps.dev/progress/33) ⓶ 
    - [ ] <img src="plot.svg"></img> javafx-media ![](https://geps.dev/progress/85)
    - [ ] <img src="plot.svg"></img> javafx-web ![](https://geps.dev/progress/80)
    - [ ] <img src="plot.svg"></img> javafx-fxml ![](https://geps.dev/progress/10) ⓷
- [ ] <img src="plot.svg"></img> Language, Build & Runtime Environments
  - [x] ![GWT](https://img.shields.io/badge/GWT-compatible-brightgreen.svg)
  - [x] ![Vert.x compatible](https://img.shields.io/badge/Vert.x-compatible-brightgreen.svg) ⓼
  - [ ] <img src="plot.svg"></img> ![J2CL](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg) ![](https://geps.dev/progress/85) ⓸
  - [ ] <img src="plot.svg"></img> ![TeaVM](https://img.shields.io/badge/TeaVM-compatible-brightgreen.svg) ![](https://geps.dev/progress/20) ⓹
  - [ ] ![Kotlin compatible](https://img.shields.io/badge/Kotlin-compatible-brightgreen.svg) (via J2CL or TeaVM)
  - [ ] ![Scala](https://img.shields.io/badge/Scala-compatible-brightgreen.svg) (via TeaVM only)
  - [ ] ![JxBrowser](https://img.shields.io/badge/JxBrowser-debug-brightgreen.svg) ⓺
  - [ ] ![WebAssembly](https://img.shields.io/badge/WebAssembly-target-brightgreen.svg) (via J2CL or TeaVM)
- [ ] <img src="plot.svg"></img> Ecosystem
    - [ ] <img src="plot.svg"></img> [WebFX Platform][webfx-platform-repo] ![](https://geps.dev/progress/85)
    - [ ] <img src="plot.svg"></img> [WebFX Extras][webfx-extras-repo] ![](https://geps.dev/progress/70) ⓻
    - [ ] <img src="plot.svg"></img> [WebFX Stack][webfx-stack-repo] ![](https://geps.dev/progress/60) ⓼
    - [ ] <img src="plot.svg"></img> WebFX CSS ![](https://geps.dev/progress/75) ⓽ 
    - [x] [WebFX Libs][webfx-libs-repo] (subject to grow on demand)
- [ ] <img src="plot.svg"></img> [WebFX Maven Plugin][webfx-maven-plugin] ![](https://geps.dev/progress/40)
- [x] Enterprise application prototype
- [ ] <img src="plot.svg"></img> [Modality][modality-repo] (first Enterprise WebFX app) ![](https://geps.dev/progress/50)
- [x] CI/CD pipeline (see example of [GitHub workflow][github-workflow-example])
- [ ] GitHub workflow automatic generation by the CLI
- [ ] Unit test support in WebFX CLI
- [ ] Additional CLI features for contributors
- [ ] Syntax documentation of `webfx.xml` files
- [ ] Improvement of the OpenJFX patching process
    - [ ] Automate the process to ease a rebase on OpenJFX
    - [ ] Rebase WebFX on OpenJFX latest version
- [ ] General Availability

⓵ 100% = No 3D, no CSS. JavaFX 3D can't be easily mapped to HTML. For CSS, the WebFX approach is to rely on HTML CSS, rather than emulating JavaFX CSS in the browser. See ⓽ for more info about this approach.

⓶ WebFX supports the general javafx-controls API (allowing you to write custom controls). So far, the supported JavaFX controls are: Button, CheckBox, RadioButton, ContextMenu, Label, Hyperlink, TextField, TextArea, PasswordField, ProgressBar, Slider, ScrollPane, SplitPane & TabPane.

⓷ FXML relies on reflection, which is not supported by transpilers. Alternatively, WebFX could transform fxml files into transpilable Java code. A prototype has been made with this approach using [Memory Game](https://github.com/JaretWright/MemoryGame), a FXML-based JavaFX game (see working [demo](https://memorygame.webfx.dev)).

⓸ An effort is currently being made to make WebFX transpiler-agnostic. [J2CL][j2cl-repo] (successor of [GWT][gwt-website] in active development) will be soon supported. This future-proof step will also bring all [next goodies](https://github.com/google/j2cl/issues/93) from J2CL, such as [WebAssembly][webassembly-website] target (in addition to JavaScript) and [Kotlin][kotlin-website] language (in addition to Java). 

⓹ [TeaVM][teavm-website] is another popular transpiler. Whereas GWT & J2CL are Java-source transpilers, TeaVM is a byte-code transpiler, which will open the door to other alternative JVM languages such as [Scala][scala-website]. Also, TeaVM supports Java 21, including `record` and new `switch` expressions.

⓺ The [JxBrowser][jxbrowser-website] support will offer an external web view that developers can use to run, test and debug the web version of their WebFX app directly from Java (no transpilation required!). The IDE will simply run your app with the WebFX Kit runtime (instead of OpenJFX) in the JVM, and the DOM generated by WebFX will be rendered in JxBrowser (a Chromium-based browser controlled by Java). This will considerably speed up the development cycle of web apps.

⓻ WebFX extras provides additional features for WebFX, and simpler alternative controls to the complex JavaFX controls not yet supported (ex: simple data grid and charts).

⓼ WebFX Stack provides additional enterprise-level features such as: UI router, I18n, Validation, Auth(n/z) with SSO support, client-side ORM, DB access, websocket-based event bus (for server communication - focus on [Vert.x][vertx-website]), and server push features. WebFX Stack + Vert.x is a Java full-stack solution (similar to React + Node.js).

⓽ WebFX CSS relies on dual format: JavaFX CSS & HTML CSS. WebFX developers will need to write both formats. Although it's more work, this is the most optimised approach (emulating JavaFX CSS and loading caspian.css in the browser would be a heavy approach). WebFX CSS will provide a lighter initial CSS for JavaFX that will replace caspian, using a modern sleek minimalistic flat design. 

Some redesign work is also necessary to transit from the current patching process (which is mainly a manual patch of OpenJFX 8) to a more professional patching process with tooling allowing regular rebasing onto OpenJFX.

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[webfx-platform-repo]: https://github.com/webfx-project/webfx-platform
[webfx-extras-repo]: https://github.com/webfx-project/webfx-extras
[webfx-stack-repo]: https://github.com/webfx-project/webfx-stack
[webfx-libs-repo]: https://github.com/webfx-libs
[webfx-maven-plugin]: https://github.com/webfx-project/webfx-maven-plugin
[modality-repo]: https://github.com/modalityone/modality
[github-workflow-example]: https://github.com/webfx-demos/webfx-demo-spacefx/blob/main/.github/workflows/builds.yml
[j2cl-repo]: https://github.com/google/j2cl
[teavm-website]: https://teavm.org
[jxbrowser-website]: https://www.teamdev.com/jxbrowser
[kotlin-website]: https://kotlinlang.org
[webassembly-website]: https://webassembly.org
[scala-website]: https://www.scala-lang.org
[vertx-website]: https://vertx.io
</details>

## Support

For any questions or requests for help, feel free to open a GitHub [discussion](https://github.com/webfx-project/webfx/discussions), or use our [#general][webfx-discord] chat channel on Discord. You can open an [issue](https://github.com/webfx-project/webfx/issues) to report a bug, or request a feature.


## Keep updated

Depending on how often you would like to receive updates, you can subscribe to our [blog][webfx-blog] (RSS - low traffic), follow  [@WebFXProject][webfx-twitter] (X - medium traffic), or join our [#news][webfx-discord] channel (Discord - higher traffic).

## Get involved!

You can greatly help the project by:

- Following the [guide][webfx-guide] and start experimenting with WebFX
- Reporting any issues you may have with the [WebFX CLI][webfx-cli-repo], which we will try to fix
- Giving us feedback in our GitHub [discussions][webfx-discussions]
- Sharing your first WebFX applications (we can add it to our [demo list][webfx-demos] if you wish)

You want to get involved in the development as well? You are very welcome! Please read our [contributing guide](CONTRIBUTING.md).

## License

WebFX is a free, open-source software licensed under the [Apache License 2.0](../LICENSE)

## Supported by
[![IntelliJ IDEA logo](https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg)](https://jb.gg/OpenSourceSupport)

Special thanks to JetBrains for supporting WebFX by generously providing a free license for IntelliJ IDEA Ultimate.

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-discord]: https://discord.gg/xJcvmGvqh9
[webfx-twitter]: https://twitter.com/WebFXProject
[webfx-demos]: https://github.com/webfx-demos
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
