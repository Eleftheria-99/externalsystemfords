package dit.hua.dsexternalproject.externalcontrollers;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import dit.hua.project.service.ServiceInterface_for_student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dit.hua.dsexternalproject.entities.Final_Ranking_Oik;
import dit.hua.dsexternalproject.entities.Users;

@Controller
public class External_StudentSeeResultsController {

	private Response response = null;
	private String host = "http://localhost:8081/DistributedSystems/api/";
	private Final_Ranking_Oik returned_finalranking_form = null;
	
	// The singleton HTTP client.
	protected OkHttpClient client = null;
	ObjectMapper objectMapper = null;

	@RequestMapping(value = "login/main-menu-for-all/student-menu/showResults", method = RequestMethod.GET)
	protected String SeeResults(Model model, HttpSession session) {
		System.out.println("ready to show: see results page for students");
		client = new OkHttpClient();

		// username = request.getParameter("username");
		String url_for_get_request = host +"login/main-menu-for-all/showResults";
		
		returned_finalranking_form = new Final_Ranking_Oik();
		returned_finalranking_form = okhttp_get_request_for_final_ranking(url_for_get_request) ;
		
		if (returned_finalranking_form.getFname() == "not-found") {
			model.addAttribute("Points", "You were not in the Final Ranking list!");
		} else {
			model.addAttribute("Points", "Points");
			model.addAttribute("points", returned_finalranking_form.getPoints());
			model.addAttribute("size", "40");
			model.addAttribute("Rank", "Rank");
			model.addAttribute("rank", returned_finalranking_form.getId());
		}
		// session.setAttribute("department",returned_finalranking_form.get);

		try {
			response.body().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "st-results";
	}
	
	
	protected Final_Ranking_Oik okhttp_get_request_for_final_ranking(String url_for_get_request) {
		Request okhttp_request = new Request.Builder()
				.url(url_for_get_request)
				.build();
	
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
	
		System.out.println("SeeResults onResponse: " + responseData);
		
		Gson gson = new Gson();
		returned_finalranking_form = new Final_Ranking_Oik();
		
		try {
			returned_finalranking_form  = gson.fromJson(responseData, Final_Ranking_Oik.class);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return returned_finalranking_form ;
	}
	

}
