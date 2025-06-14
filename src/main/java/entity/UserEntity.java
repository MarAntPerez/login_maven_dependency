package entity;

public class UserEntity {

	private String username;
	private String psw;
	private String id;
	
	public UserEntity() {
		
	}
	
	public UserEntity(String username, String psw, String id){
		this.username = username;
		this.psw = psw;
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public UserEntity fromString(String data) {
		String[] parts = data.split(",");
		if(parts.length != 3) return null;
		return new UserEntity(parts[0], parts[1], parts[2]);
	}

	@Override
	public String toString() {
		return username + "," + psw + "," + id;
	}

}
