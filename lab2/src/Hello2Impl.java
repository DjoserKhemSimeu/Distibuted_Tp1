import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

public class Hello2Impl implements Hello2 {

    int callCount;

    protected Hello2Impl(Registry_itf registry) throws RemoteException {
        super();
        this.callCount = 0;
    }

    @Override
    public String sayHello(Accounting_itf client) throws RemoteException {
        callCount ++;
        client.numberOfCalls(callCount);

        return "Hello from Hello2! Number of calls: " + callCount;
    }
}
