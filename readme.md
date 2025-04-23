# GITEPSRIT

## Overview

This project is a Java application managed with Maven. It includes custom Keycloak providers, database initialization
scripts, and monitoring configurations.

## Project Structure

## 2. Requirement

### 2.1. Activation des GitHooks

___
To enable GitHooks in your project, run the following command:

```
chmod +x .githooks/*
chmod +x .githooks/scripts/*
git config core.hooksPath .githooks
```

This command sets the global Git hooks path to .githooks, where you can store your validation scripts.

### 2.2. Standardize code formatting with Spotless

The setup is really simple and comprises of only two steps no matter you use Maven or Gradle:

Include the spotless dependency: "com.diffplug.spotless"
Configure the plugin based on what your formatting preferences
Make it part of build (optional but preferred)

```
<dependency>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
</dependency>
```

Configure the plugin

```
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <configuration>
        <ratchetFrom>origin/develop</ratchetFrom>
        <java>
            <includes>
                <include>src/main/java/**/*.java</include>
                <include>src/test/java/**/*.java</include>
            </includes>
            <importOrder />
            <removeUnusedImports />
            <toggleOffOn/>
            <trimTrailingWhitespace/>
            <endWithNewline/>
            <indent>
                <tabs>true</tabs>
                <spacesPerTab>4</spacesPerTab>
            </indent>
            <palantirJavaFormat/>
        </java>
    </configuration>
</plugin>```