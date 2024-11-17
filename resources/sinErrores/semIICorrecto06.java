///1234&1234&1234&exitosamente
//prueba de conformacion con tipos referencia y llamados a metodos

class A{
    C1 c1;
    C2 c2;
    C3 c3;

    public A() {
        c1 = new C1();
        c2 = new C2();
        c3 = new C3();
    }

    static void m4(C1 a1, C1 a2, C1 a3, C1 a4){
        a1.m3();

    }

    static void main(){
        A a = new A();
        m4(a.c1, a.c2, a.c3, null);
    }
}

class C1 {

    void m3() {
        debugPrint(1234);
    }
}
class C2 extends C1 {
    void m3() {
        debugPrint(123456789);
    }
}
class C3 extends C2{}