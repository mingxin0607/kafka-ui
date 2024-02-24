# Part IV: Continuous Integration  

## 1. Continuous Integration 
### 1.1 Definition
> Continuous Integration (CI) is the practice of merging all developers' working copies to a shared mainline several times a day. Nowadays it is typically implemented in such a way that it triggers an automated build with testing. [1](https://en.wikipedia.org/wiki/Continuous_integration)


### 1.2 Purpose of Continuous Integration

__Early Detection of Integration Issues__

CI involves regularly integrating code changes from multiple developers into a shared repository. This helps identify integration issues early in the development process, preventing the accumulation of bugs that might arise when integrating changes later

__Automated Build and Testing__

CI systems automate the process of building the software and running tests on the integrated code. Automated builds ensure that the code can be compiled successfully, and automated tests help verify that new changes do not introduce regressions or break existing functionality.

__Enhanced Code Quality__

By automating testing and integration processes, CI helps maintain and enhance code quality. The early detection of issues and the automated validation of code changes contribute to a more stable and reliable codebase.

__Streamlined Deployment__

Continuous Integration sets the stage for Continuous Deployment (CD). Automated builds and tests create a reliable foundation for deploying software to production environments, ensuring that only validated and functional code is released.


### 1.3 CI Tools & Features
__GitHub Actions__

- Native GitHub Integration: GitHub Actions is tightly integrated into GitHub repositories, allowing developers to define CI/CD workflows using YAML files within the repository.
- Workflow Automation: It supports automating build, test, and deployment processes triggered by events like pushes, pull requests, or manual triggers through the GitHub Actions UI.
- Matrix Builds: GitHub Actions supports matrix builds, allowing users to define multiple jobs with different parameters for parallel execution.

__Jenkins__

- Extensibility: Jenkins is known for its extensive plugin ecosystem, enabling users to customize and extend its capabilities.
- Customizable Workflows: Users can define complex build and deployment workflows through a web-based interface or by scripting in Groovy.
- Community Support: With a large and active community, Jenkins benefits from continuous development and a wealth of shared knowledge.

__Travis CI__

- GitHub Integration: Travis CI seamlessly integrates with GitHub repositories, automatically triggering builds for every push or pull request.
- YAML Configuration: Build configurations are specified in a .travis.yml file within the repository, making it easy to understand and version-controlled.
- Parallel Builds: Travis CI supports parallel builds, enabling faster test execution by dividing tasks across multiple instances.

## 2. Setting up CI with Github Actions
### 2.1 Create Workflow File

Create a directory named `.github/workflows` in the GitHub repository, and add a YAML formatted workflow file (for example, `ci.yml`) within it. The workflow file specifies the conditions that trigger the CI process and the steps that need to be executed.

### 2.2 Define Trigger Conditions

In the workflow file, define the conditions that trigger the CI process, such as when code is pushed to the main branch or when a Pull Request is created.

### 2.3 Configure Jobs

In the workflow file, define the jobs that need to be executed, including environment setup, dependency installation, build, testing, etc.

### 2. Configuration in the Project
The following directory contains YML files executed by the project.  
```
├── aws_publisher.yaml
├── backend.yml
├── block_merge.yml
├── branch-deploy.yml
├── branch-remove.yml
├── build-public-image.yml
├── codeql-analysis.yml
├── cve.yaml
├── delete-public-image.yml
├── documentation.yaml
├── e2e-automation.yml
├── e2e-checks.yaml
├── e2e-manual.yml
├── e2e-weekly.yml
├── frontend.yaml
├── master.yaml
├── pr-checks.yaml
├── release-serde-api.yaml
├── release.yaml
├── release_drafter.yml
├── separate_env_public_create.yml
├── separate_env_public_remove.yml
├── stale.yaml
├── terraform-deploy.yml
├── triage_issues.yml
├── triage_prs.yml
├── welcome-first-time-contributors.yml
└── workflow_linter.yaml
```

The following YML files are taken as crucial YML files to explain.  

**1. backend.yml and frontend.yaml:** These are crucial for automating the build, test, and deployment processes for the backend and frontend parts of the application, respectively. They ensure that every change in the codebase is automatically tested and, if specified, deployed to a staging or production environment. This automation helps in identifying integration issues early and reduces manual errors in deployment.

**2. codeql-analysis.yml:** This workflow is essential for maintaining the security and quality of the code. By automatically running CodeQL scans on pull requests and pushes, it helps identify vulnerabilities and coding errors before they are merged into the main codebase, ensuring a higher level of code integrity and security posture.

**3. pr-checks.yaml:** This file automates checks on pull requests, ensuring that every contribution is reviewed for coding standards, passes unit tests, and meets other criteria set by the project maintainers before it can be merged. This process helps maintain code quality and prevents bugs or issues from entering the main codebase.

**4. release.yaml:** Manages the release process, making it easier to tag releases, create GitHub releases, and publish artifacts. This automation simplifies the process of delivering new versions of the software to end-users and keeps the release process consistent and error-free.
#### Workflow Analysis
#### 1. `backend.yml` file (under the path of `.github/workflows/backend.yml`) 

The file is used as an example to explain the workflow process.
```yml
name: "Backend: PR/master build & test"
on:  # Triggers for the workflow
  push:
    branches:
      - master  # Run on push to master branch
  pull_request_target:
    types: ["opened", "edited", "reopened", "synchronize"]
    paths:
      - "kafka-ui-api/**"  # Run for changes within the kafka-ui-api directory
      - "pom.xml"  # Also run for changes to the pom.xml file
permissions:
  checks: write
  pull-requests: write
jobs:
  build-and-test:
    runs-on: ubuntu-latest  # Specifies the runner environment
    steps:
      - uses: actions/checkout@v3  # Checks out the repository code
        with:
          fetch-depth: 0  # Fetches all history for all branches and tags
          ref: ${{ github.event.pull_request.head.sha }}  # Checks out the PR commit
      - name: Set up JDK  # Sets up Java Development Kit
        uses: actions/setup-java@v3
        with:
          java-version: '17'  # Specifies the Java version
          distribution: 'zulu'  # Specifies the JDK distribution
          cache: 'maven'  # Caches Maven dependencies
      - name: Cache SonarCloud packages  # Caches SonarCloud dependencies
        uses: actions/cache@v3
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar
      - name: Build and analyze pull request target  # Builds and runs SonarCloud analysis for PRs
        if: ${{ github.event_name == 'pull_request' }}
        env:  # Sets environment variables
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN_BACKEND }}
          HEAD_REF: ${{ github.head_ref }}
          BASE_REF: ${{ github.base_ref }}
        run: |  # Maven commands for setting version, verifying the project, and SonarCloud analysis
          ./mvnw -B -ntp versions:set -DnewVersion=${{ github.event.pull_request.head.sha }}
          ./mvnw -B -V -ntp verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
          -Dsonar.projectKey=com.provectus:kafka-ui_backend \
          -Dsonar.pullrequest.key=${{ github.event.pull_request.number }} \
          -Dsonar.pullrequest.branch=$HEAD_REF \
          -Dsonar.pullrequest.base=$BASE_REF
      - name: Build and analyze push master  # Similar to above, but for pushes to master
        if: ${{ github.event_name == 'push' }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN_BACKEND }}
        run: |
          ./mvnw -B -ntp versions:set -DnewVersion=$GITHUB_SHA
          ./mvnw -B -V -ntp verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
          -Dsonar.projectKey=com.provectus:kafka-ui_backend

```

#### 2. `frontend.yml` file (under the path of `.github/workflows/frontend.yml`) 
```yml
name: "Frontend: PR/master build & test"
on:  # Triggers for the workflow
  push:
    branches:
      - master  # Run on push to master branch
  pull_request_target:
    types: ["opened", "edited", "reopened", "synchronize"]
    paths:
      - "kafka-ui-contract/**"  # Run for changes within the kafka-ui-contract directory
      - "kafka-ui-react-app/**"  # Run for changes within the kafka-ui-react-app directory
permissions:
  checks: write
  pull-requests: write
jobs:
  build-and-test:
    env:
      CI: true  # Sets CI environment variable to true
      NODE_ENV: dev  # Sets node environment to dev
    runs-on: ubuntu-latest  # Specifies the runner environment
    steps:
      - uses: actions/checkout@v3  # Checks out the repository code
        with:
          fetch-depth: 0  # Fetches all history for all branches and tags
          ref: ${{ github.event.pull_request.head.sha }}  # Checks out the PR commit
      - uses: pnpm/action-setup@v2.4.0  # Sets up pnpm package manager
        with:
          version: 8.6.12  # Specifies the pnpm version
      - name: Install node  # Sets up Node.js
        uses: actions/setup-node@v3.8.1
        with:
          node-version: "18.17.1"  # Specifies the Node.js version
          cache: "pnpm"
          cache-dependency-path: "./kafka-ui-react-app/pnpm-lock.yaml"
      - name: Install Node dependencies  # Installs dependencies
        run: |
          cd kafka-ui-react-app/
          pnpm install --frozen-lockfile
      - name: Generate sources  # Generates source files
        run: |
          cd kafka-ui-react-app/
          pnpm gen:sources
      - name: Linter  # Runs linter
        run: |
          cd kafka-ui-react-app/
          pnpm lint:CI
      - name: Tests  # Runs tests
        run: |
          cd kafka-ui-react-app/
          pnpm test:CI
      - name: SonarCloud Scan  # Runs SonarCloud analysis
        uses: sonarsource/sonarcloud-github-action@master
        with:
          projectBaseDir: ./kafka-ui-react-app
          args: -Dsonar.projectKey=mingxin0607_kafka-ui -Dsonar.pullrequest.key=${{ github.event.pull_request.number }} -Dsonar.pullrequest.branch=${{ github.head_ref }} -Dsonar.pullrequest.base=${{ github.base_ref }}
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN_FRONTEND }}

```

#### 3. `release_drafter.yml` file (under the path of `.github/workflows/release_drafter.yml`) 

This workflow is using the Release Drafter action to automatically draft release notes.
```yml
# Name of the GitHub Actions workflow
name: "Infra: Release Drafter run"

# Events that trigger the workflow
on:
  push:
    branches:
      - master
  workflow_dispatch:
    inputs:
      version:
        description: 'Release version'
        required: false
      branch:
        description: 'Target branch'
        required: false
        default: 'master'

# Permissions set for the workflow
permissions:
  contents: read

# Jobs defined in the workflow
jobs:
  # Job to update the release draft
  update_release_draft:
    runs-on: ubuntu-latest

    # Permissions for this job
    permissions:
      contents: write
      pull-requests: write

    steps:
      # Step to use the Release Drafter action
      - uses: release-drafter/release-drafter@v5
        with:
          # Configuration file for Release Drafter
          config-name: release_drafter.yaml

          # Disable autolabeler for this run
          disable-autolabeler: true

          # Retrieve version and target branch from workflow inputs
          version: ${{ github.event.inputs.version }}
          commitish: ${{ github.event.inputs.branch }}

        # Environment variable for GitHub token authentication
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

```
## 3. Adding Code & CI Result
In an effort to enhance our project's reliability and enforce coding standards, we have integrated a Continuous Integration (CI) process using GitHub Actions. This addition automates the testing of code submissions, ensuring that all changes meet our predefined quality benchmarks before being merged into the main codebase.  

### New GitHub Action
The newly added GitHub Actions workflow, the`kafkaUI-java-build-test.yml` file, automates our Java build and testing processes.
- The file below is used to test whether the code pushed is aligned with the test set. So each push will trigger `mvn test` automatically, which ensures that only code that passes all tests can be merged, maintaining the high quality and stability of the project.
- Setup a Java development environment with JDK 11, utilizing the Temurin distribution for consistency and reliability across all builds.
- Utilize Maven for building the project (mvn -B package --file pom.xml), taking advantage of Maven's dependency management and build lifecycle capabilities.
- Cache Maven dependencies between runs to speed up the build process, reducing the time developers wait for feedback from the CI system.

```yml
name: kafkaUI-java-build-test  

on:
  push:
    branches: [ master ]  # Trigger condition: When pushing to the master branch
  pull_request:
    branches: [ master ]  # Trigger condition: when a pull request is made for the master branch

jobs:
  build:
    runs-on: ubuntu-latest  # Running environment: The latest version of Ubuntu

    steps:
    - uses: actions/checkout@v3  # Check out code
    - name: Set up JDK 17  # Setting up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'zulu'
        cache: 'maven'

    - name: Build with Maven and Run Tests  # Build the project using Maven
      run: ./mvnw clean install -Pprod

```

After adding the new GitHub Action, the `push` action is triggered by pushing to the master branch. See below link for more details.




# Reference

[1]: [Continuous integration - Wikipedia](https://en.wikipedia.org/wiki/Continuous_integration)
