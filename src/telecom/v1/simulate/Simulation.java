package telecom.v1.simulate;

import telecom.v1.connect.Customer;
import telecom.v1.connect.ICall;
import telecom.v1.connect.ICustomer;

public class Simulation {
    
    // ATTRIBUTS
    
    /**
     * Définit la durée réelle d'une seconde (en ms).
     * Une seconde dans la simulation dure 0.1 seconde dans la réalité.
     */
    public static final int SECOND_DURATION = 100;
    private static final String SEP = "------------------";
    
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
    
    private void report(ICustomer c) {
        String msg = c + " a été connecté " + c.getTotalConnectedTime() + " s";
        if (c.getCall() != null) {
            msg += " (encore en communication : " + c.getCall() + ")";
        }
        msg += "\nFacture : " + c.getCharge();
        say(msg);
    }
    
    /**
     * Une communication établie par A avec B et C.
     * A appelle B.
     * B décroche.
     * A appelle C.
     * C décroche.
     * A raccroche.
     */
    private void runTest1() {
        ICustomer a = new Customer("A", 76000);
        ICustomer b = new Customer("B", 76000);
        ICustomer c = new Customer("C", 34000);

        say("A appelle B...");
        a.call(b);
        ICall call = a.getCall();
        wait(1.0);
        say("B décroche...");
        b.pickUp(call);
        wait(2.0);
        say("A appelle C (mode conférence)...");
        a.call(c);
        wait(1.0);
        say("C décroche...");
        c.pickUp(call);
        wait(3.0);
        say("A raccroche...");
        a.hangUp();
        report(a);
        report(b);
        report(c);
        say(SEP);
    }
    
    /**
     * Une communication établie par D avec E.
     * D appelle E.
     * E décroche.
     * E raccroche.
     */
    private void runTest2() {
        ICustomer d = new Customer("D", 76000);
        ICustomer e = new Customer("E", 76000);

        say("D appelle E...");
        d.call(e);
        ICall call = d.getCall();
        wait(1.0);
        say("E décroche...");
        e.pickUp(call);
        wait(3.0);
        say("E raccroche...");
        e.hangUp();
        report(d);
        report(e);
        say(SEP);
    }
    
    /**
     * Une communication établie par F avec G et H.
     * F appelle G.
     * G décroche.
     * F appelle H qui ne décroche pas.
     * G raccroche.
     * (F reste en attente de H et la communication est en suspens)
     */
    private void runTest3() {
        ICustomer f = new Customer("F", 76000);
        ICustomer g = new Customer("G", 76000);
        ICustomer h = new Customer("H", 34000);

        say("F appelle G...");
        f.call(g);
        ICall call = f.getCall();
        wait(1.0);
        say("G décroche...");
        g.pickUp(call);
        wait(2.0);
        say("F appelle H...");
        f.call(h);
        wait(2.0);
        say("G raccroche...");
        g.hangUp();
        report(f);
        report(g);
        report(h);
    }
    
    private void say(String s) {
        System.err.println(s);
    }
    
    /**
     * Attend delay secondes.
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
