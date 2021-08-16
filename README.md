# hiberium

Code generator for working springboot, hibernate projects from csv schema definition.

Generate a high quality deployable web application using freemarker templates to generate code.

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

1. Dependency ordering for sql create table statements.

2. Optional Spring security integration with JWT tokens.

3. Bulk import and export endpoint via csv files for any entity.

4. Full support for spring elastic search client.
