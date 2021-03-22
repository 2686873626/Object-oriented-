public class Triangle implements Differential {
    private int index = 0;
    private int type = 0;//0->sin 1->cos
    private String poly;
    
    public Triangle(int type, int index) {
        this.type = type;
        this.index = index;
        setPoly();
    }
    
    public void setIndex(int index) {
        this.index = this.index + index;
        setPoly();
    }
    
    public int getIndex() { return index; }
    
    public String differential() {
        if (type == 0) {
            if (index == 1) {
                return "cos(x)";
            } else {
                return index + "*sin(x)**" + Integer.toString(index - 1) + "*cos(x)";
            }
        } else {
            if (index == 1) {
                return "sin(x)*-1";
            } else {
                return index + "*cos(x)**" + Integer.toString(index - 1) + "*sin(x)*-1";
            }
        }
    }
    
    private void setPoly() {
        if (type == 0) {
            if (index == 1) {
                poly = "sin(x)";
            } else if (index == 0) {
                poly = "1";
            } else {
                poly = "sin(x)**" + index;
            }
        } else {
            if (index == 1) {
                poly = "cos(x)";
            } else if (index == 0) {
                poly = "1";
            } else {
                poly = "cos(x)**" + index;
            }
        }
    }
    
    public String origin() { return poly; }
}
