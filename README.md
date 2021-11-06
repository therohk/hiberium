
![image](docs/images/readme-header.png)

---

**Source Code** : https://github.com/therohk/hiberium

**Documentation** : [Readme](README.md) | [Schema](SCHEMA.md) | [Sample](hiberium-gen/src/main/resources/hibernate-render.yaml)

---

Hiberium is a code generator to create working springboot+hibernate projects from a schema definition in csv.

**Hiberium** uses freemarker templates to render all the java classes required for a high-quality deployable webapp. 

The key features are :

* **Easy to Use** : **No coding required!** Generate the entire web application from one yaml and two csv files.
* **Compatible** : Generates accurate jpa entities, table spec (postgresql dialect) and elastic index mappings from schema.
* **Clean Architecture** : Generates the repository, service and controller layers for each entity.
* **Production Quality** : Integration with swagger, actuator, vault apis. Proper error handling and response.
* **Pragmatic** : Explore other useful endpoints like **paginated [rsql](RSQLEXP.md) search**, bulk entity merge and **csv import**.
* **Configurable** : Configure attributes and merge behaviors using simple alphabetic flags.
* **Extensible** : Easily modify the project model or implement your own templates and coding patterns.
* **Composable** : Support for two-level nesting of concepts. Seamlessly transform between nested and flat/relational models.

---

### Usage Instructions

Follow these steps to render the sample project and bring up the application server.

To change the concepts and attributes in the sample definition refer to the [schema](SCHEMA.md) document.

1. Download this repository and open with Intellij

2. Run the gradle command `gradle clean build`

3. Run the code from `RenderProject.java` or `gradle render`

4. Webapp has been generated under the child project `hiberium-war`

5. Reimport/Refresh the gradle project then run `gradle clean build`

6. Raise the spring webapp from `Application.java` or `gradle bootRun`

7. Explore the swagger api docs at `localhost:8080/hiberium/1.0/swagger-ui.html#`

OR use local Docker:

6. Raise the webapp and postgres containers with `docker-compose up -d`

Start writing business logic and setup connection details in `application-{env}.properties`.

---

### Upcoming Features

1. Full support for spring mongo and elastic search client.

2. Support for binary and blob data types.

3. Optional spring security integration with JWT tokens.

4. Dependency ordering for sql create table statements.

5. Optional insert using form submission and lightweight browser.

---

Code contributions are welcome via issues and pull requests.

### License

This project is licensed under the terms of the Apache License 2.0.
