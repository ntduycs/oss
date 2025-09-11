plugins {
  id("com.github.ntduycs.oss.dependency-resolution")
  kotlin("jvm")
}

kotlin {
  jvmToolchain(24)
  compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property") }
}