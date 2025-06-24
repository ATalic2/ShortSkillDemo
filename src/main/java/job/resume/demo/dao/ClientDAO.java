package job.resume.demo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;
import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

/**
 * Data Access Object (DAO) for performing CRUD operations on {@link Client} entities.
 * <p>
 * This class provides methods to fetch, add, update, and delete clients from the database.
 * All methods are transactional and use Hibernate's {@link SessionFactory} for persistence.
 * </p>
 */
@Component
public class ClientDAO {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ClientDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

	/**
     * Retrieves all clients from the database.
     *
     * @return a list of all {@link Client} entities
     */
    @Transactional
    public List<Client> getClients() {
        LOGGER.info("Fetching all clients from the database");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> root = criteriaQuery.from(Client.class);
        criteriaQuery.select(root);

        List<Client> results = entityManager.createQuery(criteriaQuery).getResultList();
        LOGGER.info("Fetched {} clients", results.size());
        return results;
    }

	/**
     * Retrieves a client by its ID.
     *
     * @param id the client ID
     * @return the {@link Client} entity, or null if not found
     */
    @Transactional
    public Client getClient(int id) {
        LOGGER.info("Fetching client with id: {}", id);
        Client client = entityManager.find(Client.class, id);
        LOGGER.info("Fetched client: {}", client != null ? client.getClientId() : "null");
        return client;
    }

	 /**
     * Adds a new client to the database.
     *
     * @param client the {@link Client} entity to add
     */
    @Transactional
    public void addClient(Client client) {
        LOGGER.info("Adding new client: {} {}", client.getFirstName(), client.getLastName());
        client.setMerchant(entityManager.find(Merchant.class, client.getMerchantId()));
        entityManager.persist(client);
        LOGGER.info("Client added with id: {}", client.getClientId());
    }

	/**
     * Updates an existing client in the database.
     *
     * @param client the {@link Client} entity with updated information
     */
    @Transactional
    public void updateClient(Client client) {
        LOGGER.info("Updating client with id: {}", client.getClientId());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Client> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Client.class);
        Root<Client> root = criteriaUpdate.from(Client.class);
        criteriaUpdate.set(root.get("firstName"), client.getFirstName())
            .set(root.get("lastName"), client.getLastName())
            .set(root.get("merchant"), client.getMerchant())
            .set(root.get("job"), client.getJob())
            .where(criteriaBuilder.equal(root.get("clientId"), client.getClientId()));
        int updated = entityManager.createQuery(criteriaUpdate).executeUpdate();
        LOGGER.info("Updated {} client(s)", updated);
    }

	/**
     * Deletes a client from the database by its ID.
     *
     * @param id the client ID to delete
     */
    @Transactional
    public void deleteClient(int id) {
        LOGGER.info("Deleting client with id: {}", id);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Client> criteriaDelete = criteriaBuilder.createCriteriaDelete(Client.class);
        Root<Client> root = criteriaDelete.from(Client.class);
        if (id > 0) {
            criteriaDelete.where(criteriaBuilder.equal(root.get("clientId"), id));
        } else {
            criteriaDelete.where(criteriaBuilder.conjunction());
        }
        int deleted = entityManager.createQuery(criteriaDelete).executeUpdate();
        LOGGER.info("Deleted {} client(s)", deleted);
    }
}