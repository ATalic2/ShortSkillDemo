package job.resume.demo.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.classic.Logger;

/**
 * Controller for handling error and access-denied pages.
 * <p>
 * Provides endpoints for displaying a generic error page with a custom message,
 * and an access-denied page for unauthorized access attempts.
 * </p>
 */
@Controller
public class ErrorController {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ErrorController.class);

    /**
     * Displays the access denied page.
     *
     * @return the view name for the access denied page
     */
    @GetMapping("/access-denied")
    public String DeniedPage() {
        LOGGER.info("Access denied page accessed");
        return "access-denied";
    }

    /**
     * Displays the generic error page with a custom error message.
     *
     * @param message the error message to display (optional)
     * @param model the Spring model to hold attributes
     * @return the view name for the error page
     */
    @GetMapping("/error")
    public String errorPage(@RequestParam(value = "message", required = false) String message, Model model) {
        LOGGER.info("Error page accessed with message: {}", message);
        model.addAttribute("errorMessage", message != null ? message : "An unexpected error occurred.");
        return "error";
    }
}