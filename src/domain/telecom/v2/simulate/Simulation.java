package domain.telecom.v2.simulate;

import domain.telecom.v2.connect.Customer;
import domain.telecom.v2.connect.ICall;
import domain.telecom.v2.connect.ICustomer;

public class Simulation {
    
    // ATTRIBUTS

    /**
     * Définit la durée réelle d'une seconde (en ms).
     * Une seconde dans la simulation dure 0.1 seconde dans la réalité.
     */
    public static final int SECOND_DURATION = 100;

    // COMMANDES

    /**
     * Programme de la simulation.
     */
    public void run() {
        runTest1();
        runTest2();
        runTest3();
    }
    
    // OUTILS

    private void runTest1() {
        ICustomer a = new Customer("A", 76000);
        ICustomer b = new Customer("B", 76000);
        ICustomer c = new Customer("C", 34000);

        a.call(b);
        ICall call = a.getCall();
        wait(1.0);
        b.pickUp(call);
        wait(2.0);
        a.call(c);
        wait(1.0);
        c.pickUp(call);
        wait(3.0);
        a.hangUp();
    }
    
    private void runTest2() {
        ICustomer a = new Customer("D", 76000);
        ICustomer b = new Customer("E", 76000);

        a.call(b);
        ICall call = a.getCall();
        wait(1.0);
        b.pickUp(call);
        wait(3.0);
        b.hangUp();
    }
    
    private void runTest3() {
        ICustomer a = new Customer("F", 76000);
        ICustomer b = new Customer("G", 76000);
        ICustomer c = new Customer("H", 34000);

        a.call(b);
        ICall call = a.getCall();
        wait(1.0);
        b.pickUp(call);
        wait(2.0);
        a.call(c);
        wait(2.0);
        b.hangUp();
    }

    /**
     * Attend delay secondes.
     * En réalité, une seconde dans la simulation dure 0.1 seconde.
     */
    private void wait(double delay) {
        Object dummy = new Object();
        synchronized (dummy) {
            try {
                dummy.wait((long) (delay * SECOND_DURATION));
            } catch (Exception e) {
                // rien
            }
        }
    }
}
