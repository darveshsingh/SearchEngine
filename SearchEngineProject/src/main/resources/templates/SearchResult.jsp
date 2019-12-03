<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<title>Search Engine 2019</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
<link href="https://fonts.googleapis.com/css?family=Lato"
	rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Montserrat"
	rel="stylesheet" type="text/css">
<!-- Load icon library -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<style>
/* Style the submit button */
form.searchForm button {
	float: left;
	width: 20%;
	padding: 10px;
	background: #2196F3;
	color: white;
	font-size: 17px;
	border: 1px solid grey;
	border-left: none; /* Prevent double borders */
	cursor: pointer;
}

form.searchForm button:hover {
	background: #0b7dda;
}

th {
	color: #0b7dda;
}

h1 {
	margin: 10px 0 30px 0;
	letter-spacing: 10px;
	font-size: 50px;
	color: #0b7dda;
}

.container {
	padding: 80px 120px;
}

.btn {
	padding: 10px 20px;
	background-color: #333;
	color: #f1f1f1;
	border-radius: 0;
	transition: .2s;
}

.btn:hover, .btn:focus {
	border: 1px solid #333;
	background-color: #fff;
	color: #000;
}

.nav-tabs li a {
	color: #777;
}

.navbar {
	font-family: Montserrat, sans-serif;
	margin-bottom: 0;
	background-color: #0b7dda;
	border: 0;
	font-size: 11px !important;
	letter-spacing: 4px;
	opacity: 0.9;
}

.navbar li a, .navbar .navbar-brand {
	color: #d5d5d5 !important;
}

.navbar-nav li a:hover {
	color: #fff !important;
}

.navbar-nav li.active a {
	color: #fff !important;
	background-color: #29292c !important;
}

.navbar-default .navbar-toggle {
	border-color: transparent;
}

footer {
	background-color: #0b7dda;
	color: #f5f5f5;
	padding: 18px;
}

footer a {
	color: #f5f5f5;
}

footer a:hover {
	color: #777;
	text-decoration: none;
}

.form-control {
	border-radius: 0;
}
</style>
</head>
<body class="">
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar"></button>
				<a class="navbar-brand" href="#searchPage">Advanced Computing
					Concepts - Search Engine 2019</a>
			</div>
		</div>
	</nav>
	<div id="contact" class="container">
		<h1 class="text-center">SEARCH</h1>
		<div class="row">
			<div class="col-lg-offset-2">
				<form class="searchForm" action="#" th:action="@{/search}"
					th:object="${searchQuery}" method="post">
					<div class="col-sm-9  form-group-lg">
						<input class="form-control" placeholder="Search here" type="text"
							th:field="*{message}" required />
					</div>
					<span class="input-group-btn">
						<button type="submit">
							<i class="fa fa-search"></i>
						</button>
					</span>
				</form>
			</div>
		</div>
		<br> <br> <br>
		<div class="row">
			<table class="table table-striped">
				<tr>
					<th><b>File Name</b></th>
					<th><b>Line</b></th>
					<th><b>Count</b></th>
					<th><b>Similar Words Found</b></th>
				</tr>

				<tr th:each="searchResults : ${searchResults}">
					<td th:text="${searchResults.fileName}"></td>
					<td th:text="${searchResults.stringLine}"></td>
					<td th:text="${searchResults.count}"></td>
					<td th:text="${searchResults.matchingWords}"></td>
				</tr>
			</table>
		</div>
	</div>
	<footer class="text-center navbar-fixed-bottom">
		<div class="col-sm-12 text-center"
			style="font-family: 'Open Sans'; font-size: 12px;">&copy;2019
			Search Engine. All Rights Reserved</div>
	</footer>

</body>
</html>