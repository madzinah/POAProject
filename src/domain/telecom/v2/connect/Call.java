package domain.telecom.v2.connect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import domain.telecom.v2.util.Contract;

public class Call implements ICall {
    
    // ATTRIBUTS

    /**
     * Les clients contactés mais qui n'ont pas encore répondu ni raccroché, en
     *  relation avec leur connexion qui est dans l'état PENDING.
     */
    private final Map<ICustomer, Connection> pending;

    /**
     * Les correspondants en cours de communication, en relation avec leur
     *  connexion qui est dans l'état COMPLETE.
     */
    private final Map<ICustomer, Connection> complete;

    private final ICustomer caller;

    // CONSTRUCTEURS
    
    Call(ICustomer c, ICustomer r) {
        Contract.checkCondition(c != null && r != null);
        Contract.checkCondition(c != r);
        Contract.checkCondition(c.isFree());

        caller = c;
        pending = new HashMap<ICustomer, Connection>();
        pending.put(r, new Connection(c, r));
        complete = new HashMap<ICustomer, Connection>();
    }

    // REQUETES
    
    public ICustomer getCaller() {
        return caller;
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
            }
            pending.clear();
            for (ICustomer r : complete.keySet()) {
                Connection con = complete.get(r);
                con.drop();
            }
            complete.clear();
        } else {
            Connection con = complete.remove(x);
            con.drop();
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
}
