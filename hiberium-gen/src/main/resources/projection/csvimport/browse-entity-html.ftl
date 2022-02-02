<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">

<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<script type="text/javascript" th:src="@{/webjars/jquery/3.2.1/jquery.min.js}"></script>
<link rel="stylesheet" type="text/css" th:href="@{/webjars/bootstrap/4.6.1/css/bootstrap.min.css}"/>
<script type="text/javascript" th:src="@{/webjars/bootstrap/4.6.1/js/bootstrap.min.js}"></script>
<title>Hiberium Browser</title>
</head>

<body>
<nav class="navbar navbar-dark bg-dark">
<a class="navbar-brand" th:text="'Hiberium Browser : ' + ${r"$"}{entity}">Hiberium Browser</a>
<span class="navbar-text" th:text="'Total : ' + ${r"$"}{totalItems}">Total</span>
</nav>

<div class="table-responsive">
<table class="table table-sm table-striped text-center">
<thead class="thead-light">
<tr>
<th scope="col">Edit</th>
<th scope="col" th:each="header: ${r"$"}{headers}" th:text="${r"$"}{header}"></th>
</tr>
</thead>

<tbody>
<tr th:each="row: ${r"$"}{results}">
<td><a target="_edit" th:href="@{'/' + ${r"$"}{apiPath} + '/form'(id=${r"$"}{row.get('id')})}">Edit</a></td>
<td th:each="col: ${r"$"}{headers}" th:text="${r"$"}{row.get(col)}"></td>
</tr>
</tbody>
</table>
</div>

<nav aria-label="Browser page navigation" th:if="${r"$"}{pageMax != 1}">
<ul class="pagination pagination-sm">
    <li class="page-item"><a class="page-link" th:if="${r"$"}{pageNum != 1}"
        th:href="@{${r"$"}{entity}(pageNum=1,perPage=${r"$"}{perPage},query=${r"$"}{query})}"
    >First</a></li>
    <li class="page-item"><a class="page-link" th:if="${r"$"}{pageNum != 1}" th:text="${r"$"}{pageNum - 1}"
        th:href="@{${r"$"}{entity}(pageNum=${r"$"}{pageNum-1},perPage=${r"$"}{perPage},query=${r"$"}{query})}"
    >Prev</a></li>
    <li class="page-item"><a class="page-link" th:text="${r"$"}{pageNum}"
        th:href="@{${r"$"}{entity}(pageNum=${r"$"}{pageNum},perPage=${r"$"}{perPage},query=${r"$"}{query})}"
    >Current</a></li>
    <li class="page-item"><a class="page-link" th:if="${r"$"}{pageNum != pageMax}" th:text="${r"$"}{pageNum + 1}"
        th:href="@{${r"$"}{entity}(pageNum=${r"$"}{pageNum+1},perPage=${r"$"}{perPage},query=${r"$"}{query})}"
    >Next</a></li>
    <li class="page-item"><a class="page-link" th:if="${r"$"}{pageNum != pageMax}"
        th:href="@{${r"$"}{entity}(pageNum=${r"$"}{pageMax},perPage=${r"$"}{perPage},query=${r"$"}{query})}"
    >Last</a></li>
</ul>
</nav>

</body>
</html>