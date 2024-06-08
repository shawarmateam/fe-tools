# FilesEngine (fe-tools)

FilesEngine is a simple tool designed to create 2D games. It's mostly a tool for creating games.

## Features

- This engine use `LWJGL` library.
- Files Engine also have a custom `ECS` *(Entity Component System)* with division into components and scripts.
- Each component has its own *script* class with name `<CompName>Scripts`.

## Getting Started

To get started with FilesEngine, download this repository and create an artifact with main class `App` if you want build for test start.
Or with main class `SceneManager` if you want to build *Scene redactor*.

## Mini-Documentation

### How to create custom script

1. Create script in `src/main/java`.
2. Add `extends FilesScripts` to your script class.
3. Override `start()` & `update(float dt)` methods.
4. In file `assets/scripts.json` write in `SCRs` your script link *example: (com.example.UrScript)*.

You can see test script here: [src/main/java/testscrgl.java](https://github.com/adisteyf/fe-tools/blob/main/src/main/java/testscrgl.java).

### How to add any component to auto update

1. Write *scripts* class of your component in file `assets/scripts.json` in `Comps`.
2. That's all!

## Contributing

We welcome contributions from the community to help improve FilesEngine. If you have any suggestions, bug reports, or feature requests, please feel free to submit them through the issue tracker.

## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/adisteyf/FilesEngine/blob/main/LICENSE) file for details.
