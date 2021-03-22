import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Multiply {
    private ArrayList<Differential> list = new ArrayList<>();
    
    public Multiply(String term) {
        Constant con = new Constant("1");
        String fin = term;
        if (term.length() == 0) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (fin.charAt(0) == '+') {
                fin = fin.substring(1);
                con.setNum(BigInteger.ONE);
            } else if (fin.charAt(0) == '-') {
                fin = fin.substring(1);
                con.setNum(new BigInteger("-1"));
            }
            if (fin.length() == 0) {
                return;
            }
        }
        ArrayList<String> listIn = splitTerm(fin);
        addTerm(listIn, con);
    }
    
    private ArrayList<String> splitTerm(String fin) {
        int stage = 0;
        int times = 0;
        int begin = 0;
        int end = 0;
        ArrayList<String> list = new ArrayList<>();
        while (end < fin.length()) {
            char dec = fin.charAt(end);
            if (stage == 0) {
                if (dec == '*') {
                    list.add(fin.substring(begin, end));
                    begin = end + 1;
                } else if (dec == '(' || dec == '[') {
                    stage = 1;
                    times = 1;
                }
            } else {
                if (dec == '(' || dec == '[') {
                    times++;
                } else if (dec == ']' || dec == ')') {
                    times--;
                }
                if (times == 0) {
                    stage = 0;
                }
            }
            end++;
        }
        list.add(fin.substring(begin,end));
        return list;
    }
    
    private void addTerm(ArrayList<String> term, Constant con) {
        Pattern p1 = Pattern.compile("[+-]?[0-9]+");
        Pattern p2 = Pattern.compile("x(\\^[+-]?[0-9]+)?");
        Pattern p3 = Pattern.compile("(sin|cos)\\[x\\](\\^[+-]?[0-9]+)?");
        Pattern p4 = Pattern.compile("(sin|cos)\\[[+-]?\\d+\\](\\^[+-]?[0-9]+)?");
        Power power = new Power();
        Triangle tri0 = new Triangle(0, 0);
        Triangle tri1 = new Triangle(1, 0);
        for (String travel : term) {
            Matcher match1 = p1.matcher(travel);//match Constant
            Matcher match2 = p2.matcher(travel);//match Power
            Matcher match3 = p3.matcher(travel);//match Triangle
            Matcher match4 = p4.matcher(travel);//match Contain
            if (travel.startsWith("(") && travel.endsWith(")")) { //match Poly
                this.list.add(new Plus(travel.substring(1, travel.length() - 1)));
            } else if (match1.matches()) {
                con.setNum(new BigInteger(travel));
            } else if (match2.matches()) {
                power.setPower(getIn(travel));
            } else if (match3.matches()) {
                int now = getIn(travel);
                if (travel.startsWith("sin")) {
                    tri0.setIndex(now);
                } else {
                    tri1.setIndex(now);
                }
            } else if (match4.matches()) {
                list.add(new Constant(travel));
            }
            else {
                Contain now = new Contain(travel);
                if (now.getIndex() != 0) {
                    list.add(now);
                }
            }
        }
        list.add(con);
        if (power.getIndex() != 0) {
            list.add(power);
        }
        if (tri0.getIndex() != 0) {
            list.add(tri0);
        }
        if (tri1.getIndex() != 0) {
            list.add(tri1);
        }
    }
    
    private int getIn(String str) {
        String[] li = str.split("\\^");
        if (li.length == 1) {
            return 1;
        } else {
            return Integer.parseInt(li[1]);
        }
    }
    
    public String differential() {
        String out = "(";
        for (Differential diff1 : list) {
            for (Differential diff2 : list) {
                if (diff2 != diff1) {
                    out = out + diff2.origin() + '*';
                } else {
                    if (diff2 instanceof Constant) {
                        out = out.concat("0*");
                        break;
                    }
                    out = out + diff2.differential() + '*';
                }
            }
            out = out.substring(0, out.length() - 1) + '+';
        }
        out = out.substring(0, out.length() - 1) + ')';
        return out;
    }
}
