package io.github.ntduycs.oss

import org.gradle.kotlin.dsl.invoke

plugins {
  id("java-library")
  id("com.diffplug.spotless")
}

spotless {
  kotlinGradle {
    target("**/*.gradle.kts")
    ktlint("${property("ktlint.version")}")
      .editorConfigOverride(mapOf("indent_size" to "2", "continuation_indent_size" to "2", "max_line_length" to "120"))
    trimTrailingWhitespace()
    endWithNewline()
  }
  java {
    importOrder()
    removeUnusedImports()
    removeWildcardImports()
    cleanthat().sourceCompatibility("24")
    eclipse()
      .sortMembersEnabled(true)
      .sortMembersVisibilityOrderEnabled(true)
    googleJavaFormat().reorderImports(false)
    formatAnnotations()
    targetExclude("**/generated/**")
  }
}

tasks.build { dependsOn(tasks.spotlessApply) }