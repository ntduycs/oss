# gRPC Contract Example

This example demonstrates how to use the `grpc-contract-packager` plugin to package and publish gRPC service contracts.

## Project Structure

```
proto/
├── common/                  # Common protobuf definitions
│   └── error/               # Error-related definitions
│       └── v1/
│           └── error.proto  # Standardized error response
├── employee/               # Employee service related definitions
│    └── department/               
│       └── v1/
│           └── department.proto # For models
│           └── department_service.proto # For servides/APIs
```

## Prerequisites

- Java 21 or later
- Gradle 9.0+

## Setup

1. Add the plugin to your `build.gradle.kts`:

    ```kotlin
    plugins {
        id("java-library")
        id("io.github.ntduycs.oss.grpc.packager") version "<version>"
        `maven-publish`
    }
    ```

2. Configure the publishing repository:

    ```kotlin
    publishing {
        repositories {
            maven {
                name = "test"
                url = uri(layout.buildDirectory.dir("repo").get())
            }
        }
    }
    ```

3. For each subdirectory under `proto`, add a corresponding entry to your `gradle.properties`, for example:

    ```properties
   contract.common.version=1.0.0
   contract.employee.version=1.0.0
   ```

   The `grpc-contract-packager` use Gradle properties to resolve the version of each contract.

## Building and Publishing

To build and publish the contract package:

```bash
./gradlew build publish
```

This will:

1. Check on your `.proto` files to ensure coding conventions are followed
2. Package them into versioned ZIP archives
3. Publish to the configured Maven repository

## Using the Published Contract

Other projects can depend on the published contract using standard Maven coordinates:

```kotlin
plugins {
  id("io.github.ntduycs.oss.grpc.compiler") version "<version>"
}

dependencies {
  grpcContract("com.example:grpc-contracts:1.0.0")
}
```

## Customization

TBD.

## License

This example is licensed under the Apache License 2.0.
