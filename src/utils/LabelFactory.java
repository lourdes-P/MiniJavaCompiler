package utils;

public class LabelFactory {
    private static int i = 0;

    // type = attr, met, ctor
    // los atributos y métodos con acceso con labels son estáticos
    // todos tienen labels igual (generacion de codigo y guardado en VT)
    public static String createLabel(String type, String name, String containerClass) {
        return type + name + "@" + containerClass;
    }

    // type = VT
    public static String createVTLabel(String type, String containerClass) {
        return type + containerClass;
    }

    // para uso de instrucciones de flujo de control (If, While, Switch, break?, llamadas)
    // Sera el nodo correspondiente quien cree y guarde la etiqueta para uso en la
    // generacion de codigo.
    public static String createNewLabel() {
        return "lbl" + i++;
    }
}
