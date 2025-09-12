plugins {
  id("io.github.ntduycs.oss.dependency-resolution")
  id("io.github.ntduycs.oss.spotless")
  `java-gradle-plugin`
  `maven-publish`
  id("com.gradle.plugin-publish") version "2.0.0"
}

gradlePlugin {
  website = "https://github.com/ntduycs/oss"
  vcsUrl = "https://github.com/ntduycs/oss.git"
  plugins {
    create("grpcPackager") {
      id = "io.github.ntduycs.oss.grpc.packager"
      implementationClass = "io.github.ntduycs.oss.grpc.packager.GrpcContractPackagerPlugin"
      displayName = "gRPC Contract Packager"
      description = " A plugin to package gRPC contract into zip file that make it easy to distribute"
      version = property("oss.plugin.grpc-packager.version") as String
      tags = listOf("grpc", "packager", "contract")
    }
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(24))
  }
}

dependencies {
  implementation("build.buf:build.buf.gradle.plugin:${property("buf.version")}")

  // Test dependencies
  testImplementation(gradleTestKit())

  // JUnit Jupiter API and TestEngine implementation
  testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit-jupiter.version")}")
  testImplementation("org.junit.jupiter:junit-jupiter-params:${property("junit-jupiter.version")}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit-jupiter.version")}")

  // JUnit Platform dependencies
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testRuntimeOnly("org.junit.platform:junit-platform-runner")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
  maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

publishing {
  repositories {
    mavenLocal()
  }
}
