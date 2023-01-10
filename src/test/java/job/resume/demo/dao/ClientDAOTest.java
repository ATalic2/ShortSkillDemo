package job.resume.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import job.resume.demo.CleanDataBase;
import job.resume.demo.TestConfiguration;
import job.resume.demo.entity.Client;

@SpringBootTest(classes = TestConfiguration.class)
public class ClientDAOTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private ClientDAO clientDAO;
	@Autowired
	private CleanDataBase cleanDataBase;
	
	@BeforeEach
	public void cleanUp() {
		cleanDataBase.cleanDataBase();
	}
	
	@Test
	public void testClientDAOAdd() {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		clientDAO.addClient(client);
		
		Client clientFromDAO = clientDAO.getClient(client.getClientId());
		assertNotNull(clientFromDAO);
		assertEquals(client.getClientId(), clientFromDAO.getClientId());
		assertEquals("TestFirstName", clientFromDAO.getFirstName());
		assertEquals("TestLastName", clientFromDAO.getLastName());
		assertEquals("Tester", clientFromDAO.getJob());
		assertEquals(2, clientFromDAO.getMerchant().getMerchantId());
	}
	
	@Test
	public void testClientDAOGetClients() {
		List<Client> clients = clientDAO.getClients();
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
	public void testClientDAOGetClient() {
		Client client = clientDAO.getClient(1);
		assertNotNull(client);
		assertEquals(1, client.getClientId());
		assertEquals("TestFirstName", client.getFirstName());
		assertEquals("TestLastName", client.getLastName());
		assertEquals("TesterNum1", client.getJob());
		assertEquals(1, client.getMerchant().getMerchantId());
	}
	
	@Test
	public void testClientDAOUpdate() {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		clientDAO.addClient(client);
		
		Client clientFromDAO = clientDAO.getClient(client.getClientId());
		assertNotNull(clientFromDAO);
		clientFromDAO.setFirstName("UpdatedFirst");
		clientFromDAO.setLastName("UpdatedLast");
		clientDAO.updateClient(clientFromDAO);
		
		Client newClientFromDAO= clientDAO.getClient(client.getClientId());
		assertNotNull(newClientFromDAO);
		assertEquals(clientFromDAO.getClientId(), newClientFromDAO.getClientId());
		assertEquals("UpdatedFirst", newClientFromDAO.getFirstName());
		assertEquals("UpdatedLast", newClientFromDAO.getLastName());
		assertEquals("Tester", newClientFromDAO.getJob());
		assertEquals(2, newClientFromDAO.getMerchant().getMerchantId());
	}

	@Test
	public void testClientDAODelete() {
		Client client = new Client();
		client.setFirstName("TestFirstName");
		client.setLastName("TestLastName");
		client.setJob("Tester");
		client.setMerchantId(2);
		clientDAO.addClient(client);
		
		List<Client> clients = clientDAO.getClients();
		assertNotNull(clients);
		assertEquals(3, clients.size());
		
		clientDAO.deleteClient(client.getClientId());
		
		clients = clientDAO.getClients();
		assertNotNull(clients);
		assertEquals(2, clients.size());
	}
}