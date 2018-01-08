package crosscut.telecom.v2.trace;

import org.aspectj.lang.JoinPoint;

import domain.telecom.v2.connect.ICall;
import domain.telecom.v2.connect.ICustomer;

public privileged aspect SimulationTracing {
 
	// Point de coupe sur un appel
	pointcut catchCall(ICustomer c, ICustomer r) :
		execution(void domain.telecom.v2.connect.Customer.call(ICustomer)) && this(c) && args(r);
	
	// Point de coupe sur le temps d'attente / de communication
	pointcut catchTimeWait() :
		execution(void domain.telecom.v2.simulate.Simulation.wait(double));
	
	// Point de coup sur réponse à appel
	pointcut catchPickUp(ICustomer r, ICall c) :
		execution(void domain.telecom.v2.connect.Customer.pickUp(ICall)) && this(r) && args(c);

	pointcut catchConnection(domain.telecom.v2.connect.Connection c, ICustomer c1, ICustomer c2) :
		execution(domain.telecom.v2.connect.Connection.new(ICustomer, ICustomer)) && this(c) && args(c1, c2);
	
	pointcut catchGetCall(ICall c) :
		call(ICall domain.telecom.v2.connect.Customer.getCall()) && this(c);
	
	// Point de coupe sur un raccrochement de téléphone
	pointcut catchHangUp(ICustomer c) :
		execution(void domain.telecom.v2.connect.Customer.hangUp()) && this(c);
	
	before(ICustomer c, ICustomer r) : catchCall(c, r) {
		domain.telecom.v2.connect.Connection conn = new domain.telecom.v2.connect.Connection(c, r);
		printMessage(r, thisJoinPoint);
	}
	after(ICustomer c, ICustomer r) : catchCall(c, r) {
		System.out.println("");
		SimulationMessages sm = SimulationMessages.get(c.getClass(), "final");
		System.out.println(sm.format(thisJoinPoint));
		System.out.println("");
	}
	
	before(ICustomer c) : catchHangUp(c) {
		printMessage(c, thisJoinPoint);
	}
	after(ICustomer c) : catchHangUp(c) {
		SimulationMessages sm = SimulationMessages.get(c.getClass(), "final");
		System.out.println(sm.format(thisJoinPoint));
		System.out.println("");
	}

	before(ICustomer r, ICall c) : catchPickUp(r, c) {
		printMessage(r, thisJoinPoint);
	}
	
	before() : catchTimeWait() {
		// TODO : enregistrer temps de communication
	}
	
	/**
	 * Affiche le message associé à
	 * 
	 * @param o		L'objet sujet
	 * @param jp	Le point de coupe associé
	 */
	private void printMessage(Object o, JoinPoint jp) {
		String methodName = jp.getSignature().getName();
		SimulationMessages sm = SimulationMessages.get(o.getClass(), methodName);
		System.out.println(sm.format(jp));
	}
}
