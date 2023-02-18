package espita.client1.proj1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
	public class CV {
	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private long id;
	   private String text;
	   private String email;

	   public CV() {}

	   public CV(String text,String email) {
	      this.text = text;
	      this.email=email;
	   }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}
