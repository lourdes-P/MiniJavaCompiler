
abstract class Genericidad<String<K>,String,Clase<K,V>> {

    Genrico<String> variable;
    Generico<String<K>,String> variable2;
    Generico<String,String,Clase<K,V>> variable2;



    Generico<String<K>,String,Clase<K,V>> metodo() {
        new Tas<String,String>();
        new Tas<String,Cas<String,K>>();
        new Tas<>();
        new Tas<Clase<K,V>,Tas>();
    }

    abstract Generico<String,String> metodo2();


}