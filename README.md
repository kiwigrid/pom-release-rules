# What?

This is a Maven Enforce plugin rules for validating a maven POM for required elements.

## CI

[![Build Status](https://travis-ci.com/kiwigrid/pom-release-rules.svg?branch=main)](https://travis-ci.com/kiwigrid/pom-release-rules)

# Why?

Currently (Februar 2021) there are no rules to enforce POM entries for licence, organisation and issueManagement.

# How?

Add the rule at the enforcer plugin

```xml

<project>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>3.0.0-M3</version>
                <dependencies>
                    <dependency>
                        <groupId>com.kiwigrid.maven.enforce</groupId>
                        <artifactId>pom-release-rules</artifactId>
                        <version>${version.release.rules}</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <rules>
                        <ReleaseRule
                                implementation="com.kiwigrid.maven.enforce.rules.ReleaseRule">
                            <!-- rule configuration -->
                        </ReleaseRule>
                    </rules>
                </configuration>
                <executions>
                    <execution>
                        ...
                        <goals>
                            <goal>enforce</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

## Rules

### ReleaseRule

The rule is activated
by [configuring the enforcer plugin](https://maven.apache.org/enforcer/enforcer-api/writing-a-custom-rule.html).

The rules provide the following checks:

* licence
    * validates the presents of the `licences.licence` entry and their values
* organisation
    * validates the presents of the `organisation` entry and their values
* issue management
    * validates the presents of the `issueManagement` entry and their values

#### Configuration

You may customize checks rules for dedicated entries:

```xml

<configuration>
    <ReleaseRule implementation="com.kiwigrid.maven.enforce.rules.ReleaseRule">
        <licence>
            <!-- options -->
            <skip>false</skip>
        </licence>
        <issueManagement>
            <!-- options -->
            <skip>false</skip>
        </issueManagement>
        <organisation>
            <!-- options -->
            <skip>false</skip>
        </organisation>
    </ReleaseRule>
</configuration>
```

| Option                   | Default    | Type        | Meaning |
|--------------------------|------------|-------------|---------|
|`licence.skip`            | `false`    | boolean     | skip `<project><licences>` validation
|`licence.name`            | `required` | Requirement |`<project><licencences><licence><name>`
|`licence.url`             | `optional` | Requirement |`<project><licencences><licence><url>`
|`licence.comments`        | `optional` | Requirement |`<project><licencences><licence><comments>`
|`licence.distribution`    | `optional` | Requirement |`<project><licencences><licence><distribution>`
|||
|`issueManagement.skip`    | `false`    | boolean     | skip `<project><issumeManagement>` validation
|`issueManagement.url`     | `required` | Requirement |`<project><issueManagement><url>`
|`issueManagement.system`  | `optional` | Requirement |`<project><issueManagement><system>`
|||
|`organisation.skip`       | `false`    | boolean     | skip `<project><organisation>` validation
|`organisation.name`       | `required` | Requirement |`<project><organisation><name>`
|`organisation.url`        | `optional` | Requirement |`<project><organisation><url>`

`Requirement` ::= enum {`required`, `optional`, `forbidden`}
