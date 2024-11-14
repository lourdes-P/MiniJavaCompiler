///[Error:v1|25]

class A {

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
        A aaa = C.m4().v1;
        C.m4().v1;
    }
}