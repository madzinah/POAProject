package telecom.v1.util;

import java.util.Set;

import telecom.v1.connect.Call;
import telecom.v1.connect.Customer;
import telecom.v1.connect.ICall;
import telecom.v1.connect.ICustomer;
import telecom.v1.simulate.Simulation;

public aspect Scattering {
    
    pointcut inDomain() :
        within(telecom.v1..*)
        || within(telecom.v2.connect.*) || within(telecom.v2.simulate.*);

    pointcut duringSimulation() :
        cflowbelow(call(void Simulation.run()));
    
    pointcut unicity() :
        get(static Set<String>+ Customer.UNIQUE_IDS)
        || set(static Set<String>+ Customer.UNIQUE_IDS);
    
    pointcut tracing() :
        call(void java.io.PrintStream.print*(..))
        || call(void say(..))
        || call(void report(..))
        || call(String toString())
        || call(String setToString(..));
    
    pointcut timing() :
        call(* Timer.*(..))
        || call(Timer.new(..))

        || get(Timer telecom.v1.connect.Connection.*)
        || set(Timer telecom.v1.connect.Connection.*)
        || call(int telecom.v1.connect.Connection.getDuration())

        || call(int ICall.getElapsedTimeFor(..))

        || get(int Customer.totalTime)
        || set(int Customer.totalTime)
        || call(int ICustomer.getTotalConnectedTime());
    
    pointcut billing() :
        get(telecom.v1.connect.Connection.Type telecom.v1..Connection.*)
        || set(telecom.v1.connect.Connection.Type telecom.v1..Connection.*)
        || call(int telecom.v1.connect.Connection.getRate())
        || call(int telecom.v1.connect.Connection.getPrice())
        || get(int telecom.v1.connect.Connection.Type.rate)
        || set(int telecom.v1.connect.Connection.Type.rate)

        || get(int Call.price)
        || set(int Call.price)
        || call(int ICall.getTotalPrice())

        || get(int Customer.charge)
        || set(int Customer.charge)
        || call(int ICustomer.getCharge());
    
    declare warning : unicity() && inDomain()
        : "unicity scattering !";
    declare warning : tracing() && inDomain()
        : "tracing scattering !";
    declare warning : timing() && inDomain()
        : "timing scattering !";
    declare warning : billing() && inDomain()
        : "billing scattering !";
}
