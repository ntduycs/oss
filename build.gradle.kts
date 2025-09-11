plugins {
  kotlin("jvm")
}

kotlin {
  jvmToolchain(24)
  compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property") }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlin.version")}")
}