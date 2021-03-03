package com.kiwigrid.maven.enforce.rules;

import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * config option filtering maven projects by specified artifact filter
 */
@ToString
@Accessors
public class Filter implements Predicate<MavenProject> {

	@Parameter(name = "exclude", required = true)
	List<ArtifactFilter> excludes = new LinkedList<>();

	public void addExclude(ArtifactFilter artifact) {
		excludes.add(artifact);
	}

	@Override
	public boolean test(MavenProject project) {
		for (ArtifactFilter artifact : excludes) {
			if (artifact.test(project)) {
				return true;
			}
		}
		return false;
	}
}
