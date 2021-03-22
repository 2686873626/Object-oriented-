import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String formula = in.nextLine();
        Pattern checkTerm = Pattern.compile("[\\+\\-]?\\s*(((([\\+\\-]?[0-9]+\\s*\\*\\s*)|" +
                "([\\+\\-]\\s*))?x(\\s*\\*\\*\\s*[\\+\\-]?[0-9]+)?)|([\\+\\-]?[0-9]+))");
        Matcher finder = checkTerm.matcher(formula);
        TermList list = new TermList();
        while (finder.find()) {
            String now = formula.substring(finder.start(), finder.end());
            list.addTerm(new Term(now));
        }
        list.firstPrint();
        for (BigInteger big: list.getMap()) {
            list.listPrint(big);
        }
        if (list.getMonitor() == 0) {
            System.out.print("0");
        }
    }
}
