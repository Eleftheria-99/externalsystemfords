<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
 <style> <%@include file="/WEB-INF/resources/css/style.css"%></style> 

<meta charset="ISO-8859-1">
<title>Points and Rank</title>

</head>
<body class="menu">
	<pre>
	                          
	                                                                                                                  <a href="/DSExternal/just-logged-out">Logout</a>
	</pre>

	<h3>Here you can see your points and rank if your form has been
		processed and accepted!</h3>
	<br/> <br/> <br/>
	<center><font color="#000080"><h3> ${notfound} </h3></font></center>
	<fieldset class="field" style="width: 200px;">
		<table>
			<tbody>
				<tr>
					<td><br />
					<br />
					<br />${Points} ${points} ${size}</td>

				</tr>
				<tr>
					<td>${Rank} ${rank}<br />
					<br />
					<br /></td>

				</tr>
			</tbody>
		</table>
	</fieldset>
	