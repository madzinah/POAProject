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
     * Il faut avoir d�marr� puis arr�t� le Timer pour obtenir le temps.
     */
    public int getTime() {
        Contract.checkCondition(isStopped());
        
        return (int) ((stopTime - startTime) / SECOND_DURATION);
    }
    /**
     * Indique si le timer a �t� d�marr� et pas encore arr�t�.
     */
    public boolean isStarted() {
        return startTime != NOT_YET_STARTED && stopTime == NOT_YET_STOPPED;
    }
    /**
     * Indique si le timer a �t� d�marr� puis arr�t�.
     */
    public boolean isStopped() {
        return startTime != NOT_YET_STARTED && stopTime != NOT_YET_STOPPED;
    }

    // COMMANDES
    /**
     * On peut d�marrer le Timer tout le temps.
     * Si on avait d�j� d�marr�, on r�initialise.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        stopTime = NOT_YET_STOPPED;
        System.err.println("Timer d�marr� : " + startTime);
    }

    /**
     * Il faut avoir d�marr� et pas arr�t� le Timer pour pouvoir l'arr�ter.
     */
    public void stop() {
        Contract.checkCondition(isStarted());
        
        stopTime = System.currentTimeMillis();
        System.err.println("Timer arr�t� : " + stopTime);
    }
}
