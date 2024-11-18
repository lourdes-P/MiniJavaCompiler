///12&12345&123456789&exitosamente
// TODO ver offsets metodos
class A{
    C1 c1;
    C2 c2;
    C3 c3;

    public A() {
        c1 = new C1();
        c2 = new C2();
        c3 = new C3();
    }

    C1 m4a(){
        return c1;
    }
    C1 m4b(){
        return c2;
    }
    C1 m4c(){
        return c3;
    }
    C2 m4d() {
        return c3;
    }
    static void main(){
        A a= new A();
        a.m4a().m3();
        a.m4b().m3();
        a.m4c().m3();
        a.m4d().m3();
    }
}

class C1{
    void m3() {
        debugPrint(12);
    }

    void m1() {

    }
}
class C2 extends C1 {

    void m3() {
        debugPrint(12345);
    }

    void m2() {

    }

    void m1() {

    }
}
class C3 extends C2 {
    void m3() {
        debugPrint(123456789);
    }

    void m4() {

    }

    void m1() {

    }

    void m2() {

    }
}