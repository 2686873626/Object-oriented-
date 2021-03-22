import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contain implements Differential {
    private int type = 0;//0->sin 1->cos
    private Differential element;
    private String fx;
    private int index;
    private String poly;
    
    public Contain(String poly) {
        this.poly = poly;
        if (poly.startsWith("sin")) {
            type = 0;
        } else {
            type = 1;
        }
        int end = poly.length() - 1;
        index = 1;
        while (poly.charAt(end) != ']') {
            if (poly.charAt(end) == '^') {
                index = Integer.parseInt(poly.substring(end + 1));
            }
            end--;
        }
        int begin = 0;
        while (poly.charAt(begin) != '[') {
            begin++;
        }
        fx = poly.substring(begin + 1, end);
        Pattern p = Pattern.compile("x\\^[+-]?[0-9]+");
        Pattern q = Pattern.compile("(sin|cos)\\[x\\](\\^[+-]?[0-9]+)?");
        Pattern a = Pattern.compile("[+-]?[0-9]+");
        Matcher match1 = p.matcher(fx);
        Matcher match2 = q.matcher(fx);
        Matcher match3 = a.matcher(fx);
        if (fx.startsWith("(") && fx.endsWith(")")) {
            element = new Plus(fx.substring(1, fx.length() - 1));
        } else if (match1.matches()) {
            String[] now = fx.split("\\^");
            Power po = new Power();
            po.setPower(Integer.parseInt(now[1]));
            element = po;
        }  else if (match2.matches()) {
            String[] now = fx.split("\\^");
            if (now.length == 1) {
                if (fx.startsWith("sin")) {
                    element = new Triangle(0, 1);
                } else {
                    element = new Triangle(1, 1);
                }
            } else {
                int dex = Integer.parseInt(now[1]);
                if (fx.startsWith("sin")) {
                    element = new Triangle(0, dex);
                } else {
                    element = new Triangle(1, dex);
                }
            }
        } else if (match3.matches()) { element = new Constant(Integer.toString(index)); }
        else {
            element = new Contain(fx);
        }
    }
    
    public int getIndex() { return index; }
    
    public String differential() {
        if (type == 0) {
            if (index == 1) {
                return "cos(" + fx + ")*" + element.differential();
            } else {
                return index + "*sin(" + fx + ")**" + Integer.toString(index - 1)
                        + "*cos(" + fx + ")*" + element.differential();
            }
        } else {
            if (index == 1) {
                return "sin(" + fx + ")*" + element.differential() + "*-1";
            } else {
                return index + "*cos(" + fx + ")**" + Integer.toString(index - 1)
                        + "*sin(" + fx + ")*" + element.differential() + "*-1";
            }
        }
    }
    
    public String origin() { return poly; }
}
