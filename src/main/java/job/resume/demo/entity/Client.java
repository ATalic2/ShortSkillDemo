package job.resume.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing a client in the system.
 * <p>
 * Maps to the "clients" table in the database and contains information such as
 * first name, last name, job, and the associated merchant.
 * Includes validation constraints for required fields and maximum lengths.
 * </p>
 */
@Entity
@Table(name = "clients")
public class Client {

	/**
     * The unique identifier for the client.
     */
	@Id
	@Column(nullable = false, length = 6)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int clientId;

	/**
     * The first name of the client.
     * Cannot be blank and must be at most 50 characters.
     */
	@Column(nullable = false, length = 15)
	@NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
	private String firstName;

	/**
     * The last name of the client.
     * Cannot be blank and must be at most 50 characters.
     */
	@Column(nullable = false, length = 15)
	@NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
	private String lastName;

	/**
     * The job or occupation of the client.
     * Cannot be blank and must be at most 100 characters.
     */
	@NotBlank(message = "Job is required")
    @Size(max = 100, message = "Job must be at most 100 characters")
	@Column(length = 20)
	private String job;

	/**
     * The merchant ID associated with this client (used for form binding).
     * Not persisted in the database.
     */
	@Transient
	private int merchantId;

	/**
     * The merchant entity associated with this client.
     */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchantId")
	private Merchant merchant;

	public int getId() {
		return clientId;
	}
	public void setId(int clientId) {
		this.clientId = clientId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	@Override
	public String toString() {
		return "Client [id=" + clientId + ", firstName=" + firstName + ", lastName=" + lastName + ", job=" + job + "]";
	}
}