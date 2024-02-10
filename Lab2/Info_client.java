import java.rmi.*;

public  class Info_client implements Info_itf {

	private String name;
 
	public Info_client(String s) {
		name = s ;
	}

	public String getName() throws RemoteException {
		return name ;
	}
}