///[Error:a2|3]
class A {
    int a1 = a2;
    int a2 = 1;
    A a = this;

    String a3 = "hola";

    void m2(){

    }

}

class B extends A {
    static int a5 = 3;

    static String m3(){
        return "hola";
    }
}

class Init {
    static void main() {
    }
}