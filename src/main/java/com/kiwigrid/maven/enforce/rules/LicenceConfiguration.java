package com.kiwigrid.maven.enforce.rules;

import java.util.List;

import org.apache.maven.model.License;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors
public class LicenceConfiguration implements ConfigurationCheck {

	public static final LicenceConfiguration DEFAULT = new LicenceConfiguration();

	boolean skip;
	Requirement name = Requirement.required;
	Requirement url = Requirement.optional;
	Requirement comments = Requirement.optional;
	Requirement distribution = Requirement.optional;

	@Override
	public String title() {
		return "licence check rule";
	}

	@Override
	public boolean isEnabled() {
		return !skip;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void validate(MavenProject project, Log log) throws ValidationException {
		List licenses = project.getLicenses();
		if (licenses == null || licenses.isEmpty()) {
			throw new ValidationException("at least one licence entry is required");
		} else {
			for (int i = 0; i < licenses.size(); i++) {
				License l = (License) licenses.get(i);
				if (name.mismatches(l.getName())) {
					throw failure(i, "name", name, l.getName());
				}
				if (url.mismatches(l.getUrl())) {
					throw failure(i, "url", url, l.getUrl());
				}
				if (comments.mismatches(l.getComments())) {
					throw failure(i, "comment", comments, l.getComments());
				}
				if (distribution.mismatches(l.getDistribution())) {
					throw failure(i, "distribution", distribution, l.getDistribution());
				}
			}
		}
	}

	private ValidationException failure(int i, String var, Requirement requirement, String value) {
		return new ValidationException(
				String.format("licences.licence." + var + " is %s at item [%s], but is %s", requirement, i,
						value != null ? "'" + value + "'" : "<null>"));
	}
}
