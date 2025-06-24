package job.resume.demo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;
import job.resume.demo.entity.Merchant;

/**
 * Data Access Object (DAO) for performing CRUD operations on {@link Merchant} entities.
 * <p>
 * This class provides methods to fetch, add, update, and delete merchants from the database.
 * All methods are transactional and use JPA's {@link EntityManager} for persistence.
 * </p>
 */
@Component
public class MerchantDAO {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MerchantDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

	/**
     * Retrieves all merchants from the database.
     *
     * @return a list of all {@link Merchant} entities
     */
	@Transactional
	public List<Merchant> getMerchants() {
        LOGGER.info("Fetching all merchants from the database");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merchant> criteriaQuery = criteriaBuilder.createQuery(Merchant.class);
        Root<Merchant> root = criteriaQuery.from(Merchant.class);
        criteriaQuery.select(root);
        List<Merchant> results = entityManager.createQuery(criteriaQuery).getResultList();
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
        Merchant merchant = entityManager.find(Merchant.class, merchantId);
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Merchant> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Merchant.class);
        Root<Merchant> root = criteriaUpdate.from(Merchant.class);
        criteriaUpdate.set(root.get("name"), merchant.getName())
                .where(criteriaBuilder.equal(root.get("merchantId"), id));
        int updated = entityManager.createQuery(criteriaUpdate).executeUpdate();
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
        entityManager.persist(merchant);
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Merchant> criteriaDelete = criteriaBuilder.createCriteriaDelete(Merchant.class);
        Root<Merchant> root = criteriaDelete.from(Merchant.class);
        // Always set a where clause to avoid Hibernate 6.x NPE
        criteriaDelete.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("merchantId"), id)));
        int deleted = entityManager.createQuery(criteriaDelete).executeUpdate();
        LOGGER.info("Deleted {} merchant(s)", deleted);
	}
}