package job.resume.demo.util;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import job.resume.demo.TestDemoConfiguration;
import job.resume.demo.entity.Client;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDemoConfiguration.class)
public class JsonConverterTest {

	@Autowired
	private JsonConverter jsonConverter;
	
	@Test
	public void testToJson() {
		Client client = new Client();
		client.setClientId(5);
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		
		String jsonResult = jsonConverter.toJson(client);
		assertNotNull(jsonResult);
		assertThat(jsonResult, hasJsonPath("$.clientId", is(5)));
		assertThat(jsonResult, hasJsonPath("$.firstName", is("TestFirstName")));
		assertThat(jsonResult, hasJsonPath("$.lastName", is("TestLastName")));
		assertThat(jsonResult, hasJsonPath("$.job", is("Tester")));
	}
	
	@Test
	public void testFromJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("clientId", 5);
		jsonObject.put("firstName", "TestFirstName");
		jsonObject.put("lastName", "TestLastName");
		jsonObject.put("job", "Tester");
		
		Client clientResult = jsonConverter.fromJson(jsonObject.toString(), Client.class);
		assertNotNull(clientResult);
		assertEquals(5, clientResult.getClientId());
		assertEquals("TestFirstName", clientResult.getFirstName());
		assertEquals("TestLastName", clientResult.getLastName());
		assertEquals("Tester", clientResult.getJob());
	}
	
	@Test
	public void testFromJsonFailure() {
		Client clientResult = jsonConverter.fromJson(null, Client.class);
		assertNull(clientResult);
	}
}