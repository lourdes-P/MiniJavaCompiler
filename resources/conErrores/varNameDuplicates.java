///[Error:x|6]

class A {
    void m2(){
        var x = "hola";
        int x;
        {
            var x = 1;
        }
    }

}

class B extends A {

}

class C extends B {


    static C m4() {
        return new C();
    }
}

class Init {
    static void main() {
    }

    void asd() {
        C acc  = new C();
    }
}