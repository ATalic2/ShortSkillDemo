package job.resume.demo.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import job.resume.demo.dao.ClientDAO;
import job.resume.demo.dao.MerchantDAO;
import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;
import job.resume.demo.util.JsonConverter;

/**
 * Controller for handling all client-related web requests.
 * <p>
 * Provides endpoints for viewing, adding, updating, and deleting clients,
 * as well as fetching clients in JSON format for a given merchant.
 * Handles form validation and error redirection.
 * </p>
 */
@Controller
@RequestMapping("/client")
public class ClientController {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientController.class);
	
	private static final String ERROR = "Exception occured [{}]";

	@Autowired
	private ClientDAO clientDAO;
	@Autowired
	private MerchantDAO merchantDAO;
	@Autowired
	private JsonConverter jsonConverter;

	/**
     * Returns a JSON list of all clients for a given merchant.
     *
     * @param json JSON string containing the merchantId
     * @return JSON array of clients
     */
	@GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE) // return json of all clients of a merchants
	public @ResponseBody String test(@RequestBody String json) {
		String result = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			List<Client> clients = merchantDAO.getMerchant(jsonObject.getInt("merchantId")).getClients();
			result = jsonConverter.toJson(clients);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
		}
		return result;
	}

	/**
     * Displays the page listing all clients.
     *
     * @param model Spring model to hold attributes
     * @return the view name for the clients list
     */
	@GetMapping("/viewClients")
	public String getCliens(Model model) {
		try {
			model.addAttribute("clients", clientDAO.getClients());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			model.addAttribute("clients", new ArrayList<Client>());
			model.addAttribute("errorMessage", "Could not fetch clients.");
			return "redirect:/error?message=" + URLEncoder.encode("Could not fetch clients.", StandardCharsets.UTF_8);
		}
		return "view-clients";
	}

	/**
     * Displays the form for adding a new client.
     *
     * @param model Spring model to hold attributes
     * @return the view name for the add client form
     */
	@GetMapping("/addClient")
	public String createClient(Model model) {
		try {
			model.addAttribute("client", new Client());
			model.addAttribute("merchants", merchantDAO.getMerchants());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			model.addAttribute("errorMessage", "Could not prepare add client form.");
			return "redirect:/error?message=" + URLEncoder.encode("Could not prepare add client form.", StandardCharsets.UTF_8);
		}
		return "add-client";
	}

	/**
     * Handles the submission of the add client form.
     *
     * @param client the client to add
     * @param bindingResult validation result
     * @param redirectAttributes attributes for redirect scenarios
     * @return redirect to the add client page or error page
     */
	@PostMapping("/addClient")
	public RedirectView addClient(@Valid @ModelAttribute("client") Client client, BindingResult bindingResult, 
		RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			bindingResult.getAllErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append("<br/>"));
			redirectAttributes.addFlashAttribute("errorMessage", errorMsg.toString());
			return new RedirectView("/error?message=" + URLEncoder.encode(errorMsg.toString(), StandardCharsets.UTF_8), true);
		}
		
		RedirectView redirectView = new RedirectView("/client/addClient", true);
		try {
			clientDAO.addClient(client);
			redirectAttributes.addFlashAttribute("savedClientId", client.getClientId());
			redirectAttributes.addFlashAttribute("addClientSuccess", true);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			redirectAttributes.addFlashAttribute("addClientSuccess", false);
			redirectAttributes.addFlashAttribute("errorMessage", "Could not add client.");
			return new RedirectView("/error?message=" + URLEncoder.encode("Could not add client.", StandardCharsets.UTF_8), true);
		}
		return redirectView;
	}

	/**
     * Deletes a client by ID.
     *
     * @param id the client ID
     * @return HTTP response indicating success or failure
     */
	@DeleteMapping("/deleteClient")
	public ResponseEntity<Void> deleteClient(@RequestParam(name = "id") int id) {
		LOGGER.info("Deleting client with id: [{}]", id);
		try {
			clientDAO.deleteClient(id);
			return ResponseEntity.ok().build();
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
     * Displays the form for updating a client.
     *
     * @param id the client ID
     * @param model Spring model to hold attributes
     * @return the view name for the client update form
     */
	@GetMapping
	public String client(@RequestParam(name = "id") int id, Model model) {
		try {
			Client client = clientDAO.getClient(id);
			client.setMerchantId(client.getMerchant().getMerchantId());
			model.addAttribute("client", client);
			model.addAttribute("merchants", merchantDAO.getMerchants());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			model.addAttribute("errorMessage", "Could not fetch client.");
			return "redirect:/error?message=" + URLEncoder.encode("Could not fetch client.", StandardCharsets.UTF_8);
		}
		return "client";
	}

	/**
	 * Handles the submission of the update client form.
	 *
	 * @param id the client ID
	 * @param redirectAttributes attributes for redirect scenarios
	 * @param client the client to update
	 * @param bindingResult validation result
	 * @return redirect to the updated client page or error page
	 */
	@PostMapping
	public RedirectView updateClient(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("client") Client client, BindingResult bindingResult) {
		RedirectView redirectView = new RedirectView("/client?id=" + id, true);

		if (bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			bindingResult.getAllErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append("<br/>"));
			redirectAttributes.addFlashAttribute("errorMessage", errorMsg.toString());
			return new RedirectView("/error?message=" + URLEncoder.encode(errorMsg.toString(), StandardCharsets.UTF_8), true);
		}

		try {
			Merchant merchant = merchantDAO.getMerchant(client.getMerchantId());
			client.setMerchant(merchant);
			clientDAO.updateClient(client);
			redirectAttributes.addFlashAttribute("updatedClientId", id);
			redirectAttributes.addFlashAttribute("updateClientSuccess", true);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			redirectAttributes.addFlashAttribute("updateClientSuccess", false);
			redirectAttributes.addFlashAttribute("errorMessage", "Could not update client.");
			return new RedirectView("/error?message=" + URLEncoder.encode("Could not update client.", StandardCharsets.UTF_8), true);
		}
		return redirectView;
	}
}