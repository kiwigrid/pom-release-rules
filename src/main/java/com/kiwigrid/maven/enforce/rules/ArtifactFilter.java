package com.kiwigrid.maven.enforce.rules;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.maven.project.MavenProject;

import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors
public class ArtifactFilter implements Predicate<MavenProject> {

	String groupId;
	String artifactId;
	String type;
	String version;

	@Override
	public boolean test(MavenProject project) {

		Predicate<MavenProject> groupPredicate = fn.apply(() -> Optional.ofNullable(groupId), () -> MavenProject::getGroupId);
		Predicate<MavenProject> artifactPredicate = fn.apply(() -> Optional.ofNullable(artifactId), () -> MavenProject::getArtifactId);
		Predicate<MavenProject> typePredicate = fn.apply(() -> Optional.ofNullable(type), () -> MavenProject::getPackaging);
		Predicate<MavenProject> versionPredicate = fn.apply(() -> Optional.ofNullable(version), () -> MavenProject::getVersion);
		return groupPredicate.and(artifactPredicate).and(typePredicate).and(versionPredicate).test(project);
	}

	BiFunction<Supplier<Optional<String>>, Supplier<Function<MavenProject, String>>, Predicate<MavenProject>> fn = (
			conditionSupplier, valueSupplier) -> project -> {
				Optional<String> condition = conditionSupplier.get();
				String value = valueSupplier.get().apply(project);
				return condition.filter(c -> !c.isEmpty())
						.map(s -> s.equals(value))
						.orElse(Boolean.TRUE);
			};
}
