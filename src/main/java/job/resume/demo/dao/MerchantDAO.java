package job.resume.demo.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import job.resume.demo.entity.Merchant;

@Component
public class MerchantDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Merchant> getMerchants() {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Merchant> criteriaQuery = criteriaBuilder.createQuery(Merchant.class);
		Root<Merchant> root = criteriaQuery.from(Merchant.class);
		criteriaQuery.select(root);
		Query<Merchant> query = sessionFactory.getCurrentSession().createQuery(criteriaQuery);
		List<Merchant> results = query.getResultList();
		return results;
	}

	@Transactional
	public Merchant getMerchant(int merchantId) {
		return sessionFactory.getCurrentSession().load(Merchant.class, merchantId);
	}

	@Transactional
	public void updateMerchant(int id, Merchant merchant) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<Merchant> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Merchant.class);
		Root<Merchant> root = criteriaUpdate.from(Merchant.class);
		criteriaUpdate.set(root.get("name"), merchant.getName())
				.where(criteriaBuilder.equal(root.get("merchantId"), id));
		sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
	}

	@Transactional
	public void addMerchant(Merchant merchant) {
		sessionFactory.getCurrentSession().save(merchant);
	}

	@Transactional
	public void deleteMerchant(int id) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<Merchant> criteriaDelete = criteriaBuilder.createCriteriaDelete(Merchant.class);
		Root<Merchant> root = criteriaDelete.from(Merchant.class);
		criteriaDelete.where(criteriaBuilder.equal(root.get("merchantId"), id));
		sessionFactory.getCurrentSession().createQuery(criteriaDelete).executeUpdate();
	}
}