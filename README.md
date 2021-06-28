# hiberium

Code generator for working springboot, hibernate projects from csv schema definition.

1. Download this repository and open with Intellij

2. Run the gradle command `gradle clean build`

3. Prepare the concept and attribute schema file

4. Run the gradle command `gradle render`

5. Code has been generated under child project `hiberium-war`

6. Open `settings.gradle` and remove comment for the child project

7. Run the gradle command `gradle clean build`

8. Run the spring application main function from `Application.java`

9. Setup database connection details in `application.properties`
