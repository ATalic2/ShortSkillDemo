package job.resume.demo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import job.resume.demo.entity.Client;
import job.resume.demo.entity.Merchant;

@Component
public class CleanUpDAO {
	
    @PersistenceContext
    private EntityManager entityManager;

	@Transactional
	public void deleteClients(List<Integer> clientIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Client> criteriaDelete = criteriaBuilder.createCriteriaDelete(Client.class);
        Root<Client> root = criteriaDelete.from(Client.class);
        List<Predicate> predicates = new ArrayList<>();
        for(Integer clientId : clientIds) {
            predicates.add(criteriaBuilder.notEqual(root.get("clientId"), clientId));
        }
        if (predicates.isEmpty()) {
            criteriaDelete.where(criteriaBuilder.conjunction());
        } else {
            criteriaDelete.where(predicates.toArray(new Predicate[0]));
        }
        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

	@Transactional
	public void deleteMerchants(List<Integer> merchantIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Merchant> criteriaDelete = criteriaBuilder.createCriteriaDelete(Merchant.class);
        Root<Merchant> root = criteriaDelete.from(Merchant.class);
        List<Predicate> predicates = new ArrayList<>();
        for(Integer merchantId : merchantIds) {
            predicates.add(criteriaBuilder.notEqual(root.get("merchantId"), merchantId));
        }
        if (predicates.isEmpty()) {
            criteriaDelete.where(criteriaBuilder.conjunction());
        } else {
            criteriaDelete.where(predicates.toArray(new Predicate[0]));
        }
        entityManager.createQuery(criteriaDelete).executeUpdate();
    }
}