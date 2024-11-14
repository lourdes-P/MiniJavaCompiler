class A {
    C m4() {
        return new C();
    }
}

class B extends A {

}

class C extends B {
    A v1;

    public C() {
        v1 = new C();
    }

    A m2(A a) {
        String b = null;
        return a;
    }

    void m3() {
        v1 = m2(null);
    }
}

class Init {
    static void main() {
        A a1= new C();
        if (a1== null) {

        }
    }
}