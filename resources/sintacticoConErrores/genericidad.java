///[Error:>|8]

abstract class Genericidad<String,String,Clase<K,V>> {

    Genrico<String> variable;
    Generico<String,String> variable2;
    Generico<String,String,Clase<K,V>> variable2;
    Gen<Genrico<String>> malo;

    Generico<String,String,Clase<K,V>> metodo() {
        new Gen<>();
    }

    abstract Generico<String,String> metodo2();

}