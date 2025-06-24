package job.resume.demo.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import job.resume.demo.CleanDataBase;
import job.resume.demo.dao.ClientDAO;
import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIT {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientControllerIT.class);

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ClientDAO clientDAO;
	@Autowired
	private CleanDataBase cleanDataBase;

	// @BeforeAll
	// public static void setup(@Autowired DataSource dataSource) throws SQLException {
	// 	try (Connection conn = dataSource.getConnection()) {
	// 		ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/drop-table.sql"));
	// 	}
	// 	try (Connection conn = dataSource.getConnection()) {
	// 		ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/data.sql"));
	// 	}
	// }
	
	// @AfterAll
	// public static void destroy(@Autowired DataSource dataSource) throws SQLException {
	// 	try (Connection conn = dataSource.getConnection()) {
	// 		ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/drop-table.sql"));
	// 	}
	// }
	
	@BeforeEach
	public void cleanUp() {
		cleanDataBase.cleanDataBase();
	}

	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetCliens() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/client/viewClients")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		assertNotNull(mvcResult);
		assertEquals("view-clients", mvcResult.getModelAndView().getViewName());

		List<Client> clients = (List<Client>) mvcResult.getModelAndView().getModel().get("clients");
		assertNotNull(clients);
		assertEquals(2, clients.size());

		assertEquals(1, clients.get(0).getClientId());
		assertEquals("TestFirstName", clients.get(0).getFirstName());
		assertEquals("TestLastName", clients.get(0).getLastName());
		assertEquals("TesterNum1", clients.get(0).getJob());
		assertEquals(1, clients.get(0).getMerchant().getMerchantId());

		assertEquals(2, clients.get(1).getClientId());
		assertEquals("TestName2", clients.get(1).getFirstName());
		assertEquals("TestLsName2", clients.get(1).getLastName());
		assertEquals("TesterNum2", clients.get(1).getJob());
		assertEquals(1, clients.get(1).getMerchant().getMerchantId());
	}

	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testTest() throws Exception {
		mockMvc.perform(get("/client/test")
						.content("{\r\n" 
								+ "    \"merchantId\" : 1\r\n" 
								+ "}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				
				.andExpect(jsonPath("$[0].clientId", is(1)))
				.andExpect(jsonPath("$[0].firstName", is("TestFirstName")))
				.andExpect(jsonPath("$[0].lastName", is("TestLastName")))
				.andExpect(jsonPath("$[0].job", is("TesterNum1")))
				
				.andExpect(jsonPath("$[1].clientId", is(2)))
				.andExpect(jsonPath("$[1].firstName", is("TestName2")))
				.andExpect(jsonPath("$[1].lastName", is("TestLsName2")))
				.andExpect(jsonPath("$[1].job", is("TesterNum2")));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetAddClient() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/client/addClient")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(mvcResult);
		
		List<Merchant> merchants = (List<Merchant>) mvcResult.getModelAndView().getModel().get("merchants");
		assertNotNull(merchants);
		assertEquals(2, merchants.size());
		
		assertEquals(1, merchants.get(0).getMerchantId());
		assertEquals("Test", merchants.get(0).getName());
		
		assertEquals(2, merchants.get(1).getMerchantId());
		assertEquals("Test2", merchants.get(1).getName());
		
		Client client = (Client) mvcResult.getModelAndView().getModel().get("client");
		assertNotNull(client);
		assertEquals(0, client.getClientId());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testPostAddClient() throws Exception {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		
		mockMvc.perform(post("/client/addClient")
				.flashAttr("client", client))
				.andExpect(status().is(302));
		
		Client clientFromDAO = clientDAO.getClient(client.getClientId());
		assertNotNull(clientFromDAO);
		assertEquals(client.getClientId(), clientFromDAO.getClientId());
		assertEquals("TestFirstName", clientFromDAO.getFirstName());
		assertEquals("TestLastName", clientFromDAO.getLastName());
		assertEquals("Tester", clientFromDAO.getJob());
		assertEquals(2, clientFromDAO.getMerchant().getMerchantId());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testDeleteClient() throws Exception {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		clientDAO.addClient(client);
		
		assertNotEquals(0, client.getClientId());
		Client clientFromDAO = clientDAO.getClient(client.getClientId());
		assertNotNull(clientFromDAO);
		
		mockMvc.perform(delete("/client/deleteClient?id=" + client.getClientId())
				.flashAttr("client", client))
				.andExpect(status().isOk());
		
		try {
			Client clientFromDAOAgain = clientDAO.getClient(client.getClientId());
			assertNull(clientFromDAOAgain);
		} catch(Exception e) {
			LOGGER.info("Exception is good, the client shouldn't be in the database");
		}
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetGetClient() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/client?id=1")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(mvcResult);
		
		List<Merchant> merchants = (List<Merchant>) mvcResult.getModelAndView().getModel().get("merchants");
		assertNotNull(merchants);
		assertEquals(2, merchants.size());
		
		assertEquals(1, merchants.get(0).getMerchantId());
		assertEquals("Test", merchants.get(0).getName());
		
		assertEquals(2, merchants.get(1).getMerchantId());
		assertEquals("Test2", merchants.get(1).getName());
		
		Client client = (Client) mvcResult.getModelAndView().getModel().get("client");
		assertNotNull(client);
		assertEquals(1, client.getClientId());
		assertEquals("TestFirstName", client.getFirstName());
		assertEquals("TestLastName", client.getLastName());
		assertEquals("TesterNum1", client.getJob());
		assertEquals(1, client.getMerchant().getMerchantId());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testPostUpdateClient() throws Exception {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		clientDAO.addClient(client);
		
		client.setFirstName("Test33");
		client.setLastName("Last36");
		
		mockMvc.perform(post("/client?id=" + client.getClientId())
				.flashAttr("client", client))
				.andExpect(status().is(302));
		
		Client clientFromDAO = clientDAO.getClient(client.getClientId());
		assertNotNull(clientFromDAO);
		assertEquals("Test33", clientFromDAO.getFirstName());
		assertEquals("Last36", clientFromDAO.getLastName());
		assertEquals("Tester", clientFromDAO.getJob());
		assertEquals(2, clientFromDAO.getMerchant().getMerchantId());
	}
}