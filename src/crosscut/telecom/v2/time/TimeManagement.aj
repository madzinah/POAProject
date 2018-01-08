package crosscut.telecom.v2.time;

import telecom.v1.util.Contract;
import domain.telecom.v2.connect.ICustomer;

/**
 * TimeManagement
 */
public privileged aspect TimeManagement {

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
