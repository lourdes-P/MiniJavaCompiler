///exitosamente
//prueba de encadenados y llamados a metodos

class A{
    B b;

    public A() {
        b = new B();
    }

    B mb(){return new B();}

    void m1(A pa){
        new A().b.ma().mb().mc().string();
//        new A().aa.mb();
//        mb().ab.ma().aa.mb();
//        (A.mb2()).ab.ma().aa.mb();
//        this.aa.mb().ab.ma().mb();
//        aa.aa.mb().ab.ma().mb();
//        pa.aa.mb().ab.ma().mb();
//        var va = new A();
//        va.aa.mb().ab.ma().mb();
    }

    static B mb2(){return new B();}

    static void main(){
        A a = new A();
        a.m1(a);
    }
}

class B{
    C c;

    public B() {
        c = new C();
    }

    A ma(){return new A();}

    C mc() {
        return c;
    }
}

class C {

    C mc() {
        return new C();
    }

    void string() {
        System.printSln("string");
    }
}