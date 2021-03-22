public class Polynomial {
    public static void main(String[] args) {
        Poly poly = GetPoly.polyRead();//Read and preprocess include some modify
        if (poly.getWrong()) {
            System.out.println("WRONG FORMAT!");
        } else {
            poly.differential();
            poly.print();
        }
    }
    
}
