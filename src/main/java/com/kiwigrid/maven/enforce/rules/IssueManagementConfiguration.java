package com.kiwigrid.maven.enforce.rules;

import org.apache.maven.model.IssueManagement;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors
public class IssueManagementConfiguration implements ConfigurationCheck {

	public static final IssueManagementConfiguration DEFAULT = new IssueManagementConfiguration();

	boolean skip;
	Requirement url = Requirement.required;
	Requirement system = Requirement.optional;

	public String title() {
		return "issueManagement check rule";
	}

	@Override
	public boolean isEnabled() {
		return !skip;
	}

	@Override
	public void validate(MavenProject project, Log log) throws ValidationException {
		IssueManagement issueManagement = project.getIssueManagement();
		if (issueManagement == null) {
			throw new ValidationException("issueManagement is required");
		}
		String issueManagementUrl = issueManagement.getUrl();
		if (this.url.mismatches(issueManagementUrl)) {
			throw failure(issueManagementUrl, "url", this.url);
		}
		String issueManagementSystem = issueManagement.getSystem();
		if (this.system.mismatches(issueManagementSystem)) {
			throw failure(issueManagementSystem, "system", this.system);
		}
	}

	private ValidationException failure(String value, String var, Requirement requirement) {
		return new ValidationException(
				String.format("issueManagement." + var + " is %s, but is %s", requirement,
						value != null ? "'" + value + "'" : "<null>"));
	}
}
