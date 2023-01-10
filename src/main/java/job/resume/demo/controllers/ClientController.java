package job.resume.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@GetMapping("/viewClients")
	public String getCliens(Model model) {
		try {
			model.addAttribute("clients", clientDAO.getClients());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			model.addAttribute("clients", new ArrayList<Client>());
		}
		return "view-clients";
	}

	@GetMapping("/addClient")
	public String createClient(Model model) {
		try {
			model.addAttribute("client", new Client());
			model.addAttribute("merchants", merchantDAO.getMerchants());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
		}
		return "add-client";
	}

	@PostMapping("/addClient")
	public RedirectView addClient(@ModelAttribute("client") Client client, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView("/client/addClient", true);
		try {
			clientDAO.addClient(client);
			redirectAttributes.addFlashAttribute("savedClientId", client.getClientId());
			redirectAttributes.addFlashAttribute("addClientSuccess", true);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			redirectAttributes.addFlashAttribute("addClientSuccess", false);
		}
		return redirectView;
	}

	@DeleteMapping("/deleteClient")
	public void deleteClient(@RequestParam(name = "id") int id) {
		LOGGER.info("Deleting client with id: [{}]", id);
		try {
			clientDAO.deleteClient(id);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
		}
	}

	@GetMapping
	public String client(@RequestParam(name = "id") int id, Model model) {
		try {
			Client client = clientDAO.getClient(id);
			client.setMerchantId(client.getMerchant().getMerchantId());
			model.addAttribute("client", client);
			model.addAttribute("merchants", merchantDAO.getMerchants());
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
		}
		return "client";
	}

	@PostMapping
	public RedirectView updateClient(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes,
			@ModelAttribute("client") Client client) {
		RedirectView redirectView = new RedirectView("/client?id=" + id, true);
		try {
			Merchant merchant = merchantDAO.getMerchant(client.getMerchantId());
			client.setMerchant(merchant);
			clientDAO.updateClient(client);
			redirectAttributes.addFlashAttribute("updatedClientId", id);
			redirectAttributes.addFlashAttribute("updateClientSuccess", true);
		} catch(Exception e) {
			LOGGER.warn(ERROR, e);
			redirectAttributes.addFlashAttribute("updateClientSuccess", false);
		}
		return redirectView;
	}
}