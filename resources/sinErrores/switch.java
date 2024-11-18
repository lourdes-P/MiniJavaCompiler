///exitosamente

class A{


    void m1(int i){
        switch (i) {
            case 1 :
                break;
            case 2:
            default :
                System.printIln(10);
            default:
                System.printIln(9);
            case 3:
                System.printIln(i);
        }
    }

    static void main(){
        A a = new A();
        a.m1(3);
    }
}