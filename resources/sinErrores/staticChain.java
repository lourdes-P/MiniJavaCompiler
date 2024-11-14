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

}

class Init {
    static void main() {
        A a1= new C();

        a1.m4();

    }

    C m4() {
      return new C();
    }
}