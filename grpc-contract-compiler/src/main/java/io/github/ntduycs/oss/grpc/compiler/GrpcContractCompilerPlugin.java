package io.github.ntduycs.oss.grpc.compiler;

import static build.buf.gradle.FormatConfigurationKt.BUF_FORMAT_CHECK_TASK_NAME;
import static build.buf.gradle.GenerateConfigurationKt.BUF_GENERATE_TASK_NAME;
import static build.buf.gradle.GenerateConfigurationKt.GENERATED_DIR;
import static build.buf.gradle.LintConfigurationKt.BUF_LINT_TASK_NAME;
import static build.buf.gradle.OutputSupportKt.BUF_BUILD_DIR;

import build.buf.gradle.BufPlugin;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.Directory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

/** Plugin to extract .proto files from gRPC contract dependencies. */
@SuppressWarnings("unused")
public class GrpcContractCompilerPlugin implements Plugin<@NotNull Project> {
  static final String BUF_GENERATED_DIR =
      String.join(File.separator, BUF_BUILD_DIR, GENERATED_DIR, "sources", "grpc", "java", "main");

  static final String GRPC_CONTRACT_CONFIGURATION = "grpcContract";

  private static final Set<String> dependingTasks =
      Set.of("compileJava", "compileKotlin", "kaptGenerateStubs");

  private static String capitalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  private static String kebabToCamel(String str) {
    var words = str.split("-");
    return Arrays.stream(words)
        .map(GrpcContractCompilerPlugin::capitalize)
        .collect(Collectors.joining());
  }

  /** Default constructor. */
  public GrpcContractCompilerPlugin() {}

  @Override
  public void apply(Project project) {
    project
        .getConfigurations()
        .create(
            GRPC_CONTRACT_CONFIGURATION,
            conf -> {
              conf.setCanBeConsumed(false);
              conf.setCanBeResolved(true);
            });

    project.getPlugins().apply(BufPlugin.class);
    project.getPlugins().apply(JavaPlugin.class);

    project
        .getExtensions()
        .configure(
            JavaPluginExtension.class,
            java -> java.getSourceSets().getByName("main", configureSourceSet(project)));

    project.afterEvaluate(
        prj -> {
          var grpcContractConfig = prj.getConfigurations().getByName(GRPC_CONTRACT_CONFIGURATION);
          var bufGenerateTask = prj.getTasks().getByName(BUF_GENERATE_TASK_NAME);

          var grpcContractDependencies = grpcContractConfig.getDependencies();

          if (grpcContractDependencies.isEmpty()) {
            bufGenerateTask.setEnabled(false);
            return;
          }

          // Register tasks to extract .proto files from gRPC contract dependencies
          grpcContractDependencies.stream()
              .map(dep -> registerTasks(prj, dep))
              .forEach(bufGenerateTask::dependsOn);

          // Configure compile tasks to depend on bufGenerate
          prj.getTasks()
              .matching(task -> dependingTasks.contains(task.getName()))
              .configureEach(task -> task.dependsOn(bufGenerateTask));

          // Disable buf tasks as they've already covered in
          // the central gRPC contract repository
          project.getTasks().named(BUF_LINT_TASK_NAME, task -> task.setEnabled(false));
          project.getTasks().named(BUF_FORMAT_CHECK_TASK_NAME, task -> task.setEnabled(false));
        });
  }

  private Action<@NotNull SourceSet> configureSourceSet(Project project) {
    return sourceSet ->
        sourceSet.getJava().srcDir(project.getLayout().getBuildDirectory().dir(BUF_GENERATED_DIR));
  }

  private File getContractZipFile(Project project, String name, String version) {
    return project
        .getConfigurations()
        .getByName(GRPC_CONTRACT_CONFIGURATION)
        .filter(file -> file.getName().equals("%s-%s.zip".formatted(name, version)))
        .getSingleFile();
  }

  private String getDependencyNotation(Dependency dependency) {
    return "%s:%s:%s"
        .formatted(dependency.getGroup(), dependency.getName(), dependency.getVersion());
  }

  private String getTaskName(Dependency dependency) {
    return "compile%sGrpcContract".formatted(kebabToCamel(dependency.getName()));
  }

  private Provider<@NotNull Directory> getUnzipDir(Project project, String name) {
    return project.getLayout().getBuildDirectory().dir(Path.of("extracted-proto", name).toString());
  }

  /**
   * Registers a task to extract .proto files from a gRPC contract dependency.
   *
   * @param prj The project to register the task in.
   * @param dep The dependency to extract .proto files from.
   * @return The registered task.
   */
  private TaskProvider<@NotNull Copy> registerTasks(Project prj, Dependency dep) {
    var depName = dep.getName();
    var depVersion = dep.getVersion();
    var notation = getDependencyNotation(dep);
    var contractZipFile = getContractZipFile(prj, depName, depVersion);
    return prj.getTasks()
        .register(
            getTaskName(dep),
            Copy.class,
            t -> {
              t.setGroup("contract");
              t.setDescription("Extracts .proto files from " + notation);
              t.from(prj.zipTree(contractZipFile));
              t.into(getUnzipDir(prj, depName));
              t.include("**/*.proto");
            });
  }
}
