public class Power implements Differential {
    private int index = 0;
    private String poly;
    
    public void setPower(int index) {
        this.index = this.index + index;
        if (this.index == 1) {
            poly = "x";
        } else if (this.index == 0) {
            poly = "1";
        } else {
            poly = "x**" + this.index;
        }
    }
    
    public String origin() { return poly; }
    
    public int getIndex() { return index; }
    
    public String differential() {
        if (index == 1) {
            return "1";
        } else {
            return index + "*x**" + Integer.toString(index - 1);
        }
    }
}
