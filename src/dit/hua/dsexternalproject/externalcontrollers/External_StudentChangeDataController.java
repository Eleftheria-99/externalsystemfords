package dit.hua.dsexternalproject.externalcontrollers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import dit.hua.project.service.ServiceInterface_for_student;

@Controller
public class External_StudentChangeDataController {


	@RequestMapping(value = "login/main-menu-for-all/student-menu/change-data/newForm", method = RequestMethod.GET)
	protected String ChangedForm(HttpServletRequest request, Model model,HttpSession session) {
		 //student_service.student_service_ChangedForm(request, model, session);
		 return "show-submitted-form";
	}
}
