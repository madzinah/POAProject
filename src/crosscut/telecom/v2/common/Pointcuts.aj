package crosscut.telecom.v2.common;

import java.util.HashSet;
import java.util.Set;

import crosscut.telecom.v2.time.Timer;
import crosscut.telecom.v2.unicity.UniqueId;
import domain.telecom.v2.util.NotUniqueException;
import telecom.v1.util.Contract;
import domain.telecom.v2.connect.ICustomer;
import crosscut.telecom.v2.billing.*;

public privileged aspect Pointcuts {
	
	/**
	 * Unicity
	 * 
	 * Check if the field annotated by @UniqueId is final String.
	 * 
	 * Also check if every assignment of @UniqueId fields are unique.
	 */
	
	private static final Set<String> uniqueIdSet = new HashSet<String>();

	declare error : set(@UniqueId !final * *.*) || get(@UniqueId !final * *.*): "Erreur, l'attribut doit être final";
	declare error : set(@UniqueId !String *.*) || get(@UniqueId !String *.*): "Erreur, l'attribut doit être une String";
	
	pointcut UniqueAssignment(String s) : set(@UniqueId * *.*) && args(s);
	
	after(String s) : UniqueAssignment(s) {
		if (!uniqueIdSet.add(s)) {
			throw new NotUniqueException( s + " is not a unique String");
		}
	}
	
	/**
	 * Time
	 * 
	 */
	
	enum Type {
        LOCAL(3),
        NATIONAL(10);
        private int rate;
        Type(int r) {
            rate = r;
        }
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
	
	private Timer domain.telecom.v2.connect.Connection.timer;
	private Type domain.telecom.v2.connect.Connection.type;
	
	private int ICustomer.totalTime;
	 
	public void ICustomer.addTime(int time) {
		totalTime += time;
	}
	
	public int ICustomer.getTime() {
		return totalTime;
	}
	
	int domain.telecom.v2.connect.Connection.getDuration() {
        Contract.checkCondition(this.state == State.DROPPED);
        
        return this.timer.getTime();
    }
	
	pointcut ConnectionCreation(domain.telecom.v2.connect.Connection c, ICustomer cu, ICustomer r) : 
		execution(domain.telecom.v2.connect.Connection.new(..)) && this(c) && args(cu, r);
	
	after(domain.telecom.v2.connect.Connection c, ICustomer cu, ICustomer r) : ConnectionCreation(c, cu, r) {
		c.timer = new Timer();
		
		if (cu.getAreaCode() == r.getAreaCode()) {
            c.type = Type.LOCAL;
        } else {
            c.type = Type.NATIONAL;
        }
	}
	
	pointcut ConnectionCompleted(domain.telecom.v2.connect.Connection c) : 
		execution(void domain.telecom.v2.connect.Connection.complete()) && this(c);
	
	after(domain.telecom.v2.connect.Connection c) : ConnectionCompleted(c) {
		c.timer.start();
	}
	
	pointcut ConnectionDropped(domain.telecom.v2.connect.Connection c) : 
		execution(void domain.telecom.v2.connect.Connection.drop()) && this(c);
	
	after(domain.telecom.v2.connect.Connection c) : ConnectionDropped(c) {
		c.timer.stop();
		c.caller.addTime(c.getDuration());
		c.caller.addCharge(c.getPrice());
	}
}
