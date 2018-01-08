package crosscut.telecom.v2.unicity;

import java.util.HashSet;
import java.util.Set;

import domain.telecom.v2.util.NotUniqueException;

/**
 * EnforceUnicity
 * 
 * Check if the field annotated by @UniqueId is final String.
 * 
 * Also check if every assignment of @UniqueId fields are unique.
 */
public privileged aspect EnforceUnicity {

	private static final Set<String> uniqueIdSet = new HashSet<String>();

	declare error : set(@UniqueId !final * *.*) || get(@UniqueId !final * *.*): "Erreur, l'attribut doit être final";
	declare error : set(@UniqueId !String *.*) || get(@UniqueId !String *.*): "Erreur, l'attribut doit être une String";
	
	pointcut UniqueAssignment(String s) : set(@UniqueId * *.*) && args(s);
	
	after(String s) : UniqueAssignment(s) {
		if (!uniqueIdSet.add(s)) {
			throw new NotUniqueException( s + " is not a unique String");
		}
	}
}
