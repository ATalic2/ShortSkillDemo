package job.resume.demo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "merchants")
public class Merchant {

	@Id
	@Column(nullable = false, length = 6)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int merchantId;
	@Column(length = 20)
	private String name;
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
