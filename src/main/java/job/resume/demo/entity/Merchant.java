package job.resume.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a merchant in the system.
 * <p>
 * Maps to the "merchants" table in the database and contains information such as
 * the merchant's name and the list of associated clients.
 * Includes validation constraints for required fields and maximum lengths.
 * </p>
 */
@Entity
@Table(name = "merchants")
public class Merchant {

	/**
     * The unique identifier for the merchant.
     */
	@Id
	@Column(nullable = false, length = 6)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int merchantId;

	/**
     * The name of the merchant.
     * Cannot be blank and must be at most 100 characters.
     */
	@Column(length = 20)
	@NotBlank(message = "Merchant name is required")
    @Size(max = 100, message = "Merchant name must be at most 100 characters")
	private String name;

	/**
     * The list of clients associated with this merchant.
     */
	@JsonIgnore
	@OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY)
	private List<Client> clients;

	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Client> getClients() {
		return clients;
	}
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	@Override
	public String toString() {
		return "Merchant [id=" + merchantId + ", name=" + name + "]";
	}
}