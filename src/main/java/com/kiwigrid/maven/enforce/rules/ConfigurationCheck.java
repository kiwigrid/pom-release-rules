package com.kiwigrid.maven.enforce.rules;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public interface ConfigurationCheck {

    String title();

    boolean isEnabled();

    void validate(MavenProject project, Log log) throws ValidationException;

    class ValidationException extends Exception {

        public ValidationException(String message) {
            super(message);
        }
    }
}
