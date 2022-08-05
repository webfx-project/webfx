# WebFX

WebFX is a JavaFX application transpiler powered by [GWT][gwt-website]. It can transpile a JavaFX application into a traditional self-contained pure JavaScript web app (with no plugin or server required for its execution in the browser).

<p align="center">
  <img src="https://docs.webfx.dev/webfx-how-it-works.svg" />
</p>

For more explanation, please visit the [website][webfx-website] and read the [documentation][webfx-docs].

## Fully cross-platform

WebFX doesn't target only the Web. WebFX applications can be compiled to run natively on all major platforms from a single code base:

- [x] Desktop (Windows, macOS & Linux) - 64-bit support for native, 32/64-bit support for embed JRE
- [x] Tablet & mobiles (Android & iOS) - 64-bit support for native, no 32-bit support
- [x] Browser (Chrome, FireFox, Edge, etc...)
- [ ] (not yet documented) Embed (Raspberry Pi) - 64-bit support for native, 32/64-bit support for embed JRE

You can check out the demos to see how a GitHub workflow can generate these executables.
For example, here is the [GitHub workflow](https://github.com/webfx-demos/webfx-demo-fx2048/blob/main/.github/workflows/builds.yml) for the FX2048 demo and the [executables](https://github.com/webfx-demos/webfx-demo-fx2048/releases) that it generated.

## Ecosystem

In addition to this repository, which is the very heart of WebFX, a whole ecosystem is being built for the development of complex WebFX applications, such as Enterprise level applications. This ecosystem is presented at the [WebFX Project](https://github.com/webfx-project/.github/tree/ecosystem/profile) level.

## Live demos

We have a dedicated space for the [demos][webfx-demos]. You will find all the demos presented on the website, plus a couple of additional live demos, with their source code and GitHub workflow.

## Getting started

The [guide to getting started][webfx-guide] is included in the documentation.

## Status

Although the project successfully passed the Proof of Concept and the prototype phases, it's still in the incubation phase. We are working to make it a Minimal Viable Product.

WebFX is not yet ready for production.
At this stage, we provide only snapshot releases, and breaking changes may occur until the first official release.
But you are very welcome to try WebFX and play with it.

## Roadmap

You can consult our [roadmap](ROADMAP.md). Issues will be open when approaching the General Availability, and the project will have reached a more stable state.


## Keep updated

You can follow us by subscribing to our [blog][webfx-blog] where we will post regular news and updates on the progress made.

## Get involved!

You can greatly help the project by:

- Following the [guide][webfx-guide] and start experimenting with WebFX
- Reporting any issues you may have with the [WebFX CLI][webfx-cli-repo], which we will try to fix
- Starring the project if you like it
- Sharing the [website][webfx-website] with people who might be interested
- Giving us feedback in our GitHub [discussions][webfx-discussions]
- Sharing your first WebFX applications (we can add it to our [demo list][webfx-demos] if you wish)

You want to get involved in the development as well? You are very welcome! Please read our [contributing guide](CONTRIBUTING.md).

## History

If you are interested, here is a brief [history](HISTORY.md) of the project.

## License

WebFX is a free, open-source software licensed under the [Apache License 2.0](LICENSE)

[webfx-website]: https://webfx.dev
[webfx-docs]: https://docs.webfx.dev
[webfx-demos]: https://github.com/webfx-demos
[webfx-guide]: https://docs.webfx.dev/#_getting_started
[webfx-blog]: https://blog.webfx.dev
[webfx-discussions]: https://github.com/webfx-project/webfx/discussions
[webfx-contact]: mailto:maintainer@webfx.dev
[webfx-cli-repo]: https://github.com/webfx-project/webfx-cli
[gwt-website]: http://www.gwtproject.org
