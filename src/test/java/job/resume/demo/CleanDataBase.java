package job.resume.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import job.resume.demo.dao.CleanUpDAO;
import job.resume.demo.dao.ClientDAO;
import job.resume.demo.dao.MerchantDAO;
import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

@Component
public class CleanDataBase {
	
	private List<Integer> clientIds = new ArrayList<Integer>();
	private List<Integer> merchantIds = new ArrayList<Integer>();

	@Autowired
	private ClientDAO clientDAO;
	@Autowired
	private MerchantDAO merchantDAO;
	@Autowired
	private CleanUpDAO cleanUpDAO;
	
	private void init() {
		List<Client> clients = clientDAO.getClients();
		for(Client client : clients) {
			clientIds.add(client.getClientId());
		}

		List<Merchant> merchants = merchantDAO.getMerchants();
		for(Merchant merchant : merchants) {
			merchantIds.add(merchant.getMerchantId());
		}
	}
	
	public void cleanDataBase() {
		if(0 == clientIds.size() && 0 == merchantIds.size()) {
			init();
		}
		cleanUpDAO.deleteClients(clientIds);
		cleanUpDAO.deleteMerchants(merchantIds);
	}
}