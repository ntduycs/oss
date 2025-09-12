plugins {
  id("java-library")
  id("io.github.ntduycs.oss.dependency-resolution")
  id("io.github.ntduycs.oss.spotless")
  id("io.github.ntduycs.oss.grpc.packager") version "0.1.1"
  `maven-publish`
}

publishing {
  repositories {
    // The following repository is used for testing purposes only
    // Feel free to replace it with your own repository
    maven {
      name = "test"
      url = uri(layout.buildDirectory.dir("repo").get())
    }
  }
}
