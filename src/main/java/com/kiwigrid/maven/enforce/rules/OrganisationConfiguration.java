package com.kiwigrid.maven.enforce.rules;

import org.apache.maven.model.Organization;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors
public class OrganisationConfiguration implements ConfigurationCheck {

	public static final OrganisationConfiguration DEFAULT = new OrganisationConfiguration();

	@Parameter(name = "skip", defaultValue = "false", property = "pom-check.organisation.skip")
	boolean skip;

	@Parameter(name = "name", property = "pom-check.organisation.name")
	Requirement name = Requirement.required;

	@Parameter(name = "url", property = "pom-check.organisation.url")
	Requirement url = Requirement.optional;

	public String title() {
		return "organisation check rule";
	}

	@Override
	public boolean isEnabled() {
		return !skip;
	}

	@Override
	public void validate(MavenProject project, Log log) throws ValidationException {
		Organization organization = project.getOrganization();
		if (organization == null) {
			throw new ValidationException("organisation is required");
		}
		String organizationName = organization.getName();
		if (this.name.mismatches(organizationName)) {
			throw failure(organizationName, "name", name);
		}
		String organizationUrl = organization.getUrl();
		if (this.url.mismatches(organizationUrl)) {
			throw failure(organizationUrl, "url", url);
		}

	}

	private ValidationException failure(String value, String var, Requirement requirement) {
		return new ValidationException(
				String.format("organisation." + var + " is %s, but is %s", requirement,
						value != null ? "'" + value + "'" : "<null>"));
	}

}
