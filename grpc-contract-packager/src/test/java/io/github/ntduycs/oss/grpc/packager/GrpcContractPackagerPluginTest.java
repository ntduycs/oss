package io.github.ntduycs.oss.grpc.packager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GrpcContractPackagerPluginTest {
  private final String taskName = "packageTestGrpcContract";

  @TempDir File testProjectDir;

  @Test
  public void givenPluginApplied_WhenRunningPackageTask_ThenTaskSucceeds() {
    // Run the package task
    var runner =
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(taskName, "--stacktrace")
            .withDebug(true);

    var result = runner.build();

    // Verify the task was successful
    assertNotNull(result.task(":" + taskName));
    assertEquals(
        TaskOutcome.SUCCESS,
        Objects.requireNonNull(result.task(":" + taskName)).getOutcome(),
        "Package task should succeed");

    // Verify the zip file was created
    var zipFile = new File(testProjectDir, "build/libs/test.zip");
    assertTrue(zipFile.exists(), "ZIP file should be created");
  }

  @Test
  public void givenPluginApplied_WhenRunningTasks_ThenPackageTaskIsCreated() {
    // Run the tasks
    var runner =
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("tasks", "--all", "--stacktrace")
            .withDebug(true);

    var result = runner.build();

    // Verify the task was created
    assertTrue(result.getOutput().contains(taskName), "Package task should be created");
  }

  @BeforeEach
  public void setup() throws IOException {
    // Create build.gradle.kts
    var buildFile = new File(testProjectDir, "build.gradle.kts");

    // Create settings.gradle.kts
    var settingsFile = new File(testProjectDir, "settings.gradle.kts");
    writeFile(
        settingsFile,
        """
				rootProject.name = "test-project"
				includeBuild("../..") {
				    dependencySubstitution {
				        substitute(module("io.github.ntduycs.oss:grpc-contract-packager")).using(project(":"))
				    }
				}
				""");

    // Create proto directory structure
    var protoDir = new File(testProjectDir, "proto/test/v1");
    assertTrue(protoDir.mkdirs());

    // Create a test proto file
    var protoFile = new File(protoDir, "test.proto");
    var protoContent =
        """
				syntax = "proto3";

				package test.v1;

				service TestService {
				    rpc SayHello (HelloRequest) returns (HelloResponse);
				}

				message HelloRequest {
				    string name = 1;
				}

				message HelloResponse {
				    string message = 1;
				}""";
    writeFile(protoFile, protoContent);

    // Create gradle.properties file
    var gradleProps = new File(testProjectDir, "gradle.properties");
    writeFile(gradleProps, "contract.test.version=1.0.0");

    // Write build.gradle.kts
    var buildFileContent =
        """
				plugins {
				    id("io.github.ntduycs.oss.grpc.packager")
				    `maven-publish`
				}

				group = "com.example.test"
				version = "1.0.0"

				publishing {
				    repositories {
				        maven {
				            name = "test"
				            url = uri("${'$'}{buildDir}/repo")
				        }
				    }
				}
				""";
    writeFile(buildFile, buildFileContent);
  }

  private void writeFile(File destination, String content) throws IOException {
    try (var writer = new FileWriter(destination)) {
      writer.write(content);
    }
  }
}
