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
	private String username_from_form =null;
	private String password_from_form = null;
	private Response response = null;
	private String json = null;
	private String host = "http://localhost:8081/DistributedSystems/api/";
	private String url_for_post_request =null;
	private String string_response_from_okhttp_request = null;
	private String url_for_get_request = null;
	private Users returned_user = null;
	
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
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
		
		//get the username and password from the login form
		password_from_form = request.getParameter("password");
		username_from_form= request.getParameter("username");

		//create new object with the username from the form
		Users user_username = new Users(username_from_form, null); // department = null

		//POST OKHTTP REQUEST
		url_for_post_request = host+"login/main-menu-for-all";
		
		string_response_from_okhttp_request = okhttp_post_request(url_for_post_request,  user_username);  //the method returns a string which contains a json 
	    System.out.println( "string_response: " +  string_response_from_okhttp_request );
		
		
		//GET OKHTTP REQUEST
	    url_for_get_request = host+"login/main-menu-for-all";
	   
	    returned_user = new Users();
	    returned_user = okhttp_get_request(url_for_get_request);  //returns object type of user that contains the username and the department of the user
		//model.addAttribute("message","This is the response"+ returned_user.toString() );
		model.addAttribute("message", "This is the student with name: "+ username_from_form+ " and studies in the department :"+ returned_user.getDepartment());
		session.setAttribute("username", username_from_form);
		session.setAttribute("department",returned_user.getDepartment());  //String department = student_service.findDepartment(username);	
		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "main-menu-for-all";
	}

	@RequestMapping(value = "/just-logged-out", method = RequestMethod.GET) //log out 
	public String logout(HttpSession session, Model model) {
		
		
		//POST REQUEST to let the server know that the user has logged out 
		url_for_post_request = host+"just-logged-out";
		
		Users user_username = new Users(username_from_form, null); // department = null
		
		okhttp_post_request(url_for_post_request, user_username);
		
		session.removeAttribute("username");
		model.addAttribute("log_out_message", "You have just logged out!");
		
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/login/error", method = RequestMethod.GET) //log in error 
	public String show_login_error(HttpSession session, Model model) {
		model.addAttribute("error", "Sorry! Invalid username/password!");
		return "redirect:/login";
	}
		
		
		protected String okhttp_post_request(String post_url, Users user_username) {
			
//			String plainClientCredentials=username+":"+password_from_form;
//			String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
//			 
//			HttpHeaders headers = getHeaders();
//			headers.add("Authorization", "Basic " + base64ClientCredentials);
			try {
				json = objectMapper.writeValueAsString(user_username);
				System.out.println("EXTERNAL /login/main-menu-for-all  json: " + json);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// post ok http request
			 RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
			Request post_okhttp_request = new Request.Builder().url(post_url)
					//.addHeader("Authorization", Credentials.basic(username_from_form, password_from_form)) 
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
			
			return  string_response;
		}
		
		 protected Users okhttp_get_request(String url_for_get_request) { //returns object type of user that contains the username and the department of the user 
				Request okhttp_request = new Request.Builder()
						.url(url_for_get_request )
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
				
			    System.out.println( "onResponse: " + responseData);  //response data is a json 
			    
			    Gson gson = new Gson(); 
			    returned_user = new Users();
			    try {
				    returned_user = gson.fromJson(responseData, Users.class);  //convert json to object type of Users
			    } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    return  returned_user;
			}
		
}
