package tw.eeit117.wematch.courses.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import tw.eeit117.wematch.courses.dao.CoursesDAOImpl;
import tw.eeit117.wematch.courses.dao.CurriculumDAO;
import tw.eeit117.wematch.courses.model.Courses;
import tw.eeit117.wematch.courses.model.Curriculum;
import tw.eeit117.wematch.courses.service.CoursesService;
import tw.eeit117.wematch.courses.service.CurriculumService;

@Controller
public class CurriculumController {
	
	
	private static final Logger logger = 
			Logger.getLogger(CurriculumController.class);
	
	public CurriculumController() {
		System.out.println("CurriculumController()");
	}
	
	
	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private CoursesService coursesService;
	
//		//列出所有"已選"課表
		@RequestMapping(value = "/ListCourses")
		public ModelAndView listCurriculum2(ModelAndView model) {	
			List<Curriculum> listCurriculum2 = curriculumService.getAllCurriculum();
			model.addObject("listCurriculum2",listCurriculum2);
			model.setViewName("ListCourses");		
			return model;
		}
	
		//列出所有課程
		@RequestMapping(value = "/addCourses")
		public ModelAndView listCurriculum(ModelAndView model) {	
			List<Courses> listCurriculum = coursesService.getAllCourses();
			model.addObject("listCurriculum",listCurriculum);
			//model.setViewName("addCourses");
			
			List<Curriculum> listCurriculum2 = curriculumService.getAllCurriculum();
			model.addObject("listCurriculum2",listCurriculum2);
			model.setViewName("addCourses");
			return model;
//			return new ModelAndView("redirect:/addCourses");
		}
		
		//加選課程
		@RequestMapping(value = "/addCurriculumBtn", method = RequestMethod.GET)
		public ModelAndView addCurriculumBtn(HttpServletRequest request,ModelAndView model,
				HttpSession session) {				
				//int memberId = 1;
				int memberId = (Integer)session.getAttribute("id");//Nara
				int coursesId = Integer.parseInt(request.getParameter("coursesId"));
			//	int memberId = Integer.parseInt(request.getParameter("memberId"));
				//select * from Courses where coursesId =:coursesId 
				Courses course = coursesService.getCourses(coursesId);
			
				Curriculum curriculum = new Curriculum();
				curriculum.setCoursesId(coursesId);
				curriculum.setMemberId(memberId);
				curriculum.setCoursesName(course.getCoursesName());;
				curriculum.setCoursesWeek(course.getCoursesWeek());
				curriculum.setSectionNumber(course.getSectionNumber());
				curriculum.setClassRoom(course.getClassRoom());
				
				
				//呼叫課程防呆
		    	boolean checkCurriculum = curriculumService.checkCourses(curriculum.getCoursesId());
 		    	
		   		if(checkCurriculum) {
		   			popAlert(request);
		   		}
		   		else {
		   			
		   			if(course.getNumberPeople()==0) {
						request.setAttribute("errorMessage", "課程已滿，請選擇別的課程謝謝🙏");
					}else {
						curriculumService.addCurriculum(curriculum);
			   			
			   			course.setNumberPeople(course.getNumberPeople()-1);
			   			course.setRegNumber(course.getRegNumber()+1);
			   			course.setCoursesBalance(course.getCoursesBalance()-1);
			   			coursesService.updateCourses(course);
		   			}
		   		}
		   		
				//listCurriculum(model);
				List<Courses> listCurriculum = coursesService.getAllCourses();
				model.addObject("listCurriculum",listCurriculum);
				
				//listCurriculum2(model);
				List<Curriculum> listCurriculum2 = curriculumService.getAllCurriculum();
				model.addObject("listCurriculum2",listCurriculum2);
				model.setViewName("addCourses");	
				
				return model;
	
				//return new ModelAndView("redirect:/addCourses");
		}
		
		//課程複選防呆
		@RequestMapping(value = "/errmsgCurriculum", method = RequestMethod.GET)
		public ModelAndView popAlert(HttpServletRequest request) {
			request.setAttribute("errorMessage", "課程已加選過囉!🚫請勿複選");
			return new ModelAndView("redirect:/addCourses");
			}
		
		//刪除
		@RequestMapping(value = "/deleteCurriculum", method = RequestMethod.GET)
		public ModelAndView deleteCurriculum(HttpServletRequest request) {
			int curriculumId = Integer.parseInt(request.getParameter("curriculumId"));
			// select * from Curriculum where curriculumId =: curriculumId
			Curriculum cur = curriculumService.getCurriculum(curriculumId);
			// select * from Courses where coursesId =: coursesId
			Courses course = coursesService.getCourses(cur.getCoursesId());

			curriculumService.deleteCurriculum(curriculumId);
			
			course.setNumberPeople(course.getNumberPeople()+1);
   			course.setRegNumber(course.getRegNumber()-1);
   			course.setCoursesBalance(course.getCoursesBalance()+1);
   			coursesService.updateCourses(course);

			return new ModelAndView("redirect:/ListCourses");
		}
		
		
	
		
}