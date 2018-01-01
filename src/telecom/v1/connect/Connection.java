package telecom.v1.connect;

import telecom.v1.util.Contract;
import telecom.v1.util.Timer;

/**
 * Une connexion est un lien entre deux clients.
 * @inv
 *     getState() != null
 */
class Connection {
    
    // ATTRIBUTS
    
    private State state;
    private final Timer timer;
    private final Type type;
    private final ICustomer caller;
    private final ICustomer callee;

    // CONSTRUCTEURS
    
    Connection(ICustomer c, ICustomer r) {
        Contract.checkCondition(c != null && r != null);
        Contract.checkCondition(c != r);
        
        state = State.PENDING;
        timer = new Timer();
        caller = c;
        callee = r;
        if (c.getAreaCode() == r.getAreaCode()) {
            type = Type.LOCAL;
        } else {
            type = Type.NATIONAL;
        }
        System.err.println("Nouvelle connexion " + type + " de "
                + c.getName() + " vers " + r.getName());
    }

    // REQUETES
    
    ICustomer getCallee() {
        return callee;
    }
    
    ICustomer getCaller() {
        return caller;
    }
    
    /**
     * L'état de cette connexion.
     */
    State getState() {
        return state;
    }
    
    /**
     * Durée de la connexion.
     * @pre
     *     getState() == State.DROPPED
     */
    int getDuration() {
        Contract.checkCondition(state == State.DROPPED);
        
        return timer.getTime();
    }
    
    /**
     * Le tarif à l'unité de la connexion.
     */
    int getRate() {
        return type.rate;
    }
    
    /**
     * Le prix de la connexion.
     * @pre
     *     getState() == State.DROPPED
     */
    int getPrice() {
        Contract.checkCondition(state == State.DROPPED);

        return getDuration() * getRate();
    }
    
    // COMMANDES
    
    /**
     * Met à jour la connexion lorsque le client receveur décroche.
     * @pre
     *     getState() == State.PENDING
     * @post
     *     getState() == State.COMPLETE
     */
    void complete() {
        Contract.checkCondition(getState() == State.PENDING);

        state = State.COMPLETE;
        timer.start();
        System.err.println("connexion établie");
    }
    
    /**
     * Met à jour la connexion lorsque l'un des clients raccroche.
     * @pre
     *     getState() != State.DROPPED
     * @post
     *     getState() == State.DROPPED
     */
    void drop() {
        Contract.checkCondition(getState() != State.DROPPED);

        state = State.DROPPED;
        timer.stop();
        System.err.println("connexion terminée");
    }
    
    // OUTILS
    
    enum State { PENDING, COMPLETE, DROPPED }

    enum Type {
        LOCAL(3),
        NATIONAL(10);
        private int rate;
        Type(int r) {
            rate = r;
        }
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
