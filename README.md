# WebFX [![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/WebFXProject.svg?style=social&label=WebFXProject)](https://twitter.com/WebFXProject)

WebFX is a JavaFX application transpiler powered by [GWT][gwt-website]. It can transpile a JavaFX application into a traditional self-contained pure JavaScript web app (with no plugin or server required for its execution in the browser).

For more explanation, please visit the [website][webfx-website].

## Demos

<div align="center">
<table>
<tr>
<td align="center"><a href="https://tallycounter.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/TallyCounter.webp"/><br/>Tally Counter</a>
</td>
<td align="center"><a href="https://moderngauge.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/ModernGauge.webp"/><br/>Modern Gauge</a></td>
<td align="center"><a href="https://enzoclocks.webfx.dev"> <img src="https://webfx-demos.github.io/webfx-demos-videos/EnzoClocks.webp"/><br/> Enzo Clocks</a></td>
<td align="center"><a href="https://fx2048.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/FX2048.webp"/><br/>FX2048</a></td>
</tr>
<tr>
<td align="center"><a href="https://spacefx.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/SpaceFX.webp"/><br/>SpaceFX</a> ♪</td>
<td align="center"><a href="https://demofx.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/DemoFX.webp"/><br/>DemoFX</a> ♪</td>
<td align="center"><a href="https://raytracer.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/RayTracer.webp"/><br/>Ray Tracer</a></td>
<td align="center"><a href="https://mandelbrot.webfx.dev"><img src="https://webfx-demos.github.io/webfx-demos-videos/Mandelbrot.GIF"/><br/>Mandelbrot</a></td>
</tr>
<tr>
<td colspan="4" align="center">
<a href="https://github.com/webfx-demos">
<img width="100%" src='https://webfx-demos.github.io/webfx-demos-videos/MoreDemos.svg'/>
</a>
</td>
</tr>
</table>
</div>

## Fully cross-platform

WebFX doesn't target only the Web. WebFX applications can be compiled to run natively on all major platforms from a single code base:

<div align="center">

| Platform                                    | 32-bit JRE | 64-bit JRE | 64-bit Native |
|---------------------------------------------|:----------:|:----------:|:-------------:|
| Desktops (Windows, macOS & Linux)           |     ✅      |     ✅      |       ✅       |
| Tablets & mobiles (Android & iOS)           |     ❌      |     ❌      |       ✅       |
| Embed (Raspberry Pi) ~ *not yet documented* |     ✅      |     ✅      |       ✅       |
| Browsers (Chrome, FireFox, Edge, etc...)    |     --     |     --     |      --       |

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

## Status

Although the project successfully passed the Proof of Concept and the prototype phases, it's still in the incubation phase. We are working to make it a Minimal Viable Product.

WebFX is not yet ready for production.
At this stage, we provide only snapshot releases, and breaking changes may occur until the first official release.
But you are very welcome to try WebFX and play with it.

## Ecosystem

In addition to the WebFX Kit, a whole ecosystem is being built for the development of complex WebFX applications, such as Enterprise level applications. This ecosystem is presented at the [WebFX Project](https://github.com/webfx-project) level.

## Roadmap

You can consult our [roadmap](docs/ROADMAP.md). Issues will be open when approaching the General Availability, and the project will have reached a more stable state.


## Keep updated [![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/WebFXProject.svg?style=social&label=WebFXProject)](https://twitter.com/WebFXProject)

You can follow us on [Twitter](https://twitter.com/WebFXProject) or subscribe to our [blog][webfx-blog] where we will post regular news and updates on the progress made.

## Get involved!

You can greatly help the project by:

- Following the [guide][webfx-guide] and start experimenting with WebFX
- Reporting any issues you may have with the [WebFX CLI][webfx-cli-repo], which we will try to fix
- Giving us feedback in our GitHub [discussions][webfx-discussions]
- Sharing your first WebFX applications (we can add it to our [demo list][webfx-demos] if you wish)

You want to get involved in the development as well? You are very welcome! Please read our [contributing guide](docs/CONTRIBUTING.md).

## License

WebFX is a free, open-source software licensed under the [Apache License 2.0](docs/LICENSE)

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-demos]: https://github.com/webfx-demos
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
