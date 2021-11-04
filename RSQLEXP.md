
---

**RSQL Parser** : https://github.com/jirutka/rsql-parser

**JPA Criteria Builder** : https://github.com/tennaito/rsql-jpa

---

RSQL is a query language for parametrized filtering of entries in RESTful APIs.

Since RSQL is a superset of the FIQL, it can be used for parsing FIQL as well.

### Integration

The conversion from Rsql-query to Jpa-criteria is implemented in [SearchEntityController.java](/hiberium-gen/src/main/resources/projection/csvimport/controller-search-java.ftl).

Once the server is running, access the interface from the 
[Endpoint](http://localhost:8080/hiberium/1.0/query/entityName?query=attribute=='value')
or
[Swagger](http://localhost:8080/hiberium/1.0/swagger-ui.html#/search-entity-controller/searchByQueryUsingGET).  

---

### Examples

The rsql query is read from the request parameter "query" : `/query/entityName?pageNum=1&perPage=5&query=attribute=='value'`

RSQL expressions in both FIQL-like and alternative notations :

```
- name=="Kill Bill";year=gt=2003
- name=="Kill Bill" and year>2003
- genres=in=(sci-fi,action);(director=='Christopher Nolan',actor==*Bale);year=ge=2000
- genres=in=(sci-fi,action) and (director=='Christopher Nolan' or actor==*Bale) and year>=2000
- director.lastName==Nolan;year=ge=2000;year=lt=2010
- director.lastName==Nolan and year>=2000 and year<2010
- genres=in=(sci-fi,action);genres=out=(romance,animated,horror),director==Que*Tarantino
- genres=in=(sci-fi,action) and genres=out=(romance,animated,horror) or director==Que*Tarantino
```

---

### Grammar and semantic

The following grammar specification is written in EBNF notation ([ISO 14977](http://www.cl.cam.ac.uk/~mgk25/iso-14977.pdf)).

RSQL expression is composed of one or more comparisons, related to each other with logical operators:

* Logical AND : `;` or `` and ``
* Logical OR : `,` or `` or ``

By default, the AND operator takes precedence (i.e. it’s evaluated before any OR operators are).
However, a parenthesized expression can be used to change the precedence, yielding whatever the contained expression yields.

```
input          = or, EOF;
or             = and, { "," , and };
and            = constraint, { ";" , constraint };
constraint     = ( group | comparison );
group          = "(", or, ")";
```

Comparison is composed of a selector, an operator and an argument.

```
comparison     = selector, comparison-op, arguments;
```

Selector identifies an attribute of the resource representation to filter by.
It can be any non-empty Unicode string that doesn't contain reserved characters (see below) or a white space.
The specific syntax of the selector is not enforced by this parser.

```
selector       = unreserved-str;
```

Comparison operators are in FIQL notation and some of them has an alternative syntax as well:

* Equal to : `==`
* Not equal to : `!=`
* Less than : `=lt=` or `<`
* Less than or equal to : `=le=` or `<=`
* Greater than operator : `=gt=` or `>`
* Greater than or equal to : `=ge=` or `>=`
* In : `=in=`
* Not in : `=out=`

You can also simply extend this parser with your own operators.

```
comparison-op  = comp-fiql | comp-alt;
comp-fiql      = ( ( "=", { ALPHA } ) | "!" ), "=";
comp-alt       = ( ">" | "<" ), [ "=" ];
```

Argument can be a single value, or multiple values in parentheses separated by comma.
Value that doesn't contain any reserved character or a white space can be unquoted, other arguments must be enclosed in single or double quotes.

```
arguments      = ( "(", value, { "," , value }, ")" ) | value;
value          = unreserved-str | double-quoted | single-quoted;

unreserved-str = unreserved, { unreserved }
single-quoted  = "'", { ( escaped | all-chars - ( "'" | "\" ) ) }, "'";
double-quoted  = '"', { ( escaped | all-chars - ( '"' | "\" ) ) }, '"';

reserved       = '"' | "'" | "(" | ")" | ";" | "," | "=" | "!" | "~" | "<" | ">";
unreserved     = all-chars - reserved - " ";
escaped        = "\", all-chars;
all-chars      = ? all unicode characters ?;
```

If you need to use both single and double quotes inside a quoted argument, then you must escape one of them using `\` (backslash).
If you want to use `\` literally, then double it as `\\`. 
Backslash has a special meaning only inside a quoted argument, not in unquoted argument.

---