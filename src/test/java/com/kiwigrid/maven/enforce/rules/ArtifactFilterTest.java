package com.kiwigrid.maven.enforce.rules;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArtifactFilterTest {

	@Test
	public void testPredicate() {
		MavenProject p = new MavenProject();
		p.setGroupId("com.kiwigrid");
		p.setArtifactId("sample");
		p.setVersion("1.0.0");
		p.setPackaging("pom");

		Assertions.assertTrue(artifact(null, null, null, null).test(p));
		Assertions.assertTrue(artifact("com.kiwigrid", "sample", null, null).test(p));
		Assertions.assertTrue(artifact("com.kiwigrid", "sample", "1.0.0", null).test(p));
		Assertions.assertTrue(artifact("com.kiwigrid", "sample", "1.0.0", "pom").test(p));
		Assertions.assertFalse(artifact("com.kiwigrid.sample", null, null, null).test(p));
		Assertions.assertFalse(artifact("com.kiwigrid", "sample", "1.0.1", null).test(p));
		Assertions.assertFalse(artifact("com.kiwigrid", "sample", "1.0.0", "jar").test(p));
	}

	private static ArtifactFilter artifact(String groupId, String artifactId, String version, String type) {
		ArtifactFilter artifact = new ArtifactFilter();
		artifact.groupId = groupId;
		artifact.artifactId = artifactId;
		artifact.version = version;
		artifact.type = type;
		return artifact;
	}

}