
![image](docs/images/readme-header.png)

---

**Source Code** : https://github.com/therohk/hiberium

**Documentation** : [Readme](README.md) | [Schema](SCHEMA.md) | [Sample](hiberium-gen/src/main/resources/hibernate-render.yaml)

---

Hiberium is a code generator to create working springboot+hibernate projects from a schema definition in csv.

**Hiberium** uses freemarker templates for all the required java classes to render a high-quality deployable webapp. 

The key features are :

* **Easy to Use** : **No coding required!** Generate the entire web application from one yaml and two csv files.
* **Compatible** : Generates accurate jpa entities, table spec (postgresql dialect) and elastic index mappings from schema.
* **Clean Architecture** : Generates the repository, service and controller layers for each entity.
* **Production Quality** : Integration with swagger, actuator, logback apis. Proper error handling and response.
* **Configurable** : Configure complex attribute and merge behaviors using simple alphabetic flags.
* **Extensible** : Easily modify the project model or implement your own templates and coding patterns.
* **Pragmatic** : Explore other useful endpoints like paginated table search and bulk entity update. 

---

### Usage Instructions

Follow these steps to render the sample project and bring up the application server.

To change the concept and attributes in the sample definition refer to the [schema](SCHEMA.md) document.

1. Download this repository and open with Intellij

2. Run the gradle command `gradle clean build`

3. Run the code from `RenderProject.java` or `gradle render`

4. Code has been generated under the child project `hiberium-war`

5. Open `settings.gradle` and remove comment for the child project

6. Run the gradle command `gradle clean build`

7. Run the spring webapp from `Application.java` or `gradle bootRun`

8. Explore the swagger api docs at `localhost:8080/hiberium/1.0/swagger-ui.html#`

Start writing business logic and setup connection details in `application.properties`.

---

### Upcoming Features

1. Bulk import and export endpoint via csv files to support data seeding.

2. Composite entities that allow nested concepts and child tables.

3. Dependency ordering for sql create table statements.

4. Optional spring security integration with and JWT tokens.

5. Full support for spring elastic search client.

6. Optional entity insert using form submission.

Code contributions are welcome!

### License

This project is licensed under the terms of the MIT license.
