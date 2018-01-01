package domain.telecom.v2.connect;

import java.util.Set;

/**
 * Modélise un appel entre un appelant et un ou plusieurs correspondants.
 * @inv
 *     getCaller() != null
 *     getReceivers() != null
 *     isConnectedWith(x) <==> getReceivers().contains(x)
 *     isConnectedWith(x) ==> includes(x)
 *     noCalleePending()
 *         <==> forall x:includes(x) : isConnectedWith(x)
 * @cons
 *     $ARGS$ ICustomer c, ICustomer r
 *     $PRE$
 *         c != null && r != null
 *         c != r
 *         c.isFree()
 *     $POST$
 *         getCaller() == c
 *         getReceivers().size() == 0
 *         includes(r)
 */
public interface ICall {
    
    // REQUETES

    /**
     * Le client à l'origine de l'appel.
     */
    ICustomer getCaller();
    
    /**
     * L'ensemble des correspondants en cours de communication avec l'appelant.
     */
    Set<ICustomer> getReceivers();

    /**
     * Indique si le client x a été contacté par l'appelant et n'a pas encore
     *  répondu ou est en communication avec lui.
     * @pre
     *     x != null
     */
    boolean includes(ICustomer x);

    /**
     * Indique si x est un correspondant en cours de communication avec
     *  l'appelant.
     * @pre
     *     x != null
     */
    boolean isConnectedWith(ICustomer x);

    /**
     * Indique si l'appelant a bien obtenu tous ses correspondants.
     */
    boolean noCalleePending();
    
    // COMMANDES
    
    /**
     * Appelle un autre corresponsant (mode conférence).
     * @pre
     *     x != null
     *     x != getCaller()
     *     !includes(x)
     * @post
     *     includes(x) && !isConnectedWith(x)
     */
    void invite(ICustomer x);

    /**
     * Termine la communication avec x.
     * Si x est l'appelant, termine toute connexion concernant cet appel.
     * @pre
     *     x != null
     *     x == getCaller() || isConnectedWith(x)
     * @post
     *     x == getCaller() ==> forall y:ICustomer : !includes(y)
     *     x != getCaller()
     *         ==> getReceivers().size() == old getReceivers().size() - 1
     *             !includes(x)
     */
    void hangUp(ICustomer x);

    /**
     * Démarre une communication entre getCaller() et r lorsqu'il décroche.
     * @pre
     *     r != null
     *     r != getCaller()
     *     includes(r) && !isConnectedWith(r)
     * @post
     *     isConnectedWith(r)
     */
    void pickUp(ICustomer r);
}
