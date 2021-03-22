public class Polynomial {
    public static void main(String[] args) {
        Poly poly = GetPoly.polyRead();
        if (poly.getWrFmt()) {
            System.out.println("WRONG FORMAT!");
        } else {
            poly.generate();
            String out = poly.differential();
            printOut(out);
        }
    }
    
    private static void printOut(String str) {
        String fin = str;
        for (int i = 0; i < fin.length(); i++) {
            char now = fin.charAt(i);
            if (now == '[') {
                fin = fin.substring(0, i) + '(' + fin.substring(i + 1);
            } else if (now == ']') {
                if (i == fin.length() - 1) {
                    fin = fin.substring(0, i) + ')';
                } else {
                    fin = fin.substring(0, i) + ')' + fin.substring(i + 1);
                }
            } else if (now == '^') {
                fin = fin.substring(0, i) + "**" + fin.substring(i + 1);
            }
        }
        System.out.println(fin);
    }
}
