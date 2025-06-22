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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;
import job.resume.demo.entity.Merchant;

/**
 * Data Access Object (DAO) for performing CRUD operations on {@link Merchant} entities.
 * <p>
 * This class provides methods to fetch, add, update, and delete merchants from the database.
 * All methods are transactional and use Hibernate's {@link SessionFactory} for persistence.
 * </p>
 */
@Component
public class MerchantDAO {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MerchantDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
     * Retrieves all merchants from the database.
     *
     * @return a list of all {@link Merchant} entities
     */
	@Transactional
	public List<Merchant> getMerchants() {
		LOGGER.info("Fetching all merchants from the database");
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Merchant> criteriaQuery = criteriaBuilder.createQuery(Merchant.class);
		Root<Merchant> root = criteriaQuery.from(Merchant.class);
		criteriaQuery.select(root);
		Query<Merchant> query = sessionFactory.getCurrentSession().createQuery(criteriaQuery);
		List<Merchant> results = query.getResultList();
		LOGGER.info("Fetched {} merchants", results.size());
		return results;
	}

	/**
     * Retrieves a merchant by its ID.
     *
     * @param merchantId the ID of the merchant to fetch
     * @return the {@link Merchant} entity, or null if not found
     */
	@Transactional
	public Merchant getMerchant(int merchantId) {
		LOGGER.info("Fetching merchant with id: {}", merchantId);
		Merchant merchant = sessionFactory.getCurrentSession().load(Merchant.class, merchantId);
		LOGGER.info("Fetched merchant: {}", merchant != null ? merchant.getMerchantId() : "null");
		return merchant;
	}

	/**
     * Updates the name of a merchant with the given ID.
     *
     * @param id the ID of the merchant to update
     * @param merchant the merchant object containing the new name
     */
	@Transactional
	public void updateMerchant(int id, Merchant merchant) {
		LOGGER.info("Updating merchant with id: {}", id);
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<Merchant> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Merchant.class);
		Root<Merchant> root = criteriaUpdate.from(Merchant.class);
		criteriaUpdate.set(root.get("name"), merchant.getName())
				.where(criteriaBuilder.equal(root.get("merchantId"), id));
		int updated = sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
		LOGGER.info("Updated {} merchant(s)", updated);
	}

	/**
     * Adds a new merchant to the database.
     *
     * @param merchant the {@link Merchant} entity to add
     */
	@Transactional
	public void addMerchant(Merchant merchant) {
		LOGGER.info("Adding new merchant: {}", merchant.getName());
		sessionFactory.getCurrentSession().save(merchant);
		LOGGER.info("Merchant added with id: {}", merchant.getMerchantId());
	}

	/**
     * Deletes a merchant from the database by its ID.
     *
     * @param id the ID of the merchant to delete
     */
	@Transactional
	public void deleteMerchant(int id) {
		LOGGER.info("Deleting merchant with id: {}", id);
		CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<Merchant> criteriaDelete = criteriaBuilder.createCriteriaDelete(Merchant.class);
		Root<Merchant> root = criteriaDelete.from(Merchant.class);
		criteriaDelete.where(criteriaBuilder.equal(root.get("merchantId"), id));
		int deleted = sessionFactory.getCurrentSession().createQuery(criteriaDelete).executeUpdate();
		LOGGER.info("Deleted {} merchant(s)", deleted);
	}
}