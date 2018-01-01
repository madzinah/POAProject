package telecom.v1.connect;

/**
 * Un client est caractérisé par son nom.
 * Il peut appeler un autre client, décrocher, raccrocher et inviter d'autres
 *  clients à un appel en cours.
 * @inv
 *     getName() != null && !"".equals(getName())
 *     x.getName().equals(getName()) <==> x == this
 *     getAreaCode() >= 0
 *     isFree() <==> getCall() == null
 *     !isFree()
 *         ==> getCall().getCaller() == this || getCall().isConnectedWith(this)
 *     this.isCalling(x) <==> x.isCalling(this)
 *     !this.isCalling(this)
 *     getTotalConnectedTime() >= 0
 *     getCharge() >= 0
 * @cons
 *     $ARGS$ String n, int ac
 *     $PRE$
 *         n != null && !"".equals(n)
 *         n n'est pas déjà pris
 *         ac >= 0
 *     $POST$
 *         getName().equals(n)
 *         getAreaCode() == ac
 *         isFree()
 *         getTotalConnectedTime() == 0
 *         getCharge() == 0
 */
public interface ICustomer {
    
    // REQUETES
    
    /**
     * La zone géographique du client.
     */
    int getAreaCode();

    /**
     * L'appel en cours.
     * Retourne null s'il n'y en a pas.
     */
    ICall getCall();
    
    /**
     * Le montant dont le client doit s'acquitter.
     */
    int getCharge();

    /**
     * Le nom de ce client.
     */
    String getName();
    
    /**
     * Le temps total de communication de ce client.
     */
    int getTotalConnectedTime();

    /**
     * Détermine si ce client est en communication avec x.
     * @pre
     *     x != null
     */
    boolean isCalling(ICustomer x);
    
    /**
     * Indique si ce client est libre ou occupé.
     */
    boolean isFree();

    // COMMANDES
    
    /**
     * Appelle le client x.
     * Si x est occupé, l'appel n'aboutit pas : aucune nouvelle connexion n'est
     *  mise en place.
     * Si ce client est déjà en communication, il s'agit d'une conférence.
     * @pre
     *     x != null && x != this
     *     isFree() || getCall().getCaller() == this
     * @post
     *     x.isFree()
     *         ==> old isFree()
     *                 ==> !this.isFree()
     *                     getCall().getCaller() == this
     *                     getCall().includes(x)
     *         ==> old (!isFree() && getCall().getCaller() == this)
     *                 ==> getCall().includes(r)
     */
    void call(ICustomer x);

    /**
     * Raccroche d'un appel.
     * @pre
     *     !isFree()
     * @post
     *     isFree()
     *     getTotalConnectedTime()
     *         == old getTotalConnectedTime()
     *            + getCall().getElapsedTimeFor(this)
     *     this == old getCall().getCaller()
     *         ==> forall r:(old getCall().isConnectedWith(r)) : r.isFree()
     *             getCharge()
     *                 == old getCharge() + (old getCall()).getTotalPrice();
     */
    void hangUp();

    /**
     * Répond à un appel.
     * @pre
     *     c != null && c.includes(this)
     *     isFree()
     * @post
     *     getCall() == c
     *     c.isConnectedWith(this)
     */
    void pickUp(ICall c);
}
