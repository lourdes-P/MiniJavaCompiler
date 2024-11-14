///[Error:x|6]

class A {
    void m1(int p1)
    {
        String x = m2();
    }
    void m2(){

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