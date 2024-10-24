///[SinErrores]
// Prueba encabezados de metodos redefinicion valida e implementacion de interface
class A {
    Init main;
    void m3(A p1, B p2)
    {}  
}
class B extends A {
    void m3(A p1, B p2)
    {
        A blink, arr, as;
        A.m3(new A(), this);
        blink.m3(new A(), this);
    }
}



class Init{
    static void main()
    { }
}




