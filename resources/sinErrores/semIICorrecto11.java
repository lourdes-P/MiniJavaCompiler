///exitosamente
//prueba declaraciones de variables

class A{
   String a;
    A m1(){
        var a = 3 * 7 + 1;
        var b = true || false && !false;
        var c = "asd";
        var d = this.a;
        var g = (1 >= 3);
        return new A();
    }

    static void main(){
        A a = new A();
        a.m1();
    }
}

class B extends A{}