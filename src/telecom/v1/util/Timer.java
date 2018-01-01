package telecom.v1.util;


import static telecom.v1.simulate.Simulation.SECOND_DURATION;

public class Timer {
    
    // ATTRIBUTS
    
    private static final int NOT_YET_STARTED = -1;
    private static final int NOT_YET_STOPPED = -2;

    private long startTime;
    private long stopTime;
    
    // CONSTRUCTEURS
    
    public Timer() {
        startTime = NOT_YET_STARTED;
        stopTime = NOT_YET_STOPPED;
    }

    // REQUETES
    /**
     * Il faut avoir démarré puis arrêté le Timer pour obtenir le temps.
     */
    public int getTime() {
        Contract.checkCondition(isStopped());
        
        return (int) ((stopTime - startTime) / SECOND_DURATION);
    }
    /**
     * Indique si le timer a été démarré et pas encore arrêté.
     */
    public boolean isStarted() {
        return startTime != NOT_YET_STARTED && stopTime == NOT_YET_STOPPED;
    }
    /**
     * Indique si le timer a été démarré puis arrêté.
     */
    public boolean isStopped() {
        return startTime != NOT_YET_STARTED && stopTime != NOT_YET_STOPPED;
    }

    // COMMANDES
    /**
     * On peut démarrer le Timer tout le temps.
     * Si on avait déjà démarré, on réinitialise.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        stopTime = NOT_YET_STOPPED;
        System.err.println("Timer démarré : " + startTime);
    }

    /**
     * Il faut avoir démarré et pas arrêté le Timer pour pouvoir l'arrêter.
     */
    public void stop() {
        Contract.checkCondition(isStarted());
        
        stopTime = System.currentTimeMillis();
        System.err.println("Timer arrêté : " + stopTime);
    }
}
