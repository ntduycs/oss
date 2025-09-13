# gRPC Contract Compiler Plugin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?metadataUrl=https://plugins.gradle.org/m2/io/github/ntduycs/oss/io.github.ntduycs.oss.gradle.plugin/maven-metadata.xml&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/io.github.ntduycs.oss.grpc.compiler)

A Gradle plugin that simplifies the extraction of gRPC contract files from ZIP archives and generates the necessary gRPC stubs for your project. This plugin works hand-in-hand with the gRPC Contract Packager to provide a complete solution for managing gRPC service contracts.

## Features

- 🚀 Automatically extracts `.proto` files from versioned ZIP archives
- 🔌 Seamless integration with Gradle's dependency management
- ⚡ Generates gRPC stubs for Java projects
- 🔍 Supports Buf for protocol buffer validation and generation
- 🔄 Compatible with both single and multi-module Gradle projects
- 🎯 Automatically configures source sets for generated code

## Requirements

- Gradle 9.0+
- Java 21 or later
- Kotlin 2.2.0 or later (for build configuration)

## Installation

### Using the plugins DSL

```kotlin
plugins {
  id("io.github.ntduycs.oss.grpc.compiler") version "<version>"
}
```

### Using legacy plugin application

```kotlin
buildscript {
  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("io.github.ntduycs.oss.grpc.compiler:io.github.ntduycs.oss.grpc.compiler.gradle.plugin:<version>")
  }
}

apply(plugin = "io.github.ntduycs.oss.grpc.compiler")
```

## Usage

### Basic Configuration

1. Add the plugin to your `build.gradle.kts`:

    ```kotlin
    plugins {
        id("io.github.ntduycs.oss.grpc.compiler") version "<version>"
    }
    ```

2. Add your gRPC contract dependencies:

    ```kotlin
    dependencies {
        grpcContract("com.example:grpc-contracts:1.0.0")
    }
    ```

### Advanced Configuration

TBD.

### Buf Integration

The plugin integrates with Buf for protocol buffer validation and code generation. Make sure to include a `buf.gen.yaml` in your project root:

```yaml
version: v2
managed:
  enabled: true
  disable:
    - module: buf.build/googleapis/googleapis
    - module: buf.build/bufbuild/protovalidate
  override:
    - file_option: java_package_prefix
      value: io.github.ntduycs.oss.example.grpc
    - file_option: java_multiple_files
      value: true
plugins:
  - remote: buf.build/protocolbuffers/java:v31.1
    out: sources/grpc/java/main
    include_imports: true
  - remote: buf.build/grpc/java:v1.72.0
    out: sources/grpc/java/main
    opt: "@generated=omit"
inputs:
  - directory: build/extracted-proto
```

## Tasks

The plugin creates the following tasks:

- `extract[ServiceName]GrpcContracts` - Extracts `.proto` files from all gRPC contract dependencies
- `bufGenerate` - Generate gRPC stubs from the extracted `.proto` files

## Example

For a complete working example, see the [example project](https://github.com/ntduycs/oss/tree/main/examples/grpc-service) in the repository.

## Compatibility

This plugin is designed to work with the [gRPC Contract Packager](https://github.com/ntduycs/oss/tree/main/grpc-contract-packager) plugin, which packages gRPC contracts into distributable ZIP archives.

## Contribution

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](../LICENSE) file for details.
