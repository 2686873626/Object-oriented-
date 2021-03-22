import java.util.Scanner;

public class GetPoly {
    public static Poly polyRead() {
        Scanner in = new Scanner(System.in);
        String formula = in.nextLine();
        formula = rmSpace(formula);
        return new Poly(formula);
    }
    
    private static String rmSpace(String line) { //remove the space&\t
        String formula = line.replace(" ","");
        formula = formula.replace("\t","");
        return formula;
    }
    
    public static String dealStart(String str) {
        int symbol = 1;
        String modify = str;
        while (modify.startsWith("+") || modify.startsWith("-")) {
            if (modify.startsWith("-")) {
                symbol *= -1;
            }
            modify = modify.substring(1);
        }
        if (symbol == -1) {
            return "-1*" + modify;
        } else {
            return modify;
        }
    }
}
