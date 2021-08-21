# Hiberium Project Description

# Project

The project to be rendered is defined under [hibernate-render.yaml](hiberium-gen/src/main/resources/hibernate-render.yaml).

The yaml configuration stores the templates and their target package which are rendered in order.

The templates under the section [projections](hiberium-gen/src/main/resources/freemarker) are rendered once per project.

The templates under the section [conceptions](hiberium-gen/src/main/resources/springboot) are rendered once per concept.

A runnable spring web application is generated under the hiberium-war folder.

Run the sample project main function under [RenderProject.java](hiberium-gen/src/main/java/com/konivax/RenderProject.java) to get started.

Build and run the generated CRUD web application from [Application.java](hiberium-war/src/main/java/com/konivax/Application.java).

Implement further business logic and connect to a real database.  

# Concept

concept = entity = table = class = model = bean = type = container

This configuration is loaded from [concept-def.csv](hiberium-gen/src/main/resources/concept-def.csv) by default.

## Schema

| Required | Options | Meaning |
| ---- |---- | ---- |
| concept_id | optional `[0-9]+` | numeric identifier for concept |
| concept_name | required `[A-Z][0-9A-Za-z]+` | denoted in camel case |
| module_name | required `[a-z]+` | single word in lower case |
| concept_table | `[0-9a-z_]+` | db table name ; lower case separated by underscore |
| concept_schema | `[0-9a-z_]+` | db schema name ; lower case separated by underscore |
| concept_apipath | optional `[0-9a-z\-]+` | api context path ; default is hyphen separated concept_name |
| concept_desc | optional text | description of concept |
| update_code | optional | default update strategy for fields |
| dynamic_insert | `true` | enable hibernate dynamic insert |
| dynamic_update | `false` | enable hibernate dynamic update |

# Attribute

attribute = column = field = subtype

This configuration is loaded from [attribute-xref.csv](hiberium-gen/src/main/resources/attribute-xref.csv) by default.

## Schema

| Required | Options | Meaning |
|----|----|----|
| concept_name | defined name | concept which contains attribute |
| attribute_name | `[a-z][0-9A-Za-z]+` | java field name exposed via rest api |
| field_name | `[0-9a-z_]+` | database field name |
| field_type | `[0-9a-z]+` | database field type |
| attribute_role | optional see [roles](#attribute-roles) | alphabetic field configuration |
| foreign_key | `tablename.fieldname` | foreign key relation added on field |
| field_scale | if applicable | length for varchar or scale for numeric type |
| field_precision | if applicable | precision for numeric type |
| default_value | optional | default value for field |
| elastic_type | optional | elastic search field type |
| update_code | optional | update strategy for field |

## Attribute Roles

A string of chained alphabetic roles can be used to configure an attribute.

Todo, any boolean setting configured from the role can be over-ridden individually.

| Role Value | Meaning | Effect |
|----|----|----|
| K | Primary Key | single primary key in table |
| I | Immutable | value cannot be changed once set |
| F | Foreign Key | foreign key constraint on field |
| H | Hidden | field is excluded from json response |
| U | Unique Key | unique key constraint on field |
| N | Non-Nullable | field value cannot be null |
| R | Searchable | field can be used for table lookup |
| O | Orderable | field can be used for sorting |
| G | Groupable | field can be used for grouping |

# Update Strategy

This flag describes how updates to an entity via the PUT api are handled. 

It decides how the value of each field is handled during a merge operation between two entities.

Options include always insert, overwrite/replace, finalize and merge. 

## Strategy Codes

This feature will be implemented using reflections api and is not yet available. 
