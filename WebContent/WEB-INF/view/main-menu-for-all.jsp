<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- <link rel="stylesheet" type="text/css" href="styles_for_supervisor.css"> -->
<!--  <link href="<c:url value="/WEB-INF/resources/css/styles_for_supervisor.css"/>" rel="stylesheet"></link>  -->
<!DOCTYPE html>
<html>
<head>
 <style> <%@include file="/WEB-INF/resources/css/style.css"%></style> 

<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
body {
	min-height: 50vh;
	position: relative;
}
</style>
<title>Main menu for all users</title>
</head>
<body class="menu">
	<div class="body-wrapper">
		<div class="menu" id="options">
			${message } <br> ${username} <br> <br> <br> <br>
			<br /> <br /> <br /> <br /> <br />
		<b>	<h2>Hello student! Here you can do the following options:</h2></b>
			<br /> <br /> <br />

			

				<br /> Choose one of the following options! <br /> <br /> <br />
				<fieldset class="field">
				<br/><br/>
					1. Fill in the <a
						href="/DSExternal/login/main-menu-for-all/student-menu/showForm">form</a>
					in order to get free broad at the University! <br /> 2. Update
					your submitted form and <a
						href="/DSExternal/login/main-menu-for-all/student-menu/change-data">change</a>
					your personal data! <br /> 3. See the <a
						href="/DSExternal/login/main-menu-for-all/student-menu/showResults">points</a>
					of your form and your place in the ranking with the other students!
				<br/><br/>
				</fieldset>


		</div>
	</div>