package job.resume.demo.util;

import jakarta.annotation.PostConstruct;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

/**
 * Utility component for converting between Java objects and JSON strings.
 * <p>
 * Uses Jackson's {@link ObjectMapper} for serialization and deserialization,
 * with configuration to ignore unknown properties and exclude null values.
 * Supports Hibernate types via the {@link Hibernate6Module}.
 * </p>
 */
@Component
public class JsonConverter {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(JsonConverter.class);

	private ObjectMapper objectMapper;

	/**
     * Initializes the {@link ObjectMapper} with custom settings after construction.
     */
	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Hibernate6Module());
	}

	/**
     * Converts a JSON string to a Java object of the specified class.
     *
     * @param json the JSON string to convert
     * @param clazz the target class type
     * @param <T> the type of the returned object
     * @return the deserialized Java object, or null if conversion fails
     */
	public <T> T fromJson(String json, Class<T> clazz) {
		T result = null;
		try {
			result = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			LOGGER.warn("Exception occured: ", e);
		}
		return result;
	}

	/**
     * Converts a Java object to its JSON string representation.
     *
     * @param object the Java object to serialize
     * @return the JSON string, or null if conversion fails
     */
	public String toJson(Object object) {
		String result = null;
		try {
			result = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.warn("Exception occured: ", e);
		}
		return result;
	}
}