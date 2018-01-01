package telecom.v1;

import telecom.v1.simulate.Simulation;

public final class Telecom {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        System.out.println("\n... Simulation ...\n");
        simulation.run();
    }
}
