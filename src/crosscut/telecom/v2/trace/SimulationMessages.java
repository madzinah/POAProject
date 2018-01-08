package crosscut.telecom.v2.trace;

import org.aspectj.lang.JoinPoint;

import domain.telecom.v2.connect.ICall;
import domain.telecom.v2.connect.ICustomer;

public enum SimulationMessages {
    
    CUSTOMER_CALL("%1$s appelle %2$s %3$s") {
        @Override public String format(JoinPoint jp) {
            ICustomer caller = (ICustomer) jp.getTarget();
            ICustomer receiver = (ICustomer) jp.getArgs()[0];
            return String.format(template,
                    caller.getName(), receiver.getName(),
                    listToString(caller, receiver));
        }
    },
    
    CUSTOMER_HANGUP("%1$s raccroche%2$s %3$s") {
        @Override public String format(JoinPoint jp) {
            ICustomer target = (ICustomer) jp.getTarget();
            ICall call = target.getCall();
            ICustomer caller = call.getCaller();
            ICustomer receiver = null;
            String s = "";
            if (caller != target) {
                s = " d'avec " + caller.getName();
                receiver = target;
            }
            return String.format(template, target.getName(), s,
                    listToString(caller, receiver));
        }
    },
    
    CUSTOMER_PICKUP("%1$s décroche à l'appel de %2$s %3$s") {
        @Override public String format(JoinPoint jp) {
            ICall call = (ICall) jp.getArgs()[0];
            ICustomer caller = call.getCaller();
            ICustomer receiver = (ICustomer) jp.getTarget();
            return String.format(template,
                    receiver.getName(), caller.getName(),
                    listToString(caller, receiver));
        }
    },
    
    CUSTOMER_FINAL("+-- %1$s") {
        @Override public String format(JoinPoint jp) {
            ICustomer caller = null;
            ICustomer receiver = null;
            if (jp.getArgs().length == 0) {
                // (caller|receiver).hangUp()
                ICustomer target = (ICustomer) jp.getTarget();
                ICall call = target.getCall();
                if (call != null) {
                    caller = call.getCaller();
                    if (target != caller) {
                        receiver = target;
                    }
                } else {
                    caller = target;
                }
            } else {
                if (jp.getArgs()[0] instanceof ICustomer) {
                    // caller.call(receiver)
                    receiver = (ICustomer) jp.getArgs()[0];
                    caller = (ICustomer) jp.getTarget();
                } else {
                    // receiver.pickUp(call)
                    ICall call = (ICall) jp.getArgs()[0];
                    caller = call.getCaller();
                    receiver = (ICustomer) jp.getTarget();
                }
            }
            return String.format(template, listToString(caller, receiver));
        }
    },
    
    CALL_INVITE("%1$s est invité à une conférence par %2$s %3$s") {
        @Override public String format(JoinPoint jp) {
            ICall call = (ICall) jp.getTarget();
            ICustomer caller = call.getCaller();
            ICustomer receiver = (ICustomer) jp.getArgs()[0];
            return String.format(template,
                    receiver.getName(), caller.getName(),
                    listToString(caller, receiver));
        }
    },
    
    CALL_HANGUP("%1$s se déconnecte %2$s") {
        @Override public String format(JoinPoint jp) {
            ICall call = (ICall) jp.getTarget();
            ICustomer caller = call.getCaller();
            ICustomer customer = (ICustomer) jp.getArgs()[0];
            if (customer == caller) {
                caller = null;
            }
            return String.format(template, customer.getName(),
                    listToString(caller, customer));
        }
    },
    
    CALL_PICKUP("%1$s se connecte à %2$s %3$s") {
        @Override public String format(JoinPoint jp) {
            ICall call = (ICall) jp.getTarget();
            ICustomer caller = call.getCaller();
            ICustomer receiver = (ICustomer) jp.getArgs()[0];
            return String.format(template,
                    receiver.getName(), caller.getName(),
                    listToString(caller, receiver));
        }
    },
    
    CALL_FINAL("+-- %1$s") {
        @Override public String format(JoinPoint jp) {
            ICall call = (ICall) jp.getTarget();
            ICustomer caller = call.getCaller();
            ICustomer customer = (ICustomer) jp.getArgs()[0];
            if (customer == caller) {
                customer = null;
            }
            return String.format(template, listToString(caller, customer));
        }
    };
    
    // ATTRIBUTS
    
    protected String template;
    
    // CONSTRUCTEURS
    
    SimulationMessages(String tpl) {
        template = tpl;
    }
    
    // REQUETES
    
    public abstract String format(JoinPoint jp);
    
    static SimulationMessages get(Class<?> type, String methodName) {
        String name = type.getSimpleName().toUpperCase()
                + "_" + methodName.toUpperCase();
        try {
            return valueOf(name);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    // OUTILS
    
    private static String listToString(ICustomer... customers) {
        String res = "";
        boolean first = true;
        for (ICustomer c : customers) {
            if (c == null) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                res += ", ";
            }
            res += c.getName() + ".call = " + c.getCall();
        }
        return "[" + res + "]";
    }
}
