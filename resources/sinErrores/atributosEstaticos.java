///1&2&3&exitosamente

class A {
    static int c1;
    static int c2;
    static int c3;


    static int m() {
        return c1;
    }

    static void main(){
        A a= new A();
        c1 = 1;
        c2 = 2;
        c3 = 3;
        System.printIln(a.m());
        System.printIln(a.c2);
        System.printIln(a.c3);
    }
}

class C1{
    int x;
    void m3() {
        debugPrint(12);
    }

    void m1() {

    }
}
class C2 extends C1 {
    String x;

    void m3() {
        debugPrint(12345);
    }

    void m2() {

    }

    void m1() {

    }
}
class C3 extends C2 {
    char x;

    void m3() {
        debugPrint(123456789);
    }

    void m4() {

    }

    void m1() {

    }

    void m2() {

    }
}