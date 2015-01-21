import java.util.Random;	


public class User {
	private String carID;
	private String userID;
	private String carMan;
	public User(){
		Random rn = new Random();
		this.setCarID("CI");
		this.setUserID("UI") ;
		for(int i=0;i<5;i++){
			this.setCarID(this.getCarID() +Math.abs( rn.nextInt()%9)) ;
			this.setUserID(this.getUserID() + Math.abs(rn.nextInt()%9));}
		
		this.carMan = "Ford";
	}

	public String getCarID() {
		return carID;
	}

	private void setCarID(String carID) {
		this.carID = carID;
	}

	public String getUserID() {
		return userID;
	}

	private void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	public String carMan() {
		return carMan;
	}
	
	
}
