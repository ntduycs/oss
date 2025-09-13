# gRPC Contract Packager Plugin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?metadataUrl=https://plugins.gradle.org/m2/io/github/ntduycs/oss/io.github.ntduycs.oss.gradle.plugin/maven-metadata.xml&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/io.github.ntduycs.oss.grpc.packager)

A Gradle plugin that simplifies packaging and publishing gRPC service contracts (`.proto` files) as versioned, distributable artifacts. This plugin is particularly useful for teams that need to share gRPC service definitions across multiple services or teams.

## Features

- 🚀 Automatically packages `.proto` files into versioned ZIP archives for distribution
- 🔌 Seamless integration with Gradle's Maven Publish plugin
- 📦 Supports multiple service contracts in a single project
- ⚙️ Configurable source directories and output locations __(Not available yet)__
- 📝 Generates complete publication metadata for Maven repositories
- 🔍 Supports Buf validation for protocol buffer files
- 🎯 Centrally handles versioning based on your project properties
- 🔄 Compatible with both single and multi-module Gradle projects

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
    classpath("io.github.ntduycs.oss.grpc.packager:io.github.ntduycs.oss.grpc.packager.gradle.plugin:<version>")
  }
}

apply(plugin = "io.github.ntduycs.oss.grpc.packager")
```

## Configuration

TBD.

## Directory Structure

Each service contracts are required to be placed in a separate directory with the following structure:

```
proto/
├── service1/         # Service name (e.g., "employee")
│   └── v1/           # API version (required)
│       ├── domain_service.proto # RPC and their request/response model definitions
│       └── domain.proto # Domain models
└── common/           # Common definitions
    ├── error/
    │   └── v1/
    │       └── error.proto
    └── types/
        └── v1/
            └── types.proto
```

## Buf Integration

The plugin works well with Buf for protocol buffer linting and breaking change detection. Make sure to include a `buf.yaml` in your project root:

```yaml
# For details on buf.yaml configuration, visit https://buf.build/docs/configuration/v2/buf-yaml
version: v2
modules:
  - path: build/extracted-proto
lint:
  use:
    - STANDARD
    - UNARY_RPC
    - COMMENT_FIELD
    - COMMENT_RPC
  ignore:
    - PACKAGE_VERSION_SUFFIX
  rpc_allow_google_protobuf_empty_requests: true
  rpc_allow_google_protobuf_empty_responses: true
deps:
  - buf.build/googleapis/googleapis
  - buf.build/bufbuild/protovalidate
breaking:
  use:
    - FILE
    - WIRE
    - WIRE_JSON
```

## Tasks

The plugin creates the following tasks:

- `package[ServiceName]GrpcContract` - Packages the proto files for each specific service
- `publish` - Publishes all contract packages (requires Maven publish plugin)

Besides, by applying `build.buf` plugin, the plugin will create the following tasks:

- `bufFormatApply` - Formats a Protobuf schema according to Buf's formatting rules.
- `bufFormatCheck` - Checks that a Protobuf schema is formatted according to Buf's formatting rules.
- `bufLint` - Checks that a Protobuf schema conforms to the Buf lint configuration.

## Publishing

The plugin automatically configures the necessary publications when the `maven-publish` plugin is applied. You are free to define your own publishing repositories.

```kotlin
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/your-org/your-repo")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

## Consuming the Published Artifacts

Other projects can depend on the published contracts using standard Maven coordinates:

```kotlin
plugins {
  id("io.github.ntduycs.oss.grpc.compiler")
}

dependencies {
    grpcContract("com.example:grpc-contracts:1.0.0")
}
```

## Example Project

For a complete working example, see the [example project](https://github.com/ntduycs/oss/tree/main/examples/grpc-contract) in the repository.

## Compatibility

This plugin is designed to work with the [gRPC Contract Compiler](https://github.com/ntduycs/oss/tree/main/grpc-contract-compiler) plugin, which extract gRPC contracts from ZIP archives and generate gRPC stubs.

## Contribution

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](../LICENSE) file for details.