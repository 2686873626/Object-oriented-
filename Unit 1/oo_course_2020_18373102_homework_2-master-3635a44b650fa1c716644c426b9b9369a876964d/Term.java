import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term {
    private static String index_term  = "(\\*\\*[+-]?[0-9]+)";
    private static String tri    = "(" + "(sin|cos)\\(x\\)" + index_term + "?" + ")";
    private static String power  = "(" + "x" + index_term + "?" + ")";
    private static String factor = "(" + power + "|" + tri + "|" + "([+-]?[0-9]+)" + ")";
    
    private BigInteger coef = new BigInteger("1");
    private BigInteger[] index = new BigInteger [3];//0->x 1->sin 2->cos
    
    public Index getIndex() { return new Index(index[0], index[1], index[2]); }
    
    public BigInteger getCoef() { return this.coef; }
    
    public void plusTerm(Term in) {
        this.coef = this.coef.add(in.getCoef());
    }
    
    public BigInteger getCos() { return index[2]; }
    
    public BigInteger getSin() { return index[1]; }
    
    public BigInteger getVar() { return index[0]; }
    
    public Term(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
        this.coef = a;
        this.index[0] = b;
        this.index[1] = c;
        this.index[2] = d;
    }
    
    public Term(String str) {
        Pattern p = Pattern.compile(factor);
        Matcher split = p.matcher(str);
        for (int i = 0; i < 3; i++) {
            index[i] = new BigInteger("0");
        }
        while (split.find()) {
            String now = str.substring(split.start(), split.end());
            if (now.matches(power) | now.matches(tri)) {
                String[] fac = now.split("\\*\\*");
                BigInteger facIndex = new BigInteger("0");
                if (fac.length == 1) {
                    facIndex = new BigInteger("1");
                } else {
                    facIndex = new BigInteger(fac[1]);
                }
                if (now.startsWith("x")) {
                    this.index[0] = this.index[0].add(facIndex);
                } else if (now.startsWith("sin")) {
                    this.index[1] = this.index[1].add(facIndex);
                } else if (now.startsWith("cos")) {
                    this.index[2] = this.index[2].add(facIndex);
                }
            } else {
                coef = coef.multiply(new BigInteger(now));
            }
        }
    }
    
    public Term differential(int num) {
        BigInteger newCoef = this.coef.multiply(this.index[num]);
        switch (num) {
            case 0: return new Term(newCoef, index[0].subtract(BigInteger.ONE), index[1], index[2]);
            case 1: return new Term(newCoef, index[0], index[1].subtract(BigInteger.ONE)
                    , index[2].add(BigInteger.ONE));
            default: return new Term(newCoef.multiply(new BigInteger("-1")), index[0],
                    index[1].add(BigInteger.ONE), index[2].subtract(BigInteger.ONE));
        }
    }
    
    public void printTerm(int condition) {
        boolean need = false;
        if (coef.compareTo(BigInteger.ZERO) > 0 && condition == 1) {
            System.out.print("+");
        }
        if (noIndex()) {
            System.out.print(coef);
            return;
        }
        if (coef.equals(new BigInteger("-1"))) {
            System.out.print("-");
        } else if (!coef.equals(BigInteger.ONE)) {
            System.out.print(coef);
            need = true;
        }
        for (int i = 0; i < 3; i++) {
            if (!index[i].equals(BigInteger.ZERO)) {
                if (need) {
                    System.out.print("*");
                }
                switch (i) {
                    case 0:
                        System.out.print("x");
                        break;
                    case 1:
                        System.out.print("sin(x)");
                        break;
                    default :
                        System.out.print("cos(x)");
                        break;
                }
                need = true;
                if (i == 0 && index[i].equals(new BigInteger("2"))) {
                    System.out.print("*x");
                } else if (!index[i].equals(BigInteger.ONE)) {
                    System.out.print("**" + index[i]);
                }
            }
        }
    }
    
    private boolean noIndex() {
        return index[0].equals(BigInteger.ZERO) &
                index[1].equals(BigInteger.ZERO) & index[2].equals(BigInteger.ZERO);
    }
    
    public int match(Term com) {
        if (com.getCoef().equals(this.coef) && com.getVar().equals(this.index[0])) {
            BigInteger cos = com.getCos();
            BigInteger sin = com.getSin();
            if (cos.equals(index[2].add(new BigInteger("2"))) &&
                    sin.equals(index[1].add(new BigInteger("-2")))) {
                return 1;
            } else  if (cos.equals(index[2].add(new BigInteger("-2"))) &&
                    sin.equals(index[1].add(new BigInteger("2")))) {
                return -1;
            }
        }
        return 0;
    }
    
    public void change(int i) {
        if (i == 1) {
            index[2] = this.index[2].subtract(new BigInteger("2"));
        } else {
            index[1] = this.index[1].subtract(new BigInteger("2"));
        }
    }
}
