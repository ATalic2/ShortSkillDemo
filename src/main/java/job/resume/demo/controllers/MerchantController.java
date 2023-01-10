package job.resume.demo.controllers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import job.resume.demo.dao.MerchantDAO;
import job.resume.demo.entity.Merchant;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	private MerchantDAO merchantDAO;

	@GetMapping("/viewMerchants")
	public String merchants(Model model) {
		model.addAttribute("merchants", merchantDAO.getMerchants());
		return "view-merchants";
	}

	@GetMapping
	public String getMerchants(@RequestParam(name = "id") int id, Model model) {
		LOGGER.info("" + id);
		model.addAttribute("merchant", merchantDAO.getMerchant(id));
		return "merchant";
	}
	
	@PostMapping
	public RedirectView updateMerchant(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes,
			@ModelAttribute("merchant") Merchant merchant) {
		LOGGER.info("" + id);
		merchantDAO.updateMerchant(id, merchant);
		RedirectView redirectView = new RedirectView("/merchant?id=" + id, true);
		redirectAttributes.addFlashAttribute("updatedMerchantId", id);
		redirectAttributes.addFlashAttribute("updateMerchantSuccess", true);
		return redirectView;
	}

	@GetMapping("/addMerchant")
	public String createMerchant(Model model) {
		model.addAttribute("merchant", new Merchant());
		return "add-merchant";
	}

	@PostMapping("/addMerchant")
	public RedirectView addMerchant(@ModelAttribute("merchant") Merchant merchant, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView("/merchant/addMerchant", true);
		merchantDAO.addMerchant(merchant);
		redirectAttributes.addFlashAttribute("savedMerchant", merchant);
		redirectAttributes.addFlashAttribute("addMerchantSuccess", true);
		return redirectView;
	}
}