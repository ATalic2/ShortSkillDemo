package job.resume.demo.controllers;

import ch.qos.logback.classic.Logger;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import jakarta.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import job.resume.demo.dao.MerchantDAO;
import job.resume.demo.entity.Merchant;

/**
 * Controller for handling all merchant-related web requests.
 * <p>
 * Provides endpoints for viewing, adding, and updating merchants.
 * Handles form validation, user feedback, and error redirection.
 * </p>
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	private MerchantDAO merchantDAO;

	/**
     * Displays the page listing all merchants.
     *
     * @param model Spring model to hold attributes
     * @return the view name for the merchants list
     */
	@GetMapping("/viewMerchants")
	public String merchants(Model model) {
		try {
			model.addAttribute("merchants", merchantDAO.getMerchants());
		} catch (Exception e) {
			LOGGER.warn("Exception occurred while fetching merchants [{}]", e);
			model.addAttribute("merchants", new ArrayList<Merchant>());
			model.addAttribute("errorMessage", "Could not fetch merchants.");
		}
		return "view-merchants";
	}

	/**
     * Displays the page for a single merchant by ID.
     *
     * @param id the merchant ID
     * @param model Spring model to hold attributes
     * @return the view name for the merchant details
     */
	@GetMapping
	public String getMerchants(@RequestParam(name = "id") int id, Model model) {
		try {
			LOGGER.info("" + id);
			model.addAttribute("merchant", merchantDAO.getMerchant(id));
		} catch (Exception e) {
			LOGGER.warn("Exception occurred while fetching merchant [{}]", e);
			model.addAttribute("errorMessage", "Could not fetch merchant.");
		}
		return "merchant";
	}

	/**
     * Handles the submission of the update merchant form.
     *
     * @param id the merchant ID
     * @param redirectAttributes attributes for redirect scenarios
     * @param merchant the updated merchant object
     * @return redirect to the merchant details page
     */
	@PostMapping
	public RedirectView updateMerchant(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("merchant") Merchant merchant) {
		RedirectView redirectView = new RedirectView("/merchant?id=" + id, true);
		try {
			LOGGER.info("" + id);
			merchantDAO.updateMerchant(id, merchant);
			redirectAttributes.addFlashAttribute("updatedMerchantId", id);
			redirectAttributes.addFlashAttribute("updateMerchantSuccess", true);
		} catch (Exception e) {
			LOGGER.warn("Exception occurred while updating merchant [{}]", e);
			redirectAttributes.addFlashAttribute("updateMerchantSuccess", false);
			redirectAttributes.addFlashAttribute("errorMessage", "Could not update merchant.");
		}
		return redirectView;
	}

	/**
     * Displays the form for adding a new merchant.
     *
     * @param model Spring model to hold attributes
     * @return the view name for the add merchant form
     */
	@GetMapping("/addMerchant")
	public String createMerchant(Model model) {
		try {
			model.addAttribute("merchant", new Merchant());
		} catch (Exception e) {
			LOGGER.warn("Exception occurred while preparing add merchant [{}]", e);
			model.addAttribute("errorMessage", "Could not prepare add merchant form.");
		}
		return "add-merchant";
	}

	/**
     * Handles the submission of the add merchant form.
     *
     * @param merchant the merchant to add
     * @param bindingResult validation result
     * @param redirectAttributes attributes for redirect scenarios
     * @return redirect to the add merchant page or error page
     */
	@PostMapping("/addMerchant")
	public RedirectView addMerchant(@Valid @ModelAttribute("merchant") Merchant merchant, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			bindingResult.getAllErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append("<br/>"));
			redirectAttributes.addFlashAttribute("errorMessage", errorMsg.toString());
			return new RedirectView("/error?message=" + URLEncoder.encode(errorMsg.toString(), StandardCharsets.UTF_8), true);
		}

		RedirectView redirectView = new RedirectView("/merchant/addMerchant", true);
		try {
			merchantDAO.addMerchant(merchant);
			redirectAttributes.addFlashAttribute("savedMerchant", merchant);
			redirectAttributes.addFlashAttribute("addMerchantSuccess", true);
		} catch (Exception e) {
			LOGGER.warn("Exception occurred while adding merchant [{}]", e);
			redirectAttributes.addFlashAttribute("addMerchantSuccess", false);
			redirectAttributes.addFlashAttribute("errorMessage", "Could not add merchant.");
		}
		return redirectView;
	}
}