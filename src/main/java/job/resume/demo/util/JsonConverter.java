package job.resume.demo.util;

import javax.annotation.PostConstruct;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Component
public class JsonConverter {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(JsonConverter.class);

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Hibernate5Module());
	}

	public <T> T fromJson(String json, Class<T> clazz) {
		T result = null;
		try {
			result = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			LOGGER.warn("Exception occured: ", e);
		}
		return result;
	}

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