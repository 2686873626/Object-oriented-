import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly {
    private static String index  = "(\\*\\*[+-]?[0-9]+)";
    private static String tri    = "(" + "(sin|cos)\\(x\\)" + index + "?" + ")";
    private static String power  = "(" + "x" + index + "?" + ")";
    private static String factor = "(" + power + "|" + tri + "|" + "([+-]?[0-9]+)" + ")";
    private static String term   = "(" + "[+-]?" + factor + "(\\*" + factor + ")*" + ")";
    private static String poly   = "[+-]?" + term + "([+-]" + term + ")*";
    private static String term_sp   = "(" + "[+-]{0,2}" + factor + "(\\*" + factor + ")*" + ")";
    private static String power_ch  = "x" + index;
    
    private String formula;
    private boolean wrFormat;
    private ArrayList<Term> list = new ArrayList<>();
    private HashMap<Index,Term> diff = new HashMap<>();
    private ArrayList<Term> fin = new ArrayList<>();
    
    public Poly(String str) {
        this.formula = str;
        wrFormat = false;
        polyCheck();
        if (!this.wrFormat) {
            genList();
        }
    }
    
    private void polyCheck() {
        Pattern p = Pattern.compile(poly);
        Matcher right = p.matcher(this.formula);
        if (!right.matches()) {
            wrFormat = true;
        }
        Pattern q = Pattern.compile(power_ch);
        right = q.matcher(this.formula);
        while (right.find()) {
            String now = this.formula.substring(right.start(), right.end());
            String[] splitter = now.split("\\*\\*");
            BigInteger num = new BigInteger(splitter[1]);
            if (num.compareTo(new BigInteger("10000")) > 0 ||
                    num.compareTo(new BigInteger("-10000")) < 0) {
                wrFormat = true;
            }
        }
    }
    
    public void differential() {
        for (Term t : list) {
            for (int j = 0; j < 3; j++) {
                Term end = t.differential(j);
                if (!end.getCoef().equals(new BigInteger("0"))) {
                    addTerm(end);
                }
            }
        }
        simplify();
    }
    
    private void genList() {
        Pattern q = Pattern.compile(term_sp);
        Matcher split = q.matcher(formula);
        while (split.find()) {
            String now = formula.substring(split.start(), split.end());
            now = GetPoly.dealStart(now);
            list.add(new Term(now));
        }
    }
    
    private void addTerm(Term term) {
        Index group = term.getIndex();
        if (this.diff.containsKey(group)) {
            this.diff.get(group).plusTerm(term);
        } else {
            this.diff.put(group, term);
        }
    }
    
    public boolean getWrong() { return wrFormat; }
    
    public void print() {
        int monitor = 0;
        for (Term print: fin) {
            if (print.getCoef().compareTo(BigInteger.ZERO) > 0) {
                print.printTerm(0);
                fin.remove(print);
                monitor = 1;
                break;
            }
        }
        for (Term print: fin) {
            if (!print.getCoef().equals(BigInteger.ZERO)) {
                print.printTerm(1);
                monitor = 1;
            }
        }
        if (monitor == 0) {
            System.out.println("0");
        }
    }
    
    private void simplify() {
        for (Index now: diff.keySet()) {
            fin.add(diff.get(now));
        }
        for (int i = 0; i < fin.size(); i++) {
            Term coma = fin.get(i);
            for (int j = i + 1; j < fin.size(); j++) {
                Term comb = fin.get(j);
                if (coma.match(comb) > 0) {
                    comb.change(1);
                    fin.remove(i);
                    i--;
                    break;
                } else if (coma.match(comb) < 0) {
                    comb.change(-1);
                    fin.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
}
