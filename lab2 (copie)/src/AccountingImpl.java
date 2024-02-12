import java.rmi.RemoteException;

public class AccountingImpl implements Accounting_itf {
    private int callCount;

    protected AccountingImpl() throws RemoteException{
        super();
        this.callCount = 0;
    }

    @Override
    public void numberOfCalls(int number) throws RemoteException {
        this.callCount = number;
        System.out.println("Server notified me I made " + number + " calls.");
    }

    public int getCallCount(){
        return callCount;
    }
    public void setCallCount(int i){
        this.callCount=i;
    }
    
}
