package job.resume.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Global exception handler for all controllers.
 * <p>
 * Catches any unhandled exceptions thrown by controller methods and redirects
 * the user to a generic error page with a user-friendly error message.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles all unhandled exceptions thrown by controllers.
     *
     * @param ex the exception that was thrown
     * @param redirectAttributes attributes for passing the error message to the error page
     * @return a RedirectView to the generic error page with the error message
     */
    @ExceptionHandler(Exception.class)
    public RedirectView handleException(Exception ex, RedirectAttributes redirectAttributes) {
        LOGGER.error("Unhandled exception caught: ", ex);
        String message = "An unexpected error occurred: " + ex.getMessage();
        redirectAttributes.addFlashAttribute("errorMessage", message);
        return new RedirectView("/error?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8), true);
    }
}