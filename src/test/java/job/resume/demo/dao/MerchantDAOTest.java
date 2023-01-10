package job.resume.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import job.resume.demo.CleanDataBase;
import job.resume.demo.TestConfiguration;
import job.resume.demo.entity.Merchant;

@SpringBootTest(classes = TestConfiguration.class)
public class MerchantDAOTest {
	
	@Autowired
	private MerchantDAO merchantDAO;
	@Autowired
	private CleanDataBase cleanDataBase;
	
	@BeforeEach
	public void cleanUp() {
		cleanDataBase.cleanDataBase();
	}

	@Test
	public void testMerchantDAOAdd() {
		Merchant merchant = new Merchant();
		merchant.setName("Test10");
		merchantDAO.addMerchant(merchant);
		
		Merchant merchantFromDAO = merchantDAO.getMerchant(merchant.getMerchantId());
		assertNotNull(merchantFromDAO);
		assertEquals(merchant.getMerchantId(), merchantFromDAO.getMerchantId());
		assertEquals("Test10", merchantFromDAO.getName());
	}
	
	@Test
	public void testMerchantDAOGetMerchants() {
		List<Merchant> merchants = merchantDAO.getMerchants();
		assertNotNull(merchants);
		assertEquals(2, merchants.size());
		
		assertEquals(1, merchants.get(0).getMerchantId());
		assertEquals("Test", merchants.get(0).getName());
		
		assertEquals(2, merchants.get(1).getMerchantId());
		assertEquals("Test2", merchants.get(1).getName());
	}
	
	@Test
	public void testMerchantDAOGetMerchant() {
		Merchant merchant = merchantDAO.getMerchant(1);
		assertNotNull(merchant);
		assertEquals(1, merchant.getMerchantId());
		assertEquals("Test", merchant.getName());
	}
	
	@Test
	public void testMerchantDAOUpdate() {
		Merchant merchant = new Merchant();
		merchant.setName("Test11");
		merchantDAO.addMerchant(merchant);
		
		Merchant merchantFromDAO = merchantDAO.getMerchant(merchant.getMerchantId());
		assertNotNull(merchantFromDAO);
		merchantFromDAO.setName("Test20");
		merchantDAO.updateMerchant(merchantFromDAO.getMerchantId(), merchantFromDAO);
		
		Merchant newMerchantFromDAO= merchantDAO.getMerchant(merchant.getMerchantId());
		assertNotNull(newMerchantFromDAO);
		assertEquals(merchant.getMerchantId(), newMerchantFromDAO.getMerchantId());
		assertEquals("Test20", newMerchantFromDAO.getName());
	}
}