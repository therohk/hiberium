<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">

<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<script type="text/javascript" th:src="@{/webjars/jquery/3.2.1/jquery.min.js}"></script>
<link rel="stylesheet" type="text/css" th:href="@{/webjars/bootstrap/4.6.1/css/bootstrap.min.css}"/>
<script type="text/javascript" th:src="@{/webjars/bootstrap/4.6.1/js/bootstrap.min.js}"></script>
<title>${concept_name} Form</title>
</head>

<body>
<#assign primary_key = attributes?filter(a -> a.attribute_role?contains("K"))?first>
<nav class="navbar navbar-dark bg-dark justify-content-between">
    <a class="navbar-brand">${concept_name} Form</a>
    <form class="form-inline" th:action="@{/${concept_apipath}/form}" method="get">
        <input class="form-control form-control-sm mr-sm-2" type="number" id="id" name="id" placeholder="${primary_key.attribute_name}" aria-label="Search">
        <button class="btn btn-outline-success btn-sm my-2 my-sm-0" type="submit">Search</button>
    </form>
</nav>

<br>
<br>

<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <h2 <#if concept_desc??>title="${concept_desc}"</#if>>${concept_name}</h2>
            <form th:action="@{/${concept_apipath}/submit}" th:object="${r"$"}{${concept_varname}}" method="post">
            <fieldset>
                <div class="alert alert-danger" th:if="${r"$"}{#fields.hasGlobalErrors()}">
                    <p th:each="error : ${r"$"}{#fields.errors('global')}" th:text="${r"$"}{error}"></p>
                </div>
<#list attributes as attribute>
                <div class="form-group">
                    <@printlabel attribute=attribute/><#nt>
                    <@printinput attribute=attribute/><#nt>
                    <@printerror attribute=attribute/><#nt>
                </div>
</#list>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary" id="form-submit">Submit</button>
                    <div class="btn-group" role="group" aria-label="Form Options">
                        <a role="button" class="btn btn-secondary" th:href="@{/${concept_apipath}/form}" id="form-clear">Clear</a>
                        <button type="reset" class="btn btn-secondary" id="form-reset">Reset</button>
                        <a role="button" class="btn btn-secondary" id="form-clone">Clone</a>
                    </div>
                </div>
            </fieldset>
            </form>
        </div>
    </div>
</div>

<script>
document.getElementById('form-clone').onclick = function() {
    document.getElementById('${primary_key.attribute_name}').value = "";
};
</script>
</body>
</html>

<#macro printlabel attribute>
<@compress single_line=true>
<label class="control-label" th:for="${attribute.attribute_name}"
<#if attribute.attribute_desc??>title="${attribute.attribute_desc}"</#if>>
${attribute.attribute_name}
</label>
</@compress>
</#macro>

<#macro printinput attribute>
<@compress single_line=true>
<#assign number_types = ['Integer', 'Long', 'Float', 'Double', 'BigInteger', 'BigDecimal']>
<#if attribute.field_type == "text">
<textarea class="form-control" th:field="*{${attribute.attribute_name}}" rows="3"></textarea>
<#elseif number_types?seq_contains(attribute.attribute_java)>
<input class="form-control" type="number" th:field="*{${attribute.attribute_name}}" <#if attribute.attribute_role?contains("K")>readonly</#if> />
<#elseif attribute.attribute_java == "Date">
<input class="form-control" type="datetime-local" th:field="*{${attribute.attribute_name}}" />
<#elseif attribute.attribute_java == "Boolean">
<input class="form-control" type="checkbox" th:field="*{${attribute.attribute_name}}" />
<#else>
<input class="form-control" type="text" th:field="*{${attribute.attribute_name}}" />
</#if>
</@compress>
</#macro>

<#macro printerror attribute>
<@compress single_line=true>
<p class="alert alert-danger" role="alert" th:if="${r"$"}{#fields.hasErrors('${attribute.attribute_name}')}" th:errors="*{${attribute.attribute_name}}">Validation error</p>
</@compress>
</#macro>
