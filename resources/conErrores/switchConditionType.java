///[Error:switch|7]

class A {

    void m1() {
        B a1;
        switch(a1){}
    }

}

class B extends A {

}

class Init {
    static void main() {
    }
}