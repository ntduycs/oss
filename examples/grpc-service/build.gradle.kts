plugins {
  id("java")
  id("io.github.ntduycs.oss.dependency-resolution")
  id("io.github.ntduycs.oss.spotless")
  id("io.github.ntduycs.oss.grpc.compiler") version "0.1.0-SNAPSHOT"
}

dependencies {
  grpcContract("io.github.ntduycs.oss.proto:common:1.0.0")
  grpcContract("io.github.ntduycs.oss.proto:employee:1.0.0")

  implementation("com.google.protobuf:protobuf-java:4.32.0")
  implementation("io.grpc:grpc-protobuf:1.75.0")
  implementation("io.grpc:grpc-stub:1.75.0")
}

repositories {
  mavenCentral()
  maven {
    name = "test"
    url = uri(rootProject.layout.buildDirectory.dir("repo").get())
  }
}
