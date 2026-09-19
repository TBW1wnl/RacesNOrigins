# RacesNOrigins

A Minecraft mod that adds races and classes to players (Origins-like gameplay), built for **Fabric** and **NeoForge** from a shared codebase using the [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template).

## Project layout

- `common` — shared code, compiled against vanilla Minecraft only. No Fabric/NeoForge APIs here. This is where the bulk of the mod (races, classes, abilities, data) should live.
- `fabric` — Fabric-specific entry point and platform implementation.
- `neoforge` — NeoForge-specific entry point and platform implementation.
- `build-logic` — shared Gradle conventions used by the subprojects.

Code that needs something loader-specific (events, APIs) goes through the `IPlatformHelper` service abstraction defined in `common` and implemented per-loader in `fabric`/`neoforge`.

## Getting started

### IntelliJ IDEA

1. Open the project root (the folder containing this README and `gradlew`) as a new project in IDEA.
2. Requires Java 25. If your default JDK isn't 25, set it under `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM`, and set the Project SDK under `File > Project Structure > Project SDK`.
3. Reload the Gradle project.
4. Run configurations for Fabric (Gradle tasks) and NeoForge (Application) clients will appear once the project is synced.

### Command line

```bash
./gradlew build
```

Run a Fabric client:

```bash
./gradlew :fabric:runClient
```

Run a NeoForge client:

```bash
./gradlew :neoforge:runClient
```

## Configuration

Project metadata (mod id, name, version, Minecraft version, loader versions, license, description) is centralized in [gradle.properties](gradle.properties).
