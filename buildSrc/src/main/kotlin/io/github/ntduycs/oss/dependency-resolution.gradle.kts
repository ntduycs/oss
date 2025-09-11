package io.github.ntduycs.oss

plugins {
  id("java-library")
}

configurations.all {
  resolutionStrategy.eachDependency {
    when (requested.group) {
      "org.jetbrains.kotlin" -> useVersion(property("kotlin.version") as String)
    }
  }

  // In favor of JUnit 5
  exclude(group = "junit", module = "junit")
}
