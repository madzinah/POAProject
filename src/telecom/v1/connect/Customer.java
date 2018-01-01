package telecom.v1.connect;

import java.util.HashSet;
import java.util.Set;

import telecom.v1.util.Contract;

public class Customer implements ICustomer {
    
    // ATTRIBUTS
    
    private static final HashSet<String> UNIQUE_IDS = new HashSet<String>();

    private final String name;
    private final int area;
    private ICall call;
    private int totalTime;
    private int charge;

    // CONSTRUCTEURS
    
    public Customer(String n, int ac) {
        Contract.checkCondition(n != null && !"".equals(n));
        Contract.checkCondition(!UNIQUE_IDS.contains(n));
        Contract.checkCondition(ac >= 0);

        name = n;
        area = ac;
        UNIQUE_IDS.add(name);
    }

    // REQUETES
    
    public int getAreaCode() {
        return area;
    }
    
    public ICall getCall() {
        return call;
    }

    public String getName() {
        return name;
    }
    
    public int getTotalConnectedTime() {
        return totalTime;
    }
    
    public int getCharge() {
        return charge;
    }

    public boolean isCalling(ICustomer x) {
        Contract.checkCondition(x != null);

        if (x == this) {
            return false;
        }
        if (call == null) {
            return false;
        }
        return call.isConnectedWith(x);
    }

    public boolean isFree() {
        return call == null;
    }
    
    @Override
    public String toString() {
        String result = name + "[" + area + "]";
        return result;
    }

    // COMMANDES
    
    public void call(ICustomer x) {
        Contract.checkCondition(x != null && x != this);
        Contract.checkCondition(call == null || call.getCaller() == this);

        if (x.isFree()) {
            if (call == null) {
                call = new Call(this, x);
            } else {
                call.invite(x);
            }
        }
    }

    public void hangUp() {
        Contract.checkCondition(call != null);

        ICustomer caller = call.getCaller();
        Set<ICustomer> receivers = call.getReceivers();
        if (this == caller) {
            if (receivers.size() == 0) {
                call.hangUp(this);
                totalTime += call.getElapsedTimeFor(this);
                charge += call.getTotalPrice();
                call = null;
            } else {
                for (ICustomer r : receivers) {
                    r.hangUp();
                }
            }
        } else {
            call.hangUp(this);
            if (receivers.size() == 1 && call.noCalleePending()) {
                caller.hangUp();
            }
            call = null;
        }
    }

    public void pickUp(ICall c) {
        Contract.checkCondition(c != null && c.includes(this) && call == null);
        
        call = c;
        c.pickUp(this);
    }
}
