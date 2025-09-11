package io.github.ntduycs.oss.grpc.compiler;

import static build.buf.gradle.FormatConfigurationKt.BUF_FORMAT_CHECK_TASK_NAME;
import static build.buf.gradle.GenerateConfigurationKt.BUF_GENERATE_TASK_NAME;
import static build.buf.gradle.LintConfigurationKt.BUF_LINT_TASK_NAME;
import static io.github.ntduycs.oss.grpc.compiler.GrpcContractCompilerPlugin.BUF_GENERATED_DIR;
import static io.github.ntduycs.oss.grpc.compiler.GrpcContractCompilerPlugin.GRPC_CONTRACT_CONFIGURATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GrpcContractCompilerPluginTest {
  private static final String COMPILER_PLUGIN_ID = "io.github.ntduycs.oss.grpc.compiler";

  private Project consumerProject;

  @TempDir private File projectDir;

  @Test
  public void givenPlugin_whenApply_thenConfigurationCreated() {
    // Given
    consumerProject =
        ProjectBuilder.builder()
            .withProjectDir(new File(projectDir, "consumer"))
            .withName("contract")
            .build();
    consumerProject.getPluginManager().apply("java");
    consumerProject.getPluginManager().apply(COMPILER_PLUGIN_ID);

    // Then
    assertTrue(
        consumerProject.getConfigurations().getNames().contains(GRPC_CONTRACT_CONFIGURATION));
  }

  @Test
  public void givenPlugin_whenApply_thenPluginFound() {
    // Given
    consumerProject =
        ProjectBuilder.builder()
            .withProjectDir(new File(projectDir, "consumer"))
            .withName("contract")
            .build();
    consumerProject.getPluginManager().apply("java");
    consumerProject.getPluginManager().apply(COMPILER_PLUGIN_ID);

    // Then
    assertTrue(consumerProject.getPluginManager().hasPlugin(COMPILER_PLUGIN_ID));
    assertTrue(consumerProject.getPluginManager().hasPlugin("build.buf"));
  }

  @Test
  public void givenPlugin_whenApply_thenSourceSetConfigured() {
    // Given
    consumerProject =
        ProjectBuilder.builder()
            .withProjectDir(new File(projectDir, "consumer"))
            .withName("contract")
            .build();
    consumerProject.getPluginManager().apply("java");
    consumerProject.getPluginManager().apply(COMPILER_PLUGIN_ID);

    // When
    var mainSrcSet =
        consumerProject.getExtensions().getByType(SourceSetContainer.class).getByName("main");

    // Then
    assertTrue(
        mainSrcSet.getJava().getSrcDirs().stream()
            .anyMatch(dir -> dir.getPath().contains(BUF_GENERATED_DIR)));
  }

  @Test
  public void givenPlugin_whenApply_thenTasksRegisteredButDisabled() {
    // Given
    consumerProject =
        ProjectBuilder.builder()
            .withProjectDir(new File(projectDir, "consumer"))
            .withName("contract")
            .build();
    consumerProject.getPluginManager().apply("java");
    consumerProject.getPluginManager().apply(COMPILER_PLUGIN_ID);

    // When
    var tasks = consumerProject.getTasks();

    // Then
    assertNotNull(tasks.findByName(BUF_GENERATE_TASK_NAME));
    assertNotNull(tasks.findByName(BUF_LINT_TASK_NAME));
    assertNotNull(tasks.findByName(BUF_FORMAT_CHECK_TASK_NAME));
    assertTrue(tasks.getByName(BUF_GENERATE_TASK_NAME).getEnabled());
    assertTrue(tasks.getByName(BUF_LINT_TASK_NAME).getEnabled());
    assertTrue(tasks.getByName(BUF_FORMAT_CHECK_TASK_NAME).getEnabled());
  }
}
