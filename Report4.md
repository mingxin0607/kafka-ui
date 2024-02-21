# Part IV: Continuous Integration  

## 1. Continuous Integration 
### 1.1 Definition


### 1.2 Purpose of Continuous Integration


## 2. Setting up CI
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

## 3. Adding Code & CI Result
In an effort to enhance our project's reliability and enforce coding standards, we have integrated a Continuous Integration (CI) process using GitHub Actions. This addition automates the testing of code submissions, ensuring that all changes meet our predefined quality benchmarks before being merged into the main codebase.  

### New GitHub Action 1  
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
    - name: Set up JDK 11  # Setting up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'  # Using the Temurin distribution
        cache: 'maven'  # Cache Maven dependencies to speed up builds

    - name: Build with Maven  # Build the project using Maven
      run: mvn -B package --file pom.xml

    - name: Run tests  # Run tests
      run: mvn test

```

After adding the new GitHub Action, the `push` action is triggered by pushing the `Report4.md` file. The following result is obtained.

![Fig1. GitHub Action1-Overview](https://github.com/mingxin0607/kafka-ui/blob/d1af371d10c6749609e75d2ed9686a362feee3bd/Fig/Action1-Result%20Overview.png)  

Fig1. Overview of GitHub Action1

![Fig2. GitHub Action1-Detail1](https://github.com/mingxin0607/kafka-ui/blob/d1af371d10c6749609e75d2ed9686a362feee3bd/Fig/Action1-Details.png)  

Fig2. Detail1 of GitHub Action1

![Fig3. GitHub Action1-Detail2](https://github.com/mingxin0607/kafka-ui/blob/840d245bdac5ba1d25486bae1131827532d95002/Fig/Action1-DetailII.png)   

Fig3. Detail2 of GitHub Action2



By viewing the log given by GitHub, the failure is caused by failing to build with mvn. The error indicates a problem with compiling the `kafka-ui-contract` module using Maven. The message "release version 17 not supported" suggests that the Java version set for the project is not compatible with the compiler's expected version. This can happen if the project is set to use a newer Java version than what the compiler plugin supports or recognizes.


# Reference
[1]_ CircleCI._ (2024, February 16). CircleCI. https://circleci.com/continuous-integration/

[2] _What is CI/CD?_ (n.d.). Red Hat - We make open source technologies for the enterprise. https://www.redhat.com/en/topics/devops/what-is-ci-cd

[3] _What is Continuous Integration?_ (n.d.). AWS. https://aws.amazon.com/devops/continuous-integration/#:~:text=Continuous%20integration%20is%20a%20DevOps,builds%20and%20tests%20are%20run
