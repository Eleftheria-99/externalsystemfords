package dit.hua.dsexternalproject.externalcontrollers;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import dit.hua.dsexternalproject.entities.*;

@Controller
public class External_HomePage_LogIn_MainMenuForAll_LogOut_Controller {
	String username_from_form =null;
	
	// The singleton HTTP client.
	protected OkHttpClient client = null; 
	ObjectMapper objectMapper = null; 
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String showHomePage(){	
			return "home-page";   //the user must click to the href: login in order to go to the login page
	}
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogInForAll() {	
		System.out.println("ready to show: log in page");	
		return "show-login-for-all";
	}

	
	@RequestMapping(value = "/login/main-menu-for-all", method = RequestMethod.POST)
	public String showMainMenuForAll(HttpServletRequest request, Model model, HttpSession session,Authentication auth) {
		System.out.println("ready to show: main menu for all users page");
		Response response = null;
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
		
		String password_from_form= request.getParameter("password");
		String username_from_form= request.getParameter("username");
		Users user_username = new Users(username_from_form, null); // department = null

		String json = null;
		
//		String plainClientCredentials=username+":"+password_from_form;
//		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
//		 
//		HttpHeaders headers = getHeaders();
//		headers.add("Authorization", "Basic " + base64ClientCredentials);
		try {
			json = objectMapper.writeValueAsString(user_username);
			System.out.println("EXTERNAL /login/main-menu-for-all  json: " + json);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String post_url = "http://localhost:8083/DistributedSystems/api/login/main-menu-for-all";
		// post ok http request
		 RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request post_okhttp_request = new Request.Builder().url(post_url)
				.addHeader("Authorization", Credentials.basic(username_from_form, password_from_form)) 
				.post(body)
				.build();

		String string_response = null;
		try {
			response = client.newCall(post_okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			string_response = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    System.out.println( "string_response: " + string_response);
		
		
		//GET OKHTTP REQUEST
		//username = request.getParameter("username");
		Request okhttp_request = new Request.Builder()
				.url("http://localhost:8083/DistributedSystems/api/login/main-menu-for-all")
				//.get(responsebod)            //requestbody
				.build();
		
		try {
			response = client.newCall(okhttp_request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String string_response =response.body().toString();                                 /// = auth.getName();
		String responseData =  null;
		
		try {
			responseData = response.body().string();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    System.out.println( "onResponse: " + responseData);
	    Gson gson = new Gson(); 
	    Users returned_user = new Users();
	    try {
		    returned_user = gson.fromJson(responseData, Users.class);
	    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//model.addAttribute("message","This is the response"+ returned_user.toString() );
		model.addAttribute("message", "This is the student with name: "+ returned_user.getUsername()+ " and studies in the department :"+ returned_user.getDepartment());
		session.setAttribute("username", username_from_form);
		session.setAttribute("department",returned_user.getDepartment());
		
		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//String department = student_service.findDepartment(username);	
		return "main-menu-for-all";
	}

	//log out   http://localhost/DistributedSystems
		@RequestMapping(value = "/just-logged-out", method = RequestMethod.GET)
		public String logout(HttpSession session, Model model) {
			session.removeAttribute("username");
			model.addAttribute("log_out_message", "You have just logged out!");
			return "redirect:/login";
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
