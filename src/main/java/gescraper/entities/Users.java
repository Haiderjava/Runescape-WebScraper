package gescraper.entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;

@Entity
public class Users {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password_hash")
	private byte[] hash;
	
	@Column(name = "password_salt")
	private byte[] salt;

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Users(int userId, String username, byte[] hash, byte[] salt) {
		super();
		this.userId = userId;
		this.username = username;
		this.hash = hash;
		this.salt = salt;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username + ", hash=" + Arrays.toString(hash) + ", salt="
				+ Arrays.toString(salt) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(hash);
		result = prime * result + Arrays.hashCode(salt);
		result = prime * result + userId;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (!Arrays.equals(hash, other.hash))
			return false;
		if (!Arrays.equals(salt, other.salt))
			return false;
		if (userId != other.userId)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
}
