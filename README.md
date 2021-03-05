# What?

This is a Maven Enforce plugin rules for validating a maven POM for required elements.

## CI

[![Build Status](https://travis-ci.com/kiwigrid/pom-release-rules.svg?branch=main)](https://travis-ci.com/kiwigrid/pom-release-rules)

## Release

[![Maven Central](https://img.shields.io/maven-central/v/com.kiwigrid.maven.enforce/pom-release-rules.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.kiwigrid.maven.enforce%22%20AND%20a:%22pom-release-rules%22)

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

You may customize checks rules for dedicated entries.

```xml

<configuration>
    <ReleaseRule implementation="com.kiwigrid.maven.enforce.rules.ReleaseRule">
        <excludes>
          <exclude>
            <groupId></groupId>
            <artifactId></artifactId>
            <type></type>
            <version></version>
          </exclude>
          ...
        </excludes>
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

The customization covers filter inspected projects and rule check details.

You can skip rule enforcement on maven project level to define rule settings in any parent.

The filter is applied based on `excludes` entries where all `exclude` elements are `or` joined.

```xml
<excludes>
  <exclude>
    <type>pom</type> <!-- exclude all pom artifacts -->
  </exclude>
  <exclude>
    <groupId>com.kiwigrid</groupId> 
    <artifact>draft</artifact> <!-- exclude all types and versions of a specific artifact -->
  </exclude>
</excludes>
```

Use the following options define the requirements of the pom entries to inspect:

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

## Contribute

### Release

Add your credentials of https://issues.sonatype.org/ into your `settings.xml`.

```xml
  <server>
      <id>ossrh</id>
      <username>...</username>
      <password>...</password>
  </server>
```

and publish via

```
mvn clean deploy -Dgpg.keyname=... -Dpgp.password=... -Prelease  
```