dependencyResolutionManagement {
  // Use Maven Central and the Gradle Plugin Portal for resolving dependencies in the shared build logic (`buildSrc`) project.
  @Suppress("UnstableApiUsage")
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "buildSrc"
