package io.github.ntduycs.oss.grpc.packager;

import java.io.File;
import java.util.Optional;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.bundling.Zip;
import org.jetbrains.annotations.NotNull;

/**
 * A Gradle plugin that packages gRPC contracts into ZIP files and publishes them to a Maven
 * repository.
 */
@SuppressWarnings("unused")
public class GrpcContractPackagerPlugin implements Plugin<@NotNull Project> {
  private static String capitalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /** Default constructor for the plugin */
  public GrpcContractPackagerPlugin() {}

  @Override
  public void apply(Project project) {
    project.getPluginManager().apply(MavenPublishPlugin.class);

    var publisher = project.getExtensions().getByType(PublishingExtension.class);
    var baseGroup = project.getGroup().toString();

    var protoDirs = getProtoDirs(project);

    for (var protoDir : protoDirs) {
      var dir = protoDir.getName();
      var version = project.getProviders().gradleProperty(versionName(dir)).get();
      var task =
          project
              .getTasks()
              .register(
                  taskName(dir),
                  Zip.class,
                  t -> {
                    t.setGroup("contract");
                    t.setDescription("Packages the gRPC contract for %s".formatted(dir));
                    t.from(protoDir);
                    t.getArchiveFileName().set("%s.zip".formatted(dir));
                    t.getDestinationDirectory()
                        .set(project.getLayout().getBuildDirectory().dir("libs"));
                  });

      publisher
          .getPublications()
          .create(
              publicationName(dir),
              MavenPublication.class,
              p -> {
                p.setGroupId("%s.proto".formatted(baseGroup));
                p.setArtifactId(dir);
                p.setVersion(version);

                var artifactProvider = task.flatMap(Zip::getArchiveFile);
                p.artifact(artifactProvider, a -> a.setExtension("zip"));
              });
    }
  }

  private File[] getProtoDirs(Project project) {
    return getProtoRootDir(project)
        .map(f -> f.listFiles(File::isDirectory))
        .orElseGet(
            () -> {
              project
                  .getLogger()
                  .warn("The root 'proto' directory does not exist or has no subdirectories.");
              return new File[0];
            });
  }

  private Optional<File> getProtoRootDir(Project project) {
    var dir = project.file("proto");
    return !dir.exists() || !dir.isDirectory() ? Optional.empty() : Optional.of(dir);
  }

  private String publicationName(String dir) {
    return "%sGrpcContract".formatted(dir);
  }

  private String taskName(String dir) {
    return "package%sGrpcContract".formatted(capitalize(dir));
  }

  private String versionName(String dir) {
    return "contract.%s.version".formatted(dir);
  }
}
