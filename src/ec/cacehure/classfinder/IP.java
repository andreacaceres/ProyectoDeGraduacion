package ec.cacehure.classfinder;

public class IP {
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public IP(){
		this.ip="http://192.168.0.5/";
	}
	
	public IP(String ip){
		this.ip=ip;
	}

}
