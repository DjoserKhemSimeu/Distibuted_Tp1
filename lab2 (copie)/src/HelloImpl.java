import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
 
	public HelloImpl(String s) {
		message = s ;
	}

	public String sayHello(Info_itf name) throws RemoteException {
		String clientName = name.getName();
		return message+" "+clientName ;
	}
}

