package job.resume.demo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

@Component
public class CleanUpDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void deleteClients(List<Integer> clientIds) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<Client> criteriaDelete = criteriaBuilder.createCriteriaDelete(Client.class);
		Root<Client> root = criteriaDelete.from(Client.class);
		List<Predicate> predicates = new ArrayList<>();
		for(Integer clientId : clientIds) {
			predicates.add(criteriaBuilder.notEqual(root.get("clientId"), clientId));
		}
		criteriaDelete.where(predicates.toArray(new Predicate[0]));
		sessionFactory.getCurrentSession().createQuery(criteriaDelete).executeUpdate();
	}

	@Transactional
	public void deleteMerchants(List<Integer> merchantIds) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<Merchant> criteriaDelete = criteriaBuilder.createCriteriaDelete(Merchant.class);
		Root<Merchant> root = criteriaDelete.from(Merchant.class);
		List<Predicate> predicates = new ArrayList<>();
		for(Integer merchantId : merchantIds) {
			predicates.add(criteriaBuilder.notEqual(root.get("merchantId"), merchantId));
		}
		criteriaDelete.where(predicates.toArray(new Predicate[0]));
		sessionFactory.getCurrentSession().createQuery(criteriaDelete).executeUpdate();
	}
}