package job.resume.demo.controllers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling login page requests.
 * <p>
 * Provides an endpoint for displaying the login page and logs access attempts.
 * </p>
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(LoginController.class);

	/**
     * Displays the login page.
     *
     * @return the view name for the login page
     */
	@GetMapping
	public String login() {
		LOGGER.info("Login page accessed");
		return "login";
	}
}