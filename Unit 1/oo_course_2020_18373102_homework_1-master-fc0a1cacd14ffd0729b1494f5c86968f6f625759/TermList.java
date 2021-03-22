import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class TermList {
    private TreeMap<BigInteger, Term> list = new TreeMap<>();
    private int monitor = 0;
    
    public void addTerm(Term term) {
        term.differential();
        if (list.containsKey(term.getIndex())) {
            list.get(term.getIndex()).plusTerm(term.getCoe());
        } else {
            list.put(term.getIndex(), term);
        }
    }
    
    public void listPrint(BigInteger index) {
        BigInteger coe = list.get(index).getCoe();
        String ref = new String();
        if (coe.compareTo(new BigInteger("0")) > 0) {
            ref = "+" + coe.toString();
        } else if (coe.compareTo(new BigInteger("0")) < 0) {
            ref = coe.toString();
        } else {
            return;
        }
        if (index.equals(new BigInteger("1"))) {
            if (coe.equals(new BigInteger("1"))) {
                ref = new String("+x");
            } else if (coe.equals(new BigInteger("-1"))) {
                ref = new String("-x");
            } else {
                ref = ref + "*x";
            }
        } else if (!index.equals(new BigInteger("0"))) {
            if (coe.equals(new BigInteger("1"))) {
                ref = new String("+x**") + index.toString();
            } else if (coe.equals(new BigInteger("-1"))) {
                ref = new String("-x**") + index.toString();
            } else {
                ref = ref + "*x**" + index.toString();
            }
        }
        System.out.print(ref);
        monitor = 1;
    }
    
    public TreeSet<BigInteger> getMap() {
        return new TreeSet<>(list.keySet());
    }
    
    public int getMonitor() {
        return monitor;
    }
    
    public void firstPrint() {
        Iterator iter = list.keySet().iterator();
        BigInteger key;
        BigInteger coe;
        while (iter.hasNext()) {
            key = (BigInteger)iter.next();
            coe = list.get(key).getCoe();
            if (coe.compareTo(new BigInteger("0")) > 0) {
                String ref = new String(coe.toString());
                if (key.equals(new BigInteger("0"))) {
                    System.out.print(ref);
                } else if (key.equals(new BigInteger("1"))) {
                    System.out.print(ref + "*x");
                } else {
                    System.out.print(ref + "*x**" + key);
                }
                list.remove(key);
                monitor = 1;
                return;
            }
        }
    }
}
