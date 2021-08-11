# hiberium

Code generator for working springboot, hibernate projects from csv schema definition.

Generate a high quality deployable spring boot application using freemarker templates to generate code.

1. Download this repository and open with Intellij

2. Run the gradle command `gradle clean build`

3. Prepare the concept and attribute definition file. See the [schema](SCHEMA.md)

4. Run the gradle command `gradle render`

5. Code has been generated under child project `hiberium-war`

6. Open `settings.gradle` and remove comment for the child project

7. Run the gradle command `gradle clean build`

8. Run the spring application main function from `Application.java`

9. Setup database connection details in `application.properties`

### Features to be added soon.

1. Proper sql create statement for table along with dependency ordering

2. Optional Spring security integration with JWT tokens.

3. Support for spring elastic search project.
