package dit.hua.dsexternalproject.externalcontrollers;

import java.io.IOException;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
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

	@RequestMapping(value = "login/main-menu-for-all/student-menu/showForm", method = RequestMethod.GET)
	protected String fillForm(HttpServletRequest request, Model model, HttpSession session) {

		String url = "http://localhost:8083/DistributedSystems/api/login/main-menu-for-all/showForm";
		returnDep(request, model, session, url);
		return "st-form";
	}

	@RequestMapping(value = "login/main-menu-for-all/student-menu/showForm/StudentForm", method = RequestMethod.GET)
	protected String showSubmittedForm(HttpServletRequest request, Model model, HttpSession session) {
		Response response = null;
		client = new OkHttpClient();
	
		get_all_the_parameters_from_the_form_and_session(request, model,session);
		
		SubmittedForm_Oik form = new SubmittedForm_Oik(username, fname, lname, email, phoneNumber, placeOfResidence, placeOfStudying,
				department,int_year_of_attendance, familyStatus, siblingsStudying, annualIncome, unemployedParents);
	
		objectMapper = new ObjectMapper();
		String json = null;
		json = "{\"username\": \""+form.getUsername()+"\","
				+"\"Fname\": \""+form.getFname()+"\","
				+"\"Lname\": \""+form.getLname()+"\","
				+"\"Email\": \""+form.getEmail()+"\","
				+"\"PhoneNumber\": "+form.getPhoneNumber()+","
				+"\"PlaceOfResidence\": \""+form.getPlaceOfResidence()+"\","
				+"\"PlaceOfStudying\": \""+form.getPlaceOfStudying()+"\","
				+"\"Department\": \""+form.getDepartment()+"\","
				+"\"YearOfAttendance\": "+form.getYearOfAttendance()+","
				+"\"FamilyStatus\": \""+form.getFamilyStatus()+"\","
				+"\"SiblingsStudying\": "+form.getSiblingsStudying()+","
				+"\"AnnualIncome\": \""+form.getAnnualIncome()+"\","
				+"\"UnemployedParents\": "+form.getUnemployedParents()+","
  
        + "}";

		  System.out.println("The json is : " + json);
//		try {
//			json = objectMapper.writeValueAsString(form);
//		} catch (JsonProcessingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//						
		String url = "http://localhost:8083/DistributedSystems/api/login/main-menu-for-all/showForm/StudentForm";
		//send the object to the url of the rest controller to save them into the database
		String postResponse = null;

		//postResponse = doPostRequest(url, json);
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request okhttp_request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		//Response response = null;
		//String responxe_json = null;
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
		
        System.out.println("The post response is : " + postResponse);
		
		return "show-submitted-form";
	}

	@RequestMapping(value = "login/main-menu-for-all/student-menu/change-data", method = RequestMethod.GET)
	protected String ChangePersonalData(HttpServletRequest request, Model model, HttpSession session) {

		String url = "http://localhost:8083/DistributedSystems/api/login/main-menu-for-all/change-data";
		returnDep(request, model, session, url);
		return "st-change";
	}

	protected void returnDep(HttpServletRequest request, Model model, HttpSession session, String url) {
		Response response = null;
		client = new OkHttpClient();

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
		Users returned_user = gson.fromJson(responseData, Users.class);

		model.addAttribute("department", returned_user.getDepartment().toString());

		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void get_all_the_parameters_from_the_form_and_session(HttpServletRequest request, Model model,
			HttpSession session) {
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

	protected String doPostRequest(String url, String json)  {
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request okhttp_request = new Request.Builder().url(url).post(body).build();
		Response response = null;
		String responxe_json = null;
		try {
			response = client.newCall(okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 responxe_json = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responxe_json;
	}

}
