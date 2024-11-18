package utils;

public class Prueba {
    static int x = 1;

    public void m1() {
        System.out.println(x);
    }

    public static void m2(int i) {
        x = i;
    }

    public static void main(String[] args) {
        Prueba p = new Prueba(), p2 = new Prueba();
        p.m1();
        Prueba.m2(10);
        p2.m1();
        p.m1();
    }

}
