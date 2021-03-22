import java.math.BigInteger;

public class Index {
    private BigInteger sinIndex = new BigInteger("0");
    private BigInteger cosIndex = new BigInteger("0");
    private BigInteger varIndex   = new BigInteger("0");
    
    public Index(BigInteger a, BigInteger b, BigInteger c) {
        this.varIndex = a;
        this.sinIndex = b;
        this.cosIndex = c;
    }
    
    public BigInteger getSinIndex() { return sinIndex; }
    
    public BigInteger getCosIndex() { return cosIndex; }
    
    public BigInteger getVarIndex() { return varIndex; }
    
    @Override
    public boolean equals(Object a) {
        if (a instanceof Index) {
            if (((Index) a).getCosIndex().equals(this.cosIndex) && ((Index) a).getSinIndex().equals(
                    this.sinIndex) && ((Index) a).getVarIndex().equals(this.varIndex)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.sinIndex.hashCode();
        result += this.cosIndex.hashCode();
        result += this.varIndex.hashCode();
        return result;
    }
}
