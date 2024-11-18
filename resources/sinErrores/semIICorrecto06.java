///exitosamente
//prueba de conformacion con tipos referencia y llamados a metodos

class A{
    C1 c1;
    C2 c2;
    C3 c3;

    C1 m4(C1 a1, C1 a2, C1 a3, C1 a4){
        return m4(c1, c2, c3, null);
    }

    static void main(){}
}

class C1 {}
class C2 extends C1{}
class C3 extends C2{}