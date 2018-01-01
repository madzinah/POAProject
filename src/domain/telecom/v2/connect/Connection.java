package domain.telecom.v2.connect;

import domain.telecom.v2.util.Contract;

/**
 * Une connexion est un lien entre deux clients.
 * @inv
 *     getState() != null
 */
class Connection {
    
    // ATTRIBUTS

    private State state;
    private final ICustomer caller;
    private final ICustomer callee;

    // CONSTRUCTEURS

    Connection(ICustomer c, ICustomer r) {
        state = State.PENDING;
        caller = c;
        callee = r;
    }

    // REQUETES

    /**
     * La zone d'appel de l'appelant.
     */
    ICustomer getCallee() {
        return callee;
    }
    
    /**
     * La zone d'appel de l'appelé.
     */
    ICustomer getCaller() {
        return caller;
    }
    
    /**
     * L'état de cette connexion.
     */
    State getState() {
        return state;
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
    }
    
    // TYPES IMBRIQUES

    enum State { PENDING, COMPLETE, DROPPED }
}
