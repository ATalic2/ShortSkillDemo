package job.resume.demo.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

@Component
public class ClientDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Client> getClients() {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
		Root<Client> root = criteriaQuery.from(Client.class);
		criteriaQuery.select(root);

		Query<Client> query = sessionFactory.getCurrentSession().createQuery(criteriaQuery);
		List<Client> results = query.getResultList();
		return results;
	}

	@Transactional
	public Client getClient(int id) {
		return sessionFactory.getCurrentSession().load(Client.class, id);
	}

	@Transactional
	public void addClient(Client client) {
		client.setMerchant((Merchant) sessionFactory.getCurrentSession().load(Merchant.class, client.getMerchantId()));
		sessionFactory.getCurrentSession().save(client);
	}

	@Transactional
	public void updateClient(Client client) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<Client> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Client.class);
		Root<Client> root = criteriaUpdate.from(Client.class);
		criteriaUpdate.set(root.get("firstName"), client.getFirstName())
			.set(root.get("lastName"), client.getLastName())
			.set(root.get("merchant"), client.getMerchant())
			.set(root.get("job"), client.getJob())
			.where(criteriaBuilder.equal(root.get("clientId"), client.getClientId()));
		sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
	}

	@Transactional
	public void deleteClient(int id) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<Client> criteriaDelete = criteriaBuilder.createCriteriaDelete(Client.class);
		Root<Client> root = criteriaDelete.from(Client.class);
		criteriaDelete.where(criteriaBuilder.equal(root.get("clientId"), id));
		sessionFactory.getCurrentSession().createQuery(criteriaDelete).executeUpdate();
	}
}