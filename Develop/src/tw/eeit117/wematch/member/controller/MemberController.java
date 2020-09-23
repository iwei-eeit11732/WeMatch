package tw.eeit117.wematch.member.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import tw.eeit117.wematch.member.model.Member;
import tw.eeit117.wematch.member.model.MemberService;

@Controller("MemberController")
@SessionAttributes(names = { "MemberAccount", "MemberPwd", "Member", "MemberStatus" })
public class MemberController {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/homepage")
	public String homepage() {
		return "HomePage";
	}

	@GetMapping("/index")
	public String index(HttpSession session) {
		session.invalidate();
		return "SignInPage";
	}
	
	@GetMapping("/memberPage")
	public String memberPage(HttpSession session) {
		Member member = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
		session.setAttribute("Account", member.getMemberAccount());
		session.setAttribute("name", member.getMemberName());
		session.setAttribute("nickname", member.getNickname());
		session.setAttribute("gender", member.getGender());
		session.setAttribute("email", member.getMemberEmail());
		session.setAttribute("birthday", member.getBirthdayDate());
		session.setAttribute("starSign", member.getStarSign());
		session.setAttribute("city", member.getCity());
		session.setAttribute("booldtype", member.getBloodType());
		session.setAttribute("hobbies", member.getHobbies());
		session.setAttribute("selfinfo", member.getSelfIntro());

		return "MemberPage";
	}
	
	@GetMapping("/loginPage")
	public String loginPage() {
		return "SignInPage";
	}

	@GetMapping("/register")
	public String register() {
		return "registerPage";
	}

	@PostMapping("/loginsystem.controller")
	public String checkLogin(HttpServletRequest request,
			@PathVariable @RequestParam("memberAccount") String myUser,
			@PathVariable @RequestParam("memberPwd") String myPwd,
			Model m, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("errors", errors);

		// MemberPage
		if (myUser == null || myUser.length() == 0) {
			errors.put("name", "name is required");
		}
		if (myPwd == null || myPwd.length() == 0) {
			errors.put("pwd", "password is required");
		}
		if (errors != null && !errors.isEmpty()) {
			return "SignInPage";
		}

		Boolean checkUser = memberService.checkLogin(new Member(myUser, myPwd));
		if (checkUser) {
			Member users = memberService.selectMember(myUser, myPwd);
			int id = users.getMemberId();
			// AdminPage
			if (users.getMemberStatus() == 2) {
				session.setAttribute("Account", myUser);
				session.setAttribute("Status", users.getMemberStatus());
				session.setAttribute("id", id);
				
				List<Member> m1 = memberService.selectAllMember();		
				m.addAttribute("results", m1);
				return "MemberAdminPage";
			} else {
				m.addAttribute("MemberAccount", myUser);
				m.addAttribute("MemberPwd", myPwd);
				m.addAttribute("checkUser", checkUser);

				session.setAttribute("Account", myUser);
				session.setAttribute("Status", users.getMemberStatus());
				session.setAttribute("id", id);
				return "HomePage";
			}
		}
		errors.put("msg", "please input correct useraccount or password");
		return "SignInPage";
	}

	@GetMapping("/MemberForgot")
	public String MemberForgot() {
		return "MemberForgot";
	}

	@PostMapping("/memberforgot.controller")
	public String memberforgotAction(@RequestParam("memberAccount") String memberAccount,
			@RequestParam("memberEmail") String memberEmail, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("errors", errors);

		Member member = memberService.selectMemberByAccount(memberAccount);
		if (member.getMemberEmail().equals(memberEmail)) {
			session.setAttribute("memberForgotAccount", memberAccount);
			return "MemberForgetAction";
		} else {
			errors.put("msg", "帳號與信箱不符合");
			return "MemberForgot";
		}
	}

	@PostMapping("/memberforgot.co")
	public String memberforgotCo(@RequestParam("memberPwd") String memberPwd,
			@RequestParam("memberNewPwd") String memberNewPwd, HttpSession session) {
		Map<String, String> error = new HashMap<String, String>();
		request.setAttribute("error", error);

		if (memberPwd.equals(memberNewPwd)) {
			memberService.forgotPwd(memberPwd, session.getAttribute("memberForgotAccount").toString());
			System.out.println(session.getAttribute("memberForgotAccount").toString());
			System.out.println(memberPwd);
			error.put("msg", "成功");
			return "SignInPage";
		} else {
			error.put("msg", "輸入密碼不符");
			return "MemberForgetAction";
		}
	}

	@PostMapping("/register.controller")
	public String memberCreate(HttpServletRequest request, @PathVariable @RequestParam("memberAccount") String myUser,
			@PathVariable @RequestParam("memberPwd") String myPwd, Model m, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("errors", errors);

		if (myUser == null || myUser.length() == 0) {
			errors.put("name", "name is required");
		}
		if (myPwd == null || myPwd.length() == 0) {
			errors.put("pwd", "password is required");
		}
		if (errors != null && !errors.isEmpty()) {
			return "registerPage";
		}
		Boolean check = memberService.insertMember(myUser, myPwd);

		if (check) {
			Member users = memberService.selectMember(myUser, myPwd);
			int id = users.getMemberId();

			m.addAttribute("MemberAccount", myUser);
			m.addAttribute("MemberPwd", myPwd);
			session.setAttribute("Account", myUser);
			session.setAttribute("Password", myPwd);
			session.setAttribute("Status", users.getMemberStatus());
			session.setAttribute("id", id);
			return "HomePage";
		}
		errors.put("msg", "帳號密碼重複");
		return "registerPage";
	}

	@GetMapping("/MemberPage")
	public String MemberPage(HttpSession session) {
		Member member = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
		session.setAttribute("Account", member.getMemberAccount());
		session.setAttribute("name", member.getMemberName());
		session.setAttribute("nickname", member.getNickname());
		session.setAttribute("gender", member.getGender());
		session.setAttribute("email", member.getMemberEmail());
		session.setAttribute("birthday", member.getBirthdayDate());
		session.setAttribute("starSign", member.getStarSign());
		session.setAttribute("city", member.getCity());
		session.setAttribute("booldtype", member.getBloodType());
		session.setAttribute("hobbies", member.getHobbies());
		session.setAttribute("selfinfo", member.getSelfIntro());

		return "MemberPage";
	}

	@RequestMapping(path = "/MemberPage_update", method = RequestMethod.GET)
	public String MemberPage_update(Model m) {
		Member member = new Member();
		m.addAttribute("Member", member);
		return "MemberPage_update";
	}

	@GetMapping("/MemberPage_show")
	public String MemberShow(HttpSession session) {
		Member member = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
		session.setAttribute("Account", member.getMemberAccount());
		session.setAttribute("name", member.getMemberName());
		session.setAttribute("nickname", member.getNickname());
		session.setAttribute("gender", member.getGender());
		session.setAttribute("email", member.getMemberEmail());
		session.setAttribute("birthday", member.getBirthdayDate());
		session.setAttribute("starSign", member.getStarSign());
		session.setAttribute("city", member.getCity());
		session.setAttribute("booldtype", member.getBloodType());
		session.setAttribute("hobbies", member.getHobbies());
		session.setAttribute("selfinfo", member.getSelfIntro());

		return "MemberPage";
	}
	
	@RequestMapping("/getPhoto/{id}")
	public void getPhoto(HttpServletResponse response, HttpSession session) {
		response.setContentType("image/jpeg");
		Member member = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
		
		if(member.getPicture_1()!=null) {
			InputStream inputStream = new ByteArrayInputStream(member.getPicture_1());
			try {
				IOUtils.copy(inputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(member.getPicture_2()!=null) {
			InputStream inputStream = new ByteArrayInputStream(member.getPicture_2());
			try {
				IOUtils.copy(inputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("/getPhoto2/{id}")
	public void getPhoto2(HttpServletResponse response, HttpSession session) {
		response.setContentType("image/jpeg");
		Member member = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
		
		if(member.getPicture_2()!=null) {
			InputStream inputStream = new ByteArrayInputStream(member.getPicture_2());
			try {
				IOUtils.copy(inputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping("/MemberPage_updatePic")
	public String updatePic() {
		return "MemberPage_updatePic";
	}

	@PostMapping("/MemberPic")
	public String MemberUpdatePic(HttpServletRequest request, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("errors", errors);

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file1 = multipartRequest.getFile("picture_1");
		MultipartFile file2 = multipartRequest.getFile("picture_2");

		if (file1.getSize() > 0) {
			try {
				byte[] p1 = file1.getBytes();
				Member user = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
				user.setPicture_1(p1);
			} catch (IOException e) {
				errors.put("msg", "Picture upload Fail");
				e.printStackTrace();
			}
		}

		if (file2.getSize() > 0) {
			try {
				byte[] p2 = file2.getBytes();
				Member user = memberService.selectMemberByAccount(session.getAttribute("Account").toString());
				user.setPicture_2(p2);
				session.setAttribute("p2", p2);
			} catch (IOException e) {
				errors.put("msg", "Picture upload Fail");
				e.printStackTrace();
			}
		}
		return "MemberPage";
	}

	@GetMapping("/picture")
	public void picture(HttpSession session) {
		memberService.selectMemberByAccount(session.getAttribute("Account").toString());
	}

	@PostMapping("/MemberPage_DB")
	public String updateMember(@ModelAttribute("Member") Member member, Model m, HttpSession session) {
		session.setAttribute("name", member.getMemberName());
		session.setAttribute("nickname", member.getNickname());
		session.setAttribute("gender", member.getGender());
		session.setAttribute("email", member.getMemberEmail());
		session.setAttribute("birthday", member.getBirthdayDate());
		session.setAttribute("starSign", member.getStarSign());
		session.setAttribute("city", member.getCity());
		session.setAttribute("booldtype", member.getBloodType());
		session.setAttribute("hobbies", member.getHobbies());
		session.setAttribute("selfinfo", member.getSelfIntro());
		memberService.updateMember(member, session);

		return "MemberPage";
	}

	@ResponseBody
	@RequestMapping(path = "/test", method = RequestMethod.GET)
	public List<Member> test() {
		return memberService.selectAllMember();
	}

	@Transactional
	@PostMapping(value = "/CheckMemberAccount", produces = { "application/json" })
	public @ResponseBody Map<String, String> checkMemberAccount(@RequestParam("account1") String memberAccount) {
		System.out.println(memberAccount);
		Map<String, String> map = new HashMap<>();
		String mAccount = memberService.checkMemberAccount(memberAccount);
		map.put("id", mAccount);
		return map;
	}
}
