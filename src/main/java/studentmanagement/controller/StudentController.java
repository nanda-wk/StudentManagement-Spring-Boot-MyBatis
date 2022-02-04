package studentmanagement.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studentmanagement.dao.ClassRepository;
import studentmanagement.dao.StudentRepository;
import studentmanagement.dto.ClassResponseDTO;
import studentmanagement.dto.StudentRequestDTO;
import studentmanagement.dto.StudentResponseDTO;
import studentmanagement.model.SearchBean;
import studentmanagement.model.StudentBean;

@Controller
public class StudentController {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	ClassRepository classRepository;

	@ModelAttribute("clist")
	public List<String> classList() {
		List<ClassResponseDTO> clist = classRepository.selectClass();
		List<String> l = new ArrayList<>();
		for (int i = 0; i < clist.size(); i++) {
			l.add(clist.get(i).getName());
		}
		return l;
	}

	@GetMapping("/studentSearch")
	public ModelAndView userSearchSetUp(@ModelAttribute("success") String success, ModelMap model) {
		model.addAttribute("success", success);
		return new ModelAndView("BUD001", "bean", new SearchBean());
	}

	@GetMapping("/studentResult")
	public String userSearch(@ModelAttribute("bean") SearchBean bean, ModelMap model) {
		StudentRequestDTO dto = new StudentRequestDTO();
		List<StudentResponseDTO> list = new ArrayList<StudentResponseDTO>();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());

		if (!dto.getStudentId().equals("") || !dto.getStudentName().equals("") || !dto.getClassName().equals("")) {
			list = studentRepository.selectOne(dto);
		} else {
			list = studentRepository.selectStudent();
		}

		if (list.size() == 0) {
			model.addAttribute("error", "No Student found!");
		}
		model.addAttribute("list", list);
		return "BUD001";
	}

	@GetMapping("/studentUpdate")
	public ModelAndView studentUpdateSetUp(@RequestParam("id") String id, ModelMap model) {
		StudentRequestDTO dto = new StudentRequestDTO();
		dto.setStudentId(id);
		List<StudentResponseDTO> list = studentRepository.selectOne(dto);
		StudentBean bean = new StudentBean();
		for (StudentResponseDTO res : list) {
			bean.setStudentId(res.getStudentId());
			bean.setStudentName(res.getStudentName());
			bean.setClassName(res.getClassName());
			bean.setStatus(res.getStatus());
			String[] dt = res.getRegisterDate().toString().split("-");
			bean.setYear(dt[0]);
			bean.setMonth(dt[1]);
			bean.setDay(dt[2]);
		}
		return new ModelAndView("BUD002-01", "bean", bean);
	}

	@PostMapping("/studentUpdate")
	public String studentUpdate(@ModelAttribute("bean") @Validated StudentBean bean, BindingResult br, ModelMap model) {
		if (br.hasErrors() || bean.getYear().equals("Year") || bean.getMonth().equals("Month")
				|| bean.getDay().equals("Day")) {
			model.addAttribute("error", "Register date can't be blank!");
			return "BUD002-01";
		}

		String y = bean.getYear();
		String m = bean.getMonth();
		String d = bean.getDay();

		StudentRequestDTO dto = new StudentRequestDTO();
		dto.setStudentId(bean.getStudentId());
		dto.setStudentName(bean.getStudentName());
		dto.setClassName(bean.getClassName());
		dto.setRegisterDate(y + "-" + m + "-" + d);
		dto.setStatus(bean.getStatus());
		int i = studentRepository.updateStudent(dto);
		if (i == 0) {
			model.addAttribute("error", "Student update Fail");
			return "BUD002-01";
		}
		model.addAttribute("success", "Student successfully updated");
		return "BUD002-01";
	}

	@GetMapping("/studentRegister")
	public ModelAndView studentRegisterSetup() {
		return new ModelAndView("BUD002", "bean", new StudentBean());
	}

	@PostMapping("/studentRegister")
	public String studentRegister(@ModelAttribute("bean") @Validated StudentBean bean, BindingResult br, ModelMap model,
			RedirectAttributes ra) {
		if (br.hasErrors() || bean.getYear().equals("Year") || bean.getMonth().equals("Month")
				|| bean.getDay().equals("Day")) {
			model.addAttribute("error", "Register date can't be blank!");
			return "BUD002-01";
		}

		String y = bean.getYear();
		String m = bean.getMonth();
		String d = bean.getDay();

		StudentRequestDTO dto = new StudentRequestDTO();
		dto.setStudentId(bean.getStudentId());
		List<StudentResponseDTO> list = studentRepository.selectOne(dto);
		System.out.println(list);
		if (list.size() != 0) {
			model.addAttribute("error", "StudentID already exist!");
			return "BUD002";
		} else {
			dto.setStudentName(bean.getStudentName());
			dto.setClassName(bean.getClassName());
			dto.setRegisterDate(y + "-" + m + "-" + d);
			dto.setStatus(bean.getStatus());
			int i = studentRepository.insertStudent(dto);
			if (i > 0) {
				model.addAttribute("success", "Student successfully registered");
//				return "redirect:/studentRegister";
				return "BUD002";
			} else {
				model.addAttribute("error", "Student register fail!");
				return "BUD002";
			}
		}
	}

	@GetMapping("/studentDelete")
	public String studentDelete(@PathParam("id") String id, ModelMap model, RedirectAttributes ra) {
		StudentRequestDTO dto = new StudentRequestDTO();
		dto.setStudentId(id);
		int i = studentRepository.deleteStudent(dto);
		if (i > 0) {
			ra.addAttribute("success", "Delete successful");
		} else {
			ra.addAttribute("error", "Delete Fail!");
		}
		return "redirect:/studentSearch";
	}
}
