// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

class A {
    B a1;
    int a2 = true;
   
    
    
    
} 

class B extends A{
    A a3;
    
     void m1(B p1)     
    {
        a1.a3.a2 = 4;
        
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

            x = 3;

            w = "holas";

        }


    }
}


class Init{
    static void main()
    { }
}


