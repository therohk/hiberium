# Hiberium Project Description

# Project

The project to be rendered is defined under [hibernate-render.yaml](hiberium-gen/src/main/resources/hibernate-render.yaml).

The yaml configuration stores the templates and their target package which are rendered in order.

The templates in the section projections are rendered once per project.

The templates in the section conceptions are rendered once per concept.

A runnable spring web application is generated under the hiberium-war folder.

Run the sample project main function under [RenderProject.java](hiberium-gen/src/main/java/com/konivax/RenderProject.java) to get started.

Build and run the generated CRUD web application from [Application.java](hiberium-war/src/main/java/com/konivax/Application.java).

Implement further business logic and connect to a real database.  

# Concept

concept = entity = table = class = model = object = type

This configuration is loaded from [concept-def.csv](hiberium-gen/src/main/resources/concept-def.csv) by default.

## Schema

| Required | Options | Meaning |
| ---- |---- | ---- |
| concept_id | optional `[0-9]+` | numeric identifier for concept |
| concept_name | required `[A-Za-z0-9]+` | denoted in camel case |
| concept_module | required `[A-Z][a-z]+` | denoted in standard case as single word |
| concept_table | `[a-z0-9_]+` | db table name ; lower case separated by underscore |
| concept_schema | `[a-z0-9_]+` | db schema name ; lower case separated by underscore |
| concept_apipath | optional api context path | default value is hyphen separated concept_name |
| concept_desc | optional text | description of concept |
| concept_strategy | optional | update strategy for resource |

# Attribute

attribute = column = field = subtype

This configuration is loaded from [attribute-xref.csv](hiberium-gen/src/main/resources/attribute-xref.csv) by default.

## Schema

| Required | Options | Meaning |
|----|----|----|
| concept_name | defined name | concept which contains attribute |
| attribute_name | `[a-z][A-Za-z]+` | java field name exposed via rest api |
| field_name | `[a-z0-9_]+` | database field name |
| field_type | `[a-z0-9]+` | database field type |
| attribute_flag | optional see [flags](#attribute-flags) | alphabetic field configuration |
| foreign_key | `tablename.fieldname` | foreign key relation added on field |
| field_length | if applicable | length for varchar or scale for numeric type |
| field_precision | if applicable | precision for numeric type |
| default_value | if applicable | default value for field |
| dynamic_insert | `true` | enable hibernate dynamic insert |
| dynamic_update | `false` | enable hibernate dynamic update |
| elastic_type | not implemented | elastic search field type |

## Attribute Flags

| Flag Value | Meaning | Effect |
|----|----|----|
| K | Primary Key | single primary key in table |
| F | Finalized | value cannot be changed once set |
| H | Hidden | field is excluded from json response |
| U | Unique Key | unique key constraint on field |
| M | Foreign Key | foreign key constraint on field |
| N | Non-Nullable | field value cannot be null |
| R | Searchable | field can be used for table lookup |
| O | Orderable | field can be used for sorting |
| I | Indexable | field is indexed in search engine |
| G | Groupable | field can be used for grouping |

# Update Strategy

This flag describes how updates to an entity via the PUT api are handled. 

Options include always insert, overwrite/replace, finalize and merge. 

This feature is not yet available. Code contributions are welcome!
