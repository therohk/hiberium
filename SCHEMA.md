# Hiberium Project Description

# Project

The project to be rendered is defined under [hibernate-render.yaml](hiberium-gen/src/main/resources/hibernate-render.yaml).

The yaml configuration stores the templates and their target packages which are rendered in order.

The templates under the section [projections](hiberium-gen/src/main/resources/freemarker) are rendered only once.

The templates under the section [conceptions](hiberium-gen/src/main/resources/springboot) are rendered once per concept.

A runnable spring web application is generated under the hiberium-war folder.

Run the sample project from the main function in [RenderProject.java](hiberium-gen/src/main/java/com/konivax/RenderProject.java) to get started.

Build then run the generated web application from [Application.java](hiberium-war/src/main/java/com/konivax/Application.java).

Implement further business logic and connect to an external database.  

# Concept

concept = entity = table = document = class = model = type = relation = container

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
| concept_index | optional `[0-9a-z_]+` | elastic index name ; defaults to table name |
| update_code | optional | default update strategy for fields |
| concept_parent | not available | for composite objects and nesting |
| dynamic_insert | `true` | enable hibernate dynamic insert |
| dynamic_update | `false` | enable hibernate dynamic update |

# Attribute

attribute = column = field = subtype = dimension

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
| attribute_format | not available | regex validation pattern for string types |
| elastic_type | optional | override elastic search field type |
| update_code | optional see [codes](#strategy-codes) | update strategy for field |

## Attribute Roles

A string of chained alphabetic roles can be used to configure an attribute.

The templates take these flags into account to generate more accurate endpoints and functions.

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
| G | Groupable | field can be used for grouping and faceting |

# Update Strategy

This single letter flag describes how modifications to an entity for a PUT/UPDATE operation should be handled.
 
It decides whether a change to the value of the field is allowed during a merge operation between two entities.

This flag can be set at both concept and attribute level. An incoming api request can attempt to override this setting.

## Strategy Codes

The same field within the source and target tuple is represented by s and t respectively.

Whether the strategy requires data to be present for the field, is shown by { 0=no, 1=yes, X=dontcare }. 

The value of the source field is written to the target field only if the predicate passes.

See the implementation of this logic in [MergeObject.java](hiberium-gen/src/main/java/com/konivax/models/merge/MergeObject.java).

| Code Value | Predicate | Meaning |
|----|----|----|
| C | n/a | always create new instance |
| N | `0` | always reject ; ignore change |
| Y | `sX & tX` | always accept ; insert, update or delete |
| B | `s1 & tX` | insert or update, no delete |
| H | `s1 & t1` | update value only if exists |
| U | `sX & t1` | update or delete only, no insert |
| I | `sX & t0` | insert only, no update or delete |
| D | `s0 & tX` | delete only, no update or insert |

Here the insert, update and delete refer to the value transition for a single attribute in the target tuple.

| Modify Type | Transition |
|----|----|
| ignore | `null <- null` or `non-null == non-null` |
| insert | `null <- non-null` |
| delete | `non-null <- null` |
| update | `non-null <- non-null` |
