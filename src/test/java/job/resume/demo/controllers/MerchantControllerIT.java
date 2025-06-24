package job.resume.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import job.resume.demo.CleanDataBase;
import job.resume.demo.dao.MerchantDAO;
import job.resume.demo.entity.Merchant;

@SpringBootTest
@AutoConfigureMockMvc
public class MerchantControllerIT {

	@Autowired
	private MerchantDAO merchantDAO;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CleanDataBase cleanDataBase;

	// @BeforeAll
	// public static void setup(@Autowired DataSource dataSource) throws SQLException {
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
	public void testGetMerchants() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/merchant/viewMerchants")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		assertNotNull(mvcResult);
		assertEquals("view-merchants", mvcResult.getModelAndView().getViewName());

		List<Merchant> merchants = (List<Merchant>) mvcResult.getModelAndView().getModel().get("merchants");
		assertNotNull(merchants);
		assertEquals(2, merchants.size());
		
		assertEquals(1, merchants.get(0).getMerchantId());
		assertEquals("Test", merchants.get(0).getName());
		
		assertEquals(2, merchants.get(1).getMerchantId());
		assertEquals("Test2", merchants.get(1).getName());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetGetMerchant() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/merchant?id=1")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(mvcResult);
		
		Merchant merchant = (Merchant) mvcResult.getModelAndView().getModel().get("merchant");
		assertNotNull(merchant);
		
		assertEquals(1, merchant.getMerchantId());
		assertEquals("Test", merchant.getName());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testGetAddClient() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/merchant/addMerchant")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(mvcResult);
		
		Merchant merchant = (Merchant) mvcResult.getModelAndView().getModel().get("merchant");
		assertNotNull(merchant);
		assertEquals(0, merchant.getMerchantId());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testPostAddMerchant() throws Exception {
		Merchant merchant = new Merchant();
		merchant.setName("Test789");
		merchantDAO.addMerchant(merchant);
		
		mockMvc.perform(post("/merchant/addMerchant")
				.flashAttr("merchant", merchant))
				.andExpect(status().is(302));
		
		Merchant merchantFromDAO = merchantDAO.getMerchant(merchant.getMerchantId());
		assertNotNull(merchantFromDAO);
		assertEquals(merchant.getMerchantId(), merchantFromDAO.getMerchantId());
		assertEquals("Test789", merchantFromDAO.getName());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	public void testPostUpdateMerchant() throws Exception {
		Merchant merchant = new Merchant();
		merchant.setName("Test789");
		merchantDAO.addMerchant(merchant);
		
		merchant.setName("Test3311");
		
		mockMvc.perform(post("/merchant?id=" + merchant.getMerchantId())
				.flashAttr("merchant", merchant))
				.andExpect(status().is(302));
		
		Merchant merchantFromDAO = merchantDAO.getMerchant(merchant.getMerchantId());
		assertNotNull(merchantFromDAO);
		assertEquals(merchant.getMerchantId(), merchantFromDAO.getMerchantId());
		assertEquals("Test3311", merchantFromDAO.getName());
	}
}