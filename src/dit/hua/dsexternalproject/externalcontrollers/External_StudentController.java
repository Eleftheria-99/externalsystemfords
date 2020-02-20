package dit.hua.dsexternalproject.externalcontrollers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import dit.hua.dsexternalproject.entities.SubmittedForm_Oik;
import dit.hua.dsexternalproject.entities.Users;

@Controller
public class External_StudentController {

	// parameters of the form
	private String username = null;
	private String fname = null;
	private String lname = null;
	private String string_year_of_attendance = null;
	private int int_year_of_attendance = 0;
	private String string_number_of_siblings_studying = null;
	private int siblingsStudying = 0;
	private String email = null;
	private String string_phone_number = null;
	private int phoneNumber = 0;
	private String department = null;
	private String string_number_of_unemployed_parents = null;
	private int unemployedParents = 0;
	private String familyStatus = null;
	private String placeOfResidence = null;
	private String placeOfStudying = null;
	private String annualIncome = null;

	// The singleton HTTP client.
	protected OkHttpClient client = null;
	protected ObjectMapper objectMapper = null;

	protected Response response = null;
	private String host = "http://localhost:8081/DistributedSystems/api/";
	protected String postResponse = null;
	protected String json = null;

	// for when the user decides to chnage some the data form
	protected String changed_email = null;
	protected String changed_phone_number = null;
	protected int int_changed_phoneNumber = 0;
	protected String changed_place_of_residence = null;
	protected String department_for_changed_data_form = null;
	protected String putResponse = null;

	@RequestMapping(value = "login/main-menu-for-all/student-menu/showForm", method = RequestMethod.GET)
	protected String fillForm(HttpServletRequest request, Model model, HttpSession session) {

		String url = host + "login/main-menu-for-all/showForm";
		String department_sent_from_server = returnDep(request, model, session, url);

		model.addAttribute("department", department_sent_from_server);

		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "st-form";
	}

	@RequestMapping(value = "login/main-menu-for-all/student-menu/showForm/StudentForm", method = RequestMethod.POST)
	protected String showSubmittedForm(HttpServletRequest request, Model model, HttpSession session) {
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();

		get_all_the_parameters_from_the_form_and_session(request, model, session);

		SubmittedForm_Oik form = new SubmittedForm_Oik(username, fname, lname, email, phoneNumber, placeOfResidence,
				placeOfStudying, department, int_year_of_attendance, familyStatus, siblingsStudying, annualIncome,
				unemployedParents);

		json = return_string_that_contains_json_from_object_PUT_REQUEST(form);
		// json = return_string_that_contains_json_from_object(form); // the method
		// returns a string that contains a json

		System.out.println("The json is : " + json);

		// POST OKHTTP REQUEST -- send the object to the url of the rest controller to
		// save them into the database
		String url = host + "login/main-menu-for-all/showForm/StudentForm";

		postResponse = doPostRequest(url, json); // send the info to the server, in order the data to be submitted in
													// the database

		System.out.println("The post response is : " + postResponse);

		SubmittedForm_Oik newForm = new SubmittedForm_Oik();

		Gson gson = new Gson();
		postResponse.trim();
		newForm = gson.fromJson(postResponse, SubmittedForm_Oik.class);
		if (newForm.getDepartment().equals("exists")) {
			model.addAttribute("error", "You have already submitted your form!");
			department = (String) session.getAttribute("department");
			newForm.setDepartment(department);
		}
		System.out.println("NEW FORM AFTER GSON IS:  " + newForm.toString());
		// add them in the form , so that the user can see the submitted form
		add_parameters_in_the_form_and_show_the_user_the_submitted_form(newForm, model);
		return "show-submitted-form";
	}

	@RequestMapping(value = "login/main-menu-for-all/student-menu/change-data", method = RequestMethod.GET)
	protected String ChangePersonalData(HttpServletRequest request, Model model, HttpSession session) {

		String url = host + "login/main-menu-for-all/change-data";
		String department_sent_from_server = returnDep(request, model, session, url);

		model.addAttribute("department", department_sent_from_server);

		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "st-change"; // returns the form that needs to be written by the student, having his/ her
							// department
	}

	@RequestMapping(value = "login/main-menu-for-all/student-menu/change-data/newForm", method = RequestMethod.GET)
	protected String ChangedForm(HttpServletRequest request, Model model, HttpSession session) {
		// get the parameters from the form, send them to the server with put okhttp
		// request, and add them to the model, so that the user can see the form with
		// the changed data that was submitted
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();

		get_parameters_from_the_changed_data_form(request); // get the parameters from the form with the changed data

		String url = host + "login/main-menu-for-all/showForm";
		department_for_changed_data_form = returnDep(request, model, session, url); // get the department of the user

		username = (String) session.getAttribute("username");
		// convert object to string that contains a json
		SubmittedForm_Oik form = new SubmittedForm_Oik(username, changed_email, int_changed_phoneNumber,
				changed_place_of_residence, department_for_changed_data_form);

		json = return_string_that_contains_json_from_object_PUT_REQUEST(form); // the method returns a string that
																				// contains a json
		System.out.println("DSExternal : ChangedForm - The json is : " + json);

		// PUT REQUEST --send to the server the data that the user wants to change in
		// his/her submitted form in the db
		String put_url = host + "login/main-menu-for-all/change-data/newForm";
		putResponse = doPutRequest(put_url, json);// send to the server whatever the user has written in the form for
													// changed data

		System.out.println("The put response is : " + putResponse);
		String notfound = "{\"error\":\"Sorry, you haven't submitted your form yet!\"}"; // MAKE IT JSON

		if(putResponse.equals(notfound)) {
			model.addAttribute("error","Sorry, you haven't submitted your form yet!");
		}else {
		SubmittedForm_Oik newForm = new SubmittedForm_Oik();
		Gson gson = new Gson();
		putResponse.trim();

		newForm = gson.fromJson(putResponse, SubmittedForm_Oik.class);

		add_parameters_in_the_form_and_show_the_user_the_submitted_form(newForm, model);
		}
		return "show-submitted-form";
	}

	protected String returnDep(HttpServletRequest request, Model model, HttpSession session, String url) {

		System.out.println(
				"method returnDep is beginning,about to retrieve the department of the user found in db and sent from server to client ");
		client = new OkHttpClient();
		// GET OKHTTP REQUEST
		Request okhttp_request = new Request.Builder().url(url).build();
		try {
			response = client.newCall(okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String string_response =response.body().toString(); /// = auth.getName();
		String responseData = null;

		try {
			responseData = response.body().string();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("onResponse: " + responseData);
		Gson gson = new Gson();
		Users returned_user = new Users();
		try {
			returned_user = gson.fromJson(responseData, Users.class);
		} catch (Exception e) {
			e.getStackTrace();
		}
		// model.addAttribute("department", returned_user.getDepartment().toString());
		return returned_user.getDepartment();

	}

	protected void get_all_the_parameters_from_the_form_and_session(HttpServletRequest request, Model model,
			HttpSession session) {
		// gets all the parameters from the form that the user submitted
		fname = request.getParameter("fname");
		lname = request.getParameter("lname");
		email = request.getParameter("email");

		string_phone_number = request.getParameter("phonenumber");

		if (string_phone_number == null || string_phone_number.length() == 0) {
			phoneNumber = -1; // in case of error
		} else {
			try {
				phoneNumber = Integer.parseInt(string_phone_number);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		placeOfResidence = request.getParameter("placeofliving");
		placeOfStudying = request.getParameter("placeofstudying");
		annualIncome = request.getParameter("FinancialIncome");

		string_number_of_siblings_studying = request.getParameter("numofsiblingswhostudy");

		if (string_number_of_siblings_studying == null || string_number_of_siblings_studying.length() == 0) {
			siblingsStudying = -1; // in case of error
		} else {
			try {
				siblingsStudying = Integer.parseInt(string_number_of_siblings_studying);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		familyStatus = request.getParameter("FamilyStatus");

		string_year_of_attendance = request.getParameter("yearofattendance");

		if (string_year_of_attendance == null || string_year_of_attendance.length() == 0) {
			int_year_of_attendance = -1; // in case of error
		} else {
			try {
				int_year_of_attendance = Integer.parseInt(string_year_of_attendance);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		string_number_of_unemployed_parents = request.getParameter("UnemployedParents");

		if (string_number_of_unemployed_parents == null || string_number_of_unemployed_parents.length() == 0) {
			unemployedParents = -1; // -1 means that the id does not exist or was not retrieved
		} else {
			try {
				unemployedParents = Integer.parseInt(string_number_of_unemployed_parents);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		username = (String) session.getAttribute("username");
		System.out.println("INSIDE PLAIN CONTROLLER USERNAME:   " + username);

		department = (String) session.getAttribute("department");
	}

	/*
	 * protected String
	 * return_string_that_contains_json_from_object(SubmittedForm_Oik form) { // the
	 * method creates and returns a string that conatains all the parameters // from
	 * the form that the user submitted
	 * 
	 * json = "{\"username\": \"" + form.getUsername() + "\"," + "\"Fname\": \"" +
	 * form.getFname() + "\"," + "\"Lname\": \"" + form.getLname() + "\"," +
	 * "\"Email\": \"" + form.getEmail() + "\"," + "\"PhoneNumber\": " +
	 * form.getPhoneNumber() + "," + "\"PlaceOfResidence\": \"" +
	 * form.getPlaceOfResidence() + "\"," + "\"PlaceOfStudying\": \"" +
	 * form.getPlaceOfStudying() + "\"," + "\"Department\": \"" +
	 * form.getDepartment() + "\"," + "\"YearOfAttendance\": " +
	 * form.getYearOfAttendance() + "," + "\"FamilyStatus\": \"" +
	 * form.getFamilyStatus() + "\"," + "\"SiblingsStudying\": " +
	 * form.getSiblingsStudying() + "," + "\"AnnualIncome\": \"" +
	 * form.getAnnualIncome() + "\"," + "\"UnemployedParents\": " +
	 * form.getUnemployedParents()
	 * 
	 * + "}";
	 * 
	 * // try { // json = objectMapper.writeValueAsString(form); // } catch
	 * (JsonProcessingException e1) { // // TODO Auto-generated catch block //
	 * e1.printStackTrace(); // } return json; }
	 */

	protected String doPostRequest(String url, String json) { // send to the server whatever the user has written in the
																// form
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request okhttp_request = new Request.Builder().url(url).post(body).build();

		try {
			response = client.newCall(okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			postResponse = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postResponse;
	}

	protected void add_parameters_in_the_form_and_show_the_user_the_submitted_form(SubmittedForm_Oik form,
			Model model) { // add them in the form , so that the user can see the submitted form
		model.addAttribute("fname", form.getFname());
		model.addAttribute("lname", form.getLname());
		model.addAttribute("email", form.getEmail());
		model.addAttribute("phone", form.getPhoneNumber());
		model.addAttribute("pofresidence", form.getPlaceOfResidence());
		model.addAttribute("pofstudying", form.getPlaceOfStudying());
		model.addAttribute("dep", form.getDepartment());
		model.addAttribute("year", form.getYearOfAttendance());
		model.addAttribute("family", form.getFamilyStatus());
		model.addAttribute("siblings", form.getSiblingsStudying());
		model.addAttribute("income", form.getAnnualIncome());
		model.addAttribute("parents", form.getUnemployedParents());
	}

	protected void get_parameters_from_the_changed_data_form(HttpServletRequest request) { // get the parameters from
																							// the form with the changed
																							// data
		changed_email = request.getParameter("email");
		int_changed_phoneNumber = Integer.parseInt(request.getParameter("phonenumber"));
		System.out.println("PHONE BEFORE: " + changed_phone_number);
//		if (string_phone_number == null || string_phone_number.length() == 0) {
//			int_changed_phoneNumber = -1; // in case of error
//		} else {
//			try {
//				int_changed_phoneNumber = Integer.parseInt(string_phone_number);
//				System.out.println("PHONE in try: "+int_changed_phoneNumber);
//
//			} catch (Exception e) {
//				e.getStackTrace();
//			}
//		}
		changed_place_of_residence = request.getParameter("placeofliving"); // place of residence
	}

	protected String return_string_that_contains_json_from_object_PUT_REQUEST(SubmittedForm_Oik form) {
		// the method creates and returns a string that contains all the parameters from
		// the form with the changed data that the user submitted

		try {
			json = objectMapper.writeValueAsString(form);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return json;
	}

	protected String doPutRequest(String url, String json) { // send to the server whatever the user has written in the
																// form for changed data
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request okhttp_request = new Request.Builder().url(url).put(body).build();

		try {
			response = client.newCall(okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			putResponse = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return putResponse;
	}

}
