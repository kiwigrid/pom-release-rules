package com.kiwigrid.maven.enforce.rules;

import java.util.Objects;

public enum Requirement {

    required {
        @Override
        boolean mismatches(String value) {
            return !Objects.nonNull(value) || value.isEmpty();
        }
    },
    optional {
        @Override
        boolean mismatches(String value) {
            return false;
        }
    },
    forbidden {
        @Override
        boolean mismatches(String value) {
            return !Objects.isNull(value) && !value.isEmpty();
        }
    };

    abstract boolean mismatches(String value);
}
