public class Poly {
    private String poly = new String();
    private boolean wrFmt = false;
    private Plus head;
    
    public Poly(String str, boolean t) {
        poly = str;
        wrFmt = t;
    }
    
    public boolean getWrFmt() {
        return wrFmt;
    }
    
    public void generate() {
        head = new Plus(poly);
    }
    
    public String differential() {
        return head.differential();
    }
}
