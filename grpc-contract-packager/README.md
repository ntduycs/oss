# gRPC Contract Packager Plugin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?metadataUrl=https://plugins.gradle.org/m2/io/github/ntduycs/oss/io.github.ntduycs.oss.gradle.plugin/maven-metadata.xml&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/io.github.ntduycs.oss.grpc.packager)

A Gradle plugin that simplifies packaging and publishing gRPC service contracts (.proto files) as distributable
artifacts.

## Features

- Automatically packages `.proto` files into versioned ZIP archives
- Integrates with Gradle's publishing plugin for easy Maven publication
- Supports multiple service contracts in a single project
- Configurable source directory and output locations
- Generates publication metadata for Maven repositories

## Requirements

- Gradle 9.0+
- Java 21 or later
- Kotlin 2.2.0 or later (for build configuration)

## Installation

### Using the plugins DSL

```kotlin
plugins {
  id("io.github.ntduycs.oss.grpc.packager") version "<version>"
}
```

### Using legacy plugin application

```kotlin
buildscript {
  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("io.github.ntduycs.oss:grpc-contract-packager:<version>")
  }
}

apply(plugin = "io.github.ntduycs.oss.grpc.packager")
```

## Usage

1. Place your `.proto` files in the default directory structure:

    ```
    proto/
    ├── service1/    # Contains .proto files for service1
    │   └── v1/      # Optional (but recommended) version directory
    │       └── *.proto
    └── service2/    # Contains .proto files for service2
    ```

2. Configure the plugin in your `build.gradle.kts` (not supported yet)

## Tasks

The plugin creates the following tasks:

- `package[ServiceName]GrpcContract` - Packages the proto files for each specific service
- `publish` - Publishes all contract packages (requires Maven publish plugin)

## Publishing

The plugin integrates with Gradle's Maven Publish plugin. To publish your contracts:

```kotlin
publishing {
  repositories {
    maven {
      name = "myMavenRepo"
      url = uri("https://maven.pkg.github.com/your-org/your-repo")
      credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
        password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
```

## Contribution

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0.