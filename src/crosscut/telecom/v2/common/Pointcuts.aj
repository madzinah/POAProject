package crosscut.telecom.v2.common;

import java.util.HashSet;
import java.util.Set;

import crosscut.telecom.v2.unicity.UniqueID;
import domain.telecom.v2.util.NotUniqueException;

public privileged aspect Pointcuts {
	
	private static final Set<String> uniqueIdSet = new HashSet<String>();
	

	declare error : set(@UniqueID !final * *.*) || get(@UniqueID !final * *.*): "Erreur, l'attribut doit être final";
	declare error : set(@UniqueID !String *.*) || get(@UniqueID !String *.*): "Erreur, l'attribut doit être une String";
	
	pointcut UniqueAssignement(String s) : set(@UniqueID * *.*) && args(s);
	
	after(String s) : UniqueAssignement(s) {
		if (!uniqueIdSet.add(s)) {
			throw new NotUniqueException();
		}
	}
	
}
