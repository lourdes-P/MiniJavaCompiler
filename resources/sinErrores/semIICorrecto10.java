///exitosamente
//prueba asignacion

class A{
    int a1;
    A a2;
    B a3;

    A m1(){
        a1 = 7 * 3 / a1;
        System.printIln(a1);
        a1 -= 4 % 3 + a1;
        System.printIln(a1);
        new A().a1 = 3;
        return new A();
    }

    static void main(){
        A a = new A();
        a.a1 = 2;
        a.a2 = a;
        a.a3 = new B();
        a.m1();
    }
}

class B extends A{}