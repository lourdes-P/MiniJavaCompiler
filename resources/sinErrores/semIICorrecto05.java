// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

class A {
    B a4, a1 = null;
    int a2 = 2;


    public A(String w) {
        boolean x, y;
        x = true;
        y = false;
        int a, b;

        x = !true;

        a = 1;
        a = -3;
        a = +3;

        if (a < b) {
            ;
        }

        while (true) {

        }
        a1 = new B(this);
    }

    void m1(B p1) {
        a1.m2(2);
    }
} 

class B extends A{
    A a3;

    public B(A a) {
        a3 = a;
    }
    
     void m1(B p1)
    {
        a1.a3.a2 = 4;
    }

    A asd() {
        return new B(a3);
    }

    void m2(int p1)
    {
        var x = 1;
        String w;
        {
            {
                var y = 2;
            }
            var y = 3;

            x = 3 +1;

            w = "holas";

        }


    }
}


class Init{

    public Init() {

    }
    static void main()
    { }
}

class D {

    void m5(int i) {
        switch (i) {
            case 2: {
                break;
            }
            case 3: {
                new D();
                break;
            }
            default: {
                new A("hola");
                break;
            }
        }
    }
}


