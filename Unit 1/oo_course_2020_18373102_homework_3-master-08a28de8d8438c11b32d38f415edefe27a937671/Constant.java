import java.math.BigInteger;

public class Constant implements Differential {
    private BigInteger num = new BigInteger("1");
    private String poly = new String("1");
    
    public Constant(String str) {
        poly = str;
    }
    
    public void setNum(BigInteger num) {
        this.num = this.num.multiply(num);
        poly = this.num.toString();
    }
    
    public String origin() { return poly; }
    
    public String differential() {
        return "0";
    }
}
