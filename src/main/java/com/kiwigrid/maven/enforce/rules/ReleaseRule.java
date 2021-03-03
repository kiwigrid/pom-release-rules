package com.kiwigrid.maven.enforce.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

/**
 * Enforcer rule checking pom element presence based on requirement specification
 */
public class ReleaseRule implements EnforcerRule {

	LicenceConfiguration licence = LicenceConfiguration.DEFAULT;
	OrganisationConfiguration organisation = OrganisationConfiguration.DEFAULT;
	IssueManagementConfiguration issueManagement = IssueManagementConfiguration.DEFAULT;
	Filter excludes = new Filter();

	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {

		Log log = helper.getLog();

		try {
			MavenProject project = (MavenProject) helper.evaluate("${project}");
			if (excludes.test(project)) {
				log.info(String.format("ReleaseRule: excluding %s.%s-%s.%s", project.getGroupId(), project.getArtifactId(),
						project.getVersion(), project.getPackaging()));
			} else {
				log.info(String.format("ReleaseRule: validating %s.%s-%s.%s", project.getGroupId(), project.getArtifactId(),
						project.getVersion(), project.getPackaging()));
				ConfigurationCheck[] rules = { licence, organisation, issueManagement };

				for (ConfigurationCheck rule : rules) {
					if (rule.isEnabled()) {
						try {
							rule.validate(project, log);
						} catch (ConfigurationCheck.ValidationException e) {
							log.error(String.format("FAILURE: %s (%s)", rule.title(), e.getMessage()));
							throw new EnforcerRuleException(e.getMessage());
						}
						log.info(String.format("OK: %s", rule.title()));
					} else {
						log.debug(String.format("disabled rule: %s", rule.title()));
					}
				}
			}

		} catch (ExpressionEvaluationException e) {
			throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
		}

	}

	@Override
	public boolean isCacheable() {
		return false;
	}

	@Override
	public boolean isResultValid(EnforcerRule enforcerRule) {
		return false;
	}

	@Override
	public String getCacheId() {
		return getClass().getName();
	}
}
