import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term {
    private BigInteger coe = new BigInteger("0");
    private BigInteger index = new BigInteger("0");
    private int state = 0;
    
    public Term(String str) {
        Pattern element = Pattern.compile("(\\*\\*)|[0-9]+|[\\+\\-\\*x]");
        Matcher finder = element.matcher(str);
        int status = 0;
        while (finder.find()) {
            String now = str.substring(finder.start(), finder.end());
            switch (status) {
                case 0: {
                    if (now.equals("+")) {
                        status = 0;
                    } else if (now.equals("-")) {
                        status = 1;
                    } else if (now.equals("x")) {
                        coe = new BigInteger("1");
                        index = new BigInteger("1");
                        status = 3;
                    } else {
                        coe = new BigInteger(now);
                        status = 2;
                    }
                    break;
                } case 1: {
                    if (now.equals("+")) {
                        status = 1;
                    } else if (now.equals("-")) {
                        status = 0;
                    } else if (now.equals("x")) {
                        coe = new BigInteger("-1");
                        index = new BigInteger("1");
                        status = 3;
                    } else {
                        coe = new BigInteger(now);
                        coe = coe.subtract(coe.multiply(new BigInteger("2")));
                        status = 2;
                    }
                    break;
                } case 2: {
                    if (now.equals("x")) {
                        status = 3;
                        index = new BigInteger("1");
                    }
                    break;
                } case 3: {
                    if (now.equals("**")) {
                        status = 4;
                    }
                    break;
                } case 4: {
                    if (now.equals("+")) {
                        status = 4;
                    } else if (now.equals("-")) {
                        status = 5;
                    } else {
                        index = new BigInteger(now);
                    }
                    break;
                } case 5: {
                    index = new BigInteger(now);
                    index = index.subtract(index.multiply(new BigInteger("2")));
                    break;
                } default: {
                    status = 0;
                }
            }
        }
    }
    
    public void plusTerm(BigInteger coe) {
        this.coe = this.coe.add(coe);
    }
    
    public void differential() {
        if (this.index.equals(new BigInteger("0"))) {
            this.coe = this.coe.subtract(coe);
        } else {
            this.coe = this.coe.multiply(this.index);
            this.index = this.index.subtract(new BigInteger("1"));
        }
    }
    
    public BigInteger getCoe() {
        return coe;
    }
    
    public BigInteger getIndex() {
        return index;
    }
}
