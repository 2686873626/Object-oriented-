import java.util.ArrayList;

public class Plus implements Differential {
    private ArrayList<Multiply> list = new ArrayList<>();
    private String poly;
    
    public Plus(String poly) {
        this.poly = poly;
        int begin = 0;
        int end = 0;
        int stage = 0;
        int times = 0;
        while (end < poly.length()) {
            char dec = poly.charAt(end);
            switch (stage) {
                case 0:
                    if (dec == '*' || dec == '^') {
                        stage = 2;
                    } else if (dec == '(' || dec == '[') {
                        stage = 3;
                        times = 1;
                    } else if (dec != '+' && dec != '-') {
                        stage = 1;
                    }
                    break;
                case 1:
                    if (dec == '+' || dec == '-') {
                        list.add(new Multiply(poly.substring(begin, end)));
                        begin = end;
                        stage = 0;
                    } else if (dec == '(' || dec == '[') {
                        stage = 3;
                        times = 1;
                    } else if (dec == '*' || dec == '^') {
                        stage = 2;
                    }
                    break;
                case 2:
                    if (dec == '(' || dec == '[') {
                        stage = 3;
                        times = 1;
                    } else if (dec != '+' && dec != '-') {
                        stage = 1;
                    }
                    break;
                default:
                    if (dec == '(' || dec  == '[') {
                        times++;
                    } else if (dec == ')' || dec  == ']') {
                        times--;
                    }
                    if (times == 0) {
                        stage = 1;
                    }
                    break;
            }
            end++;
        }
        list.add(new Multiply(poly.substring(begin, end)));
    }
    
    public String differential() {
        String out = new String("(");
        for (Multiply travel : list) {
            out = out + travel.differential() + '+';
        }
        out = out.substring(0, out.length() - 1) + ')';
        return out;
    }
    
    public String origin() { return '(' + poly + ')'; }
}
