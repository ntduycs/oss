# OSS Projects

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A collection of open-source projects and Gradle plugins for modern Java/Kotlin development, with a focus on gRPC service development and contract management.

## 📦 Projects

### 🔄 gRPC Contract Management Suite

A pair of Gradle plugins that work together to streamline gRPC service development:

#### 1. 🚀 gRPC Contract Packager
Package and version your gRPC service contracts (`.proto` files) as distributable artifacts.

[📖 Documentation](grpc-contract-packager/README.md) | 
[📦 Gradle Plugin](https://plugins.gradle.org/plugin/io.github.ntduycs.oss.grpc.packager)

#### 2. 🔌 gRPC Contract Compiler
Easily consume and generate gRPC stubs from versioned contract packages in your service implementations.

[📖 Documentation](grpc-contract-compiler/README.md) | 
[📦 Gradle Plugin](https://plugins.gradle.org/plugin/io.github.ntduycs.oss.grpc.compiler)

### 🚧 More Coming Soon
Additional tools and plugins for modern service development will be added in the future.

## 🛠 Development

### Prerequisites
- Java 21 or later
- Gradle 9.0+
- Kotlin 2.2.0+ (for build configuration)
- Buf CLI (for protocol buffer tooling)

### Building

### Quick Start

1. **Install Buf CLI** (if not already installed):
   ```bash
   # MacOS
   brew install bufbuild/buf/buf
   
   # Or using curl
   curl -sSL https://github.com/bufbuild/buf/releases/latest/download/install.sh | sh -s -- -b $(go env GOPATH)/bin
   ```

2. **Build all projects**:
   ```bash
   ./gradlew build
   ```

3. **Build a specific project**:
   ```bash
   ./gradlew :grpc-contract-packager:build
   # or
   ./gradlew :grpc-contract-compiler:build
   ```

4. **Publish to Maven Local** (for testing):
   ```bash
   ./gradlew publishToMavenLocal
   ```

## 🤝 Contributing

I welcome contributions to improve these tools! Here's how you can help:

1. **Report Issues**: Found a bug? Have a feature request? Let us know by [opening an issue](https://github.com/ntduycs/oss/issues).

2. **Submit Code Changes**:
   - Fork the repository
   - Create your feature branch (`git checkout -b feature/AmazingFeature`)
   - Commit your changes (`git commit -m 'Add some AmazingFeature'`)
   - Push to the branch (`git push origin feature/AmazingFeature`)
   - Open a Pull Request

3. **Improve Documentation**: Better documentation is always welcome! Feel free to improve our READMEs, add examples, or enhance our API documentation.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📚 Examples

Check out our example projects to see these plugins in action:

- [gRPC Contract Example](examples/grpc-contract) - Example of packaging gRPC contracts
- [gRPC Service Example](examples/grpc-service) - Example of consuming and using gRPC contracts

## 📬 Contact

- GitHub: [@ntduycs](https://github.com/ntduycs)
- Issues: [GitHub Issues](https://github.com/ntduycs/oss/issues)
