///[Error:b|22]
// El lado izquierdo de la asignación es no asignable - ln: 9
class A {
    int a1;
    
    static void m1(int p1)
    
    {
       A a = new B();
    }
    
     void m2()
    {}
         
    

}


class B extends A{

    static void m10() {
        B b = new A();
    }
}


class Init{
    static void main()
    { }
}


