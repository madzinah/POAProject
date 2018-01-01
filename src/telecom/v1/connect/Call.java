package telecom.v1.connect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import telecom.v1.util.Contract;

public class Call implements ICall {
    
    // ATTRIBUTS
    
    private final Map<ICustomer, Connection> pending;
    private final Map<ICustomer, Connection> complete;
    private final Map<ICustomer, Connection> dropped;
    private final ICustomer caller;
    private int price;

    // CONSTRUCTEURS
    
    Call(ICustomer c, ICustomer r) {
        Contract.checkCondition(c != null && r != null);
        Contract.checkCondition(c != r);
        Contract.checkCondition(c.isFree());

        caller = c;
        pending = new HashMap<ICustomer, Connection>();
        pending.put(r, new Connection(c, r));
        complete = new HashMap<ICustomer, Connection>();
        dropped = new HashMap<ICustomer, Connection>();
    }

    // REQUETES
    
    public ICustomer getCaller() {
        return caller;
    }
    
    public int getTotalPrice() {
        return price;
    }
    
    public int getElapsedTimeFor(ICustomer x) {
        Contract.checkCondition(x != null);
        Contract.checkCondition(!isConnectedWith(x));

        if (x == caller) {
            int result = 0;
            for (Connection con : dropped.values()) {
                result += con.getDuration();
            }
            return result;
        } else {
            Connection con = dropped.get(x);
            if (con == null) {
                return 0;
            } else {
                return con.getDuration();
            }
        }
    }

    public Set<ICustomer> getReceivers() {
        return new HashSet<ICustomer>(complete.keySet());
    }

    public boolean includes(ICustomer x) {
        Contract.checkCondition(x != null);

        return pending.keySet().contains(x) || complete.keySet().contains(x);
    }

    public boolean isConnectedWith(ICustomer x) {
        Contract.checkCondition(x != null);

        return complete.containsKey(x);
    }
    
    public boolean noCalleePending() {
        return pending.isEmpty();
    }

    @Override
    public String toString() {
        String result = "<" + caller.getName();
        result += setToString(pending.keySet());
        result += setToString(complete.keySet());
        result += setToString(dropped.keySet());
        return result + ">";
    }

    // COMMANDES
    
    public void invite(ICustomer x) {
        Contract.checkCondition(x != null);
        Contract.checkCondition(x != caller);
        Contract.checkCondition(!includes(x));
        
        pending.put(x, new Connection(caller, x));
    }

    public void hangUp(ICustomer x) {
        Contract.checkCondition(x != null);
        Contract.checkCondition(x == caller || isConnectedWith(x));

        if (x == caller) {
            for (ICustomer r : pending.keySet()) {
                Connection con = pending.get(r);
                con.drop();
                dropped.put(r, con);
            }
            pending.clear();
            for (ICustomer r : complete.keySet()) {
                Connection con = complete.get(r);
                con.drop();
                dropped.put(r, con);
                price += con.getPrice();
            }
            complete.clear();
        } else {
            Connection con = complete.remove(x);
            con.drop();
            dropped.put(x, con);
            price += con.getPrice();
        }
    }

    public void pickUp(ICustomer r) {
        Contract.checkCondition(r != null);
        Contract.checkCondition(r != caller);
        Contract.checkCondition(includes(r));
        Contract.checkCondition(!isConnectedWith(r));
        
        Connection con = pending.remove(r);
        con.complete();
        complete.put(r, con);
    }
    
    // OUTILS
    
    private String setToString(Set<ICustomer> s) {
        String result = "|";
        boolean first = true;
        for (ICustomer x : s) {
            if (first) {
                first = false;
            } else {
                result += " ";
            }
            result += x.getName();
        }
        return result;
    }
}
