plugins {
  // The Kotlin DSL plugin provides a convenient way to develop convention plugins.
  // Convention plugins are located in `src/main/kotlin`, with the file extension `.gradle.kts`,
  // and are applied in the project's `build.gradle.kts` files as required.
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
  id("com.diffplug.spotless") version "7.2.1"
}

kotlin {
  jvmToolchain(24)
  compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property") }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin.version")}")
  implementation("com.diffplug.spotless:spotless-plugin-gradle:${property("spotless.version")}")
}

spotless {
  kotlinGradle {
    target("**/*.gradle.kts")
    ktlint("${property("ktlin.version")}")
      .editorConfigOverride(mapOf("indent_size" to "2", "continuation_indent_size" to "2", "max_line_length" to "120"))
    trimTrailingWhitespace()
    endWithNewline()
  }
}

tasks.build { dependsOn(tasks.spotlessApply) }
