///exitosamente
//prueba de expresiones

class A{
    int a;
    boolean b;
    B c;

    void m1(){
        a = (5 + -3 * +2) / (a % 7);
        System.printIln(a);
        b = !b || (a >= 5) && (a != 3 || (new B() == c) && (new B() == new C()));
        System.printBln(b);
        b = c != null;
        System.printBln(b);
        c = (((new B()).b));
    }

    static void main(){
        A a = new A();
        a.a = 2;
        a.m1();
    }
}

class B{
    B b;
}

class C extends B{}