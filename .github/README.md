
<div align="center">

![Scc Code Count Badge](https://sloc.xyz/github/webfx-project/webfx/?category=code)
![Scc Blanks Count Badge](https://sloc.xyz/github/webfx-project/webfx/?category=blanks)
![Scc Comments Count Badge](https://sloc.xyz/github/webfx-project/webfx/?category=comments)
![Scc Lines Count Badge](https://sloc.xyz/github/webfx-project/webfx/?category=lines)
![Scc COCOMO Badge](https://sloc.xyz/github/webfx-project/webfx/?category=cocomo)
![Discover Vulnerabilities Using CodeQL](https://github.com/webfx-project/webfx/actions/workflows/discover-vulnerabilities.yml/badge.svg)
![Deploy to Sonatype](https://github.com/webfx-project/webfx/actions/workflows/build-and-deploy-to-sonatype.yml/badge.svg)

<a href="https://webfx.dev"><img src="WebFX.png"/></a>
&nbsp;

[![JFXCentral](https://img.shields.io/badge/Find_me_on-JFXCentral-blue?logo=googlechrome&logoColor=white)](https://www.jfx-central.com/tools/webfx)
[![Twitter](https://img.shields.io/badge/follow-%40WebFXProject-0f80c0?logo=twitter)](https://twitter.com/WebFXProject) 
[![Discord](https://img.shields.io/badge/join-WebFX_chat-0f80c0?logo=discord&logoColor=white)](https://discord.gg/7FQh3S5S)

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
For example, here is the [GitHub workflow](https://github.com/webfx-demos/webfx-demo-fx2048/blob/main/.github/workflows/builds.yml) for the FX2048 demo and the [executables](https://github.com/webfx-demos/webfx-demo-fx2048/releases) that it generated.

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
- [ ] <img src="plot.svg"></img> JavaFX coverage
    - [ ] <img src="plot.svg"></img> javafx-base ![](https://geps.dev/progress/80)
    - [ ] <img src="plot.svg"></img> javafx-graphics ![](https://geps.dev/progress/70)
    - [ ] <img src="plot.svg"></img> javafx-controls ![](https://geps.dev/progress/33)
    - [ ] <img src="plot.svg"></img> javafx-media ![](https://geps.dev/progress/85)
    - [ ] <img src="plot.svg"></img> javafx-web ![](https://geps.dev/progress/80)
    - [ ] <img src="plot.svg"></img> javafx-fxml ![](https://geps.dev/progress/10)
- [ ] <img src="plot.svg"></img> Ecosystem
    - [ ] <img src="plot.svg"></img> [WebFX Platform][webfx-platform-repo] ![](https://geps.dev/progress/85)
    - [ ] <img src="plot.svg"></img> [WebFX Extras][webfx-extras-repo] ![](https://geps.dev/progress/70)
    - [ ] <img src="plot.svg"></img> [WebFX Stack][webfx-stack-repo] ![](https://geps.dev/progress/60)
    - [x] [WebFX Libs][webfx-libs-repo] (subject to grow on demand)
- [ ] <img src="plot.svg"></img> Build & Runtime Environments
    - [x] [GWT][gwt-website] support
    - [ ] <img src="plot.svg"></img> [J2CL][j2cl-repo] support ![](https://geps.dev/progress/30)
    - [ ] <img src="plot.svg"></img> [TeaVM][teavm-website] support ![](https://geps.dev/progress/20)
    - [ ] [Kotlin][kotlin-website] support
    - [ ] [JxBrowser][jxbrowser-website] support
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

An effort is currently being made to make WebFX transpiler-agnostic. J2CL & TeaVM will be supported in a near future. J2CL (successor of GWT in active development) is not only targeting JavaScript but also WebAssembly, which should boost the performance of WebFX apps. Whereas GWT & J2CL are Java-source transpilers, TeaVM is a byte-code transpiler, which will open the door to alternative JVM languages such as Kotlin. Also, TeaVM supports Java 21, including `record` and new `switch` expressions.

The JxBrowser support will offer a web view that developers can use to run, test and debug the web version of their WebFX app directly from Java (no traspilation required). The IDE will simply run your app with the WebFX runtime (instead of OpenJFX) in the JVM, and the DOM generated by WebFX will be rendered in JxBrowser (a Chromium-based browser controlled by Java). This will considerably speed up the development cycle of web apps.

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
[gwt-website]: https://gwtproject.org
[j2cl-repo]: https://github.com/google/j2cl
[teavm-website]: https://teavm.org
[jxbrowser-website]: https://www.teamdev.com/jxbrowser
[kotlin-website]: https://kotlinlang.org

</details>

## Support

For any questions or requests for help, feel free to open a GitHub [discussion](https://github.com/webfx-project/webfx/discussions), or chat with us on [Discord](https://discord.gg/7FQh3S5S). You can open an issue to report a bug, or request a feature.


## Keep updated

You can subscribe to our [blog][webfx-blog] (low traffic), or follow us on [Twitter](https://twitter.com/WebFXProject) (medium traffic), or on [Discord](https://discord.gg/7FQh3S5S) (higher traffic).

## Get involved!

You can greatly help the project by:

- Following the [guide][webfx-guide] and start experimenting with WebFX
- Reporting any issues you may have with the [WebFX CLI][webfx-cli-repo], which we will try to fix
- Giving us feedback in our GitHub [discussions][webfx-discussions]
- Sharing your first WebFX applications (we can add it to our [demo list][webfx-demos] if you wish)

You want to get involved in the development as well? You are very welcome! Please read our [contributing guide](CONTRIBUTING.md).

## License

WebFX is a free, open-source software licensed under the [Apache License 2.0](../LICENSE)

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-demos]: https://github.com/webfx-demos
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
