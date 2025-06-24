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
	
	@Override
	public String toString() {
		return "UserEntity [username=" + username + ", psw=" + psw + ", id=" + id + "]";
	}



}
