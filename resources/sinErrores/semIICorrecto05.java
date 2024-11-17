///exitosamente
// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

class A {
    B a1;
    int a2;
    
} 

class B extends A{
    A a3;

    public B() {
        a1 = this;
        a3 = new A();
        a2 = 2;
    }
     void m1()
    {
        a1.a3.a2 = 4;
        System.printI(a1.a3.a2);
    }
}


class Init{
    static void main()
    {
        B b = new B();
        b.m1();
    }
}


