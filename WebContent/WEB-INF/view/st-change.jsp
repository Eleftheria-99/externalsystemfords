<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/resources/css/form-style.css"%></style>
<title>Change Personal Data</title>
</head>
<body>
	<sec:authorize access="isAuthenticated()">
		<div class="ui segment">
		<pre>
	                     	                                                                                                                  <a href="/DistributedSystems/just-logged-out">Logout</a>
	</pre>
			User:
			<sec:authentication property="principal.username" />
			, Role:
			<sec:authentication property="principal.authorities" />
		</div>
	</sec:authorize>
	<div  class="center" >
	<h3>Here you can change some of your personal data, so we can be
		able to communicate with you!</h3>
	
	Please fill in the new data below in English:</div>

	<form action="/DSExternal/login/main-menu-for-all/student-menu/change-data/newForm" method="get">
	<fieldset class="field">
		<legend><h3>Department :<i> ${department}</i></h3></legend>
		<table>
			<tbody>
				<tr>
					<td>E-mail :</td>
					<td><input class="textarea" name="email" type="text" size="30" value="" required /></td>
				</tr>
				<tr>
					<td>Phone number :</td>
					<td><input class="textarea" name="phonenumber" type="number" size="30" value=""
						required /></td>
				</tr>
				<tr>
					<td>Place of residence :</td>
					<td><input class="textarea" name="placeofliving" type="text" size="30" value="" required ></td>
				</tr>
			</tbody>
		</table>
		</fieldset>
		<center><p class="submit input">
		<br /><input  type="submit" value="Submit Form" /> <br /></p></center>
	</form>
	