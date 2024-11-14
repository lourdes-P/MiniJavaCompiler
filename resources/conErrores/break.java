///[Error:break|13]

class A {

    void m1() {
        B a1;
        int as;
        switch(as){
            case 0:
                break;
        }
        {
            break;

        }

        while (true) {
            if (as== 1)
                break;
        }
    }

}

class B extends A {

    void m2 () {

    }
}

class Init {
    static void main() {
    }
}