<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file ="/WEB-INF/resources/css/form-style.css"%> </style>

<title>Student form</title>
</head>
<body>
	<div class="center">

		${errormessage} <br /> <br />
		<h3 class="center">Fill in the form below in English:</h3>

		<form
			action="/DSExternal/login/main-menu-for-all/student-menu/showForm/StudentForm"
			method="POST">
			<fieldset class="field">
				<legend>
					<h3>
						Department :<i> ${department}</i>
					</h3>
				</legend>

				<table>
					<tbody>
						<tr>
							<td>First Name :</td>
							<td><input class="textarea" name="fname" type="text"
								size="30" value="" required /></td>
						</tr>
						<tr>
							<td>Last Name :</td>
							<td><input class="textarea" name="lname" type="text"
								size="30" value="" required /></td>
						</tr>
						<tr>
							<td>E-mail :</td>
							<td><input class="textarea" name="email" type="text"
								size="30" value="" required /></td>
						</tr>
						<tr>
							<td>Phone number :</td>
							<td><input class="textarea" name="phonenumber" type="number"
								size="30" value="" required /></td>
						</tr>
						<tr>
							<td>Place of residence :</td>
							<td><input class="textarea" name="placeofliving" type="text"
								size="30" value=""></td>
						</tr>
						<tr>
							<td>Place of studying :</td>
							<td><input class="textarea" name="placeofstudying"
								type="text" size="30" value=""></td>
						</tr>

						<tr>
							<td>Year of attendance :</td>
							<td><input class="textarea" name="yearofattendance"
								type="number" size="30" value="" /></td>
						</tr>
						<tr>
							<td>Family status :</td>
							<td><select class="textarea" name="FamilyStatus">
									<option value="Single">Single</option>
									<option value="Married">Married</option>
									<option value="Widowed">Widowed</option>
									<option value="Divorced">Divorced</option>
							</select></td>
						</tr>
						<tr>
							<td>Number of siblings who study :</td>
							<td><input class="textarea" name="numofsiblingswhostudy"
								type="number" size="30" value=""></td>
						</tr>
						<tr>
							<td>Family annual financial income :</td>
							<td><select class="textarea" name="FinancialIncome">
									<option value="zero">0</option>
									<option value="lower than 10.000">lower than 10.000</option>
									<option value="10.000 - 15.000">10.000 - 15.000</option>
									<option value="higher than 15.000">higher than 15.000</option>
							</select></td>
						</tr>
						<tr>
							<td>Number of unemployed parents :</td>
							<td><select class="textarea" name="UnemployedParents">
									<option value="0">0</option>
									<option value="1">1</option>
									<option value="2">2</option>
							</select></td>
						</tr>

					</tbody>
				</table>
			</fieldset>
	
				<p class="submit input">
				<br /> <input type="submit" name="SubmitButton" id="SubmitButton"
					value="Submit Form" /><br />
			</p>
			
		</form>
	</div>