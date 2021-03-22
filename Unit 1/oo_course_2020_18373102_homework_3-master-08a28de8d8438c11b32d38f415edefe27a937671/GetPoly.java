import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPoly {
    public static Poly polyRead() {
        Scanner in = new Scanner(System.in);
        String form = in.nextLine();
        if (spaceCheck(form)) {
            return new Poly(form, true);
        } else {
            form = modify(form);
        }
        if (indexCheck(form)) {
            return new Poly(form, true);
        }
        if (polyCheck(form)) {
            return new Poly(form, true);
        }
        return new Poly(form, false);
    }
    
    private static boolean spaceCheck(String form) {
        String sin   = "(s\\s*i\\s+n)|(s\\s+in)";
        String cos  = "(c\\s*o\\s+s)|(c\\s+os)";
        String index = "(\\*\\s+\\*)";
        String num   = "(\\d\\s+\\d)";
        String sym  = "([+-]\\s*[+-]\\s*[+-]\\s+)|(\\*\\s*[+-]\\s+\\d)";
        String space = sin + "|" + cos + "|" + index + "|" + num +  "|" + sym;
        Pattern p = Pattern.compile(space);
        Matcher fd = p.matcher(form);
        if (fd.find()) {
            return true;
        }
        Pattern q = Pattern.compile("[^\\+\\-\\*\\(\\)\\s\\dcosinx]");
        fd = q.matcher(form);
        if (fd.find()) {
            return true;
        }
        Pattern st = Pattern.compile("(cos|sin)\\s*\\(\\s*[+-]\\s+[0-9]+\\s*\\)");
        fd = st.matcher(form);
        if (fd.find()) {
            return true;
        }
        int times = 0;
        for (int i = 0; i < form.length(); i++) {
            if (form.charAt(i) == '(') {
                times++;
            } else if (form.charAt(i) == ')') {
                times--;
            }
            if (times < 0) {
                return true;
            }
        }
        if (times > 0) {
            return true;
        }
        String after = form.replaceAll(" ", "");
        after = after.replaceAll("\t", "");
        Pattern r = Pattern.compile("sin|cos");
        Matcher bracket = r.matcher(after);
        while (bracket.find()) {
            if (bracket.end() == after.length()) {
                return true;
            }
            if (after.charAt(bracket.end()) != '(') {
                return true;
            }
        }
        return false;
    }
    
    private static String modify(String line) {
        String after = line.replaceAll(" ", "");
        after = after.replaceAll("\t", "");
        after = after.replaceAll("\\*\\*", "^");
        Pattern p = Pattern.compile("cos|sin");
        Matcher fd = p.matcher(after);
        while (fd.find()) {
            int times = 1;
            int sle   = fd.end() + 1;
            while (times != 0) {
                if (after.charAt(sle) == '(') {
                    times++;
                } else if (after.charAt(sle) == ')') {
                    times--;
                }
                sle++;
            }
            after = after.substring(0,fd.end()) + '[' + after.substring(fd.end() + 1, sle - 1)
                    + ']' + after.substring(sle);
        }
        return after;
    }
    
    private static boolean polyCheck(String str) {
        int begin = 0;
        int end = 0;
        int stage = 0;
        int times = 0;
        while (end < str.length()) {
            char dec = str.charAt(end);
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
                        if (termCheck(str.substring(begin, end))) {
                            return true;
                        }
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
        return termCheck(str.substring(begin, end));
    }
    
    private static boolean termCheck(String str) {
        String matchX  = "(x(\\^[+-]?[0-9]+)?)|([+-]?[0-9]+)";
        String match   = "(sin|cos)\\[\\S+\\](\\^[+-]?[0-9]+)?";
        String fin = str;
        if (fin.length() == 0) {
            return true;
        }
        for (int i = 0; i < 2; i++) {
            if (fin.charAt(0) == '+' || fin.charAt(0) == '-') {
                fin = fin.substring(1);
            }
            if (fin.length() == 0) {
                return true;
            }
        }
        ArrayList<String> list = splitTerm(fin);
        Pattern x  = Pattern.compile(matchX);
        Pattern m  = Pattern.compile(match);
        for (String travel : list) {
            Matcher match1 = x.matcher(travel);
            Matcher match2 = m.matcher(travel);
            if (travel.startsWith("(") && travel.endsWith(")")) {
                if (polyCheck(travel.substring(1, travel.length() - 1))) {
                    return true;
                }
            } else if (match1.matches()) {
                continue;
            } else if (match2.matches()) {
                if (checkIn(travel.substring(match2.start(), match2.end()))) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }
    
    private static ArrayList<String> splitTerm(String fin) {
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
    
    private static boolean indexCheck(String str) {
        Pattern p = Pattern.compile("\\^[+-]?\\d+");
        Matcher fd = p.matcher(str);
        while (fd.find()) {
            BigInteger x = new BigInteger(str.substring(fd.start() + 1, fd.end()));
            if (x.compareTo(new BigInteger("50")) > 0 || x.compareTo(new BigInteger("-50")) < 0) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean checkIn(String str) {
        String matchX  = "(x(\\^[+-]?[0-9]+)?)|([+-]?[0-9]+)";
        String match   = "(sin|cos)\\[\\S+\\](\\^[+-]?[0-9]+)?";
        Pattern p = Pattern.compile(matchX);
        Pattern q = Pattern.compile(match);
        Matcher match1 = p.matcher(str);
        Matcher match2 = q.matcher(str);
        if (match1.matches()) {
            return false;
        } else if (match2.matches()) {
            Pattern t = Pattern.compile("\\[\\S+\\]");
            Matcher match3 = t.matcher(str);
            if (match3.find()) {
                String now = str.substring(match3.start() + 1, match3.end() - 1);
                if (now.startsWith("(") && now.endsWith(")")) {
                    if (polyCheck(now.substring(1, now.length() - 1))) {
                        return true;
                    }
                } else {
                    if (checkIn(now)) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }
}