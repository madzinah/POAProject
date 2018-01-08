import telecom.v1.util.Contract;

import domain.telecom.v2.connect.ICustomer;

/**
 * Billing
 */
public privileged aspect BillManagement {

	private int ICustomer.charge;
	
	int domain.telecom.v2.connect.Connection.getRate() {
        return type.rate;
    }
	
	int domain.telecom.v2.connect.Connection.getPrice() {
        Contract.checkCondition(state == State.DROPPED);

        return getDuration() * getRate();
    }
	
	void ICustomer.addCharge(int addedCharge) {
		charge += addedCharge;
	}
	
	int ICustomer.getCharge() {
		return charge;
	}
}