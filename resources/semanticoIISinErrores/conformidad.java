class A {

}

class B extends A {

}

class C extends B {
    A v1;

    public C() {
        v1 = new C();
    }
}

class Init {
    static void main() {

    }
}
