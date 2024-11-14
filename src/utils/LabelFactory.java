package utils;

public class LabelFactory {

    // type = attr, met, ctor
    // los atributos y métodos con labels son estáticos
    public static String createLabel(String type, String name, String containerClass) {
        return type + name + "@" + containerClass;
    }

    // type = VT
    public static String createLabel(String type, String containerClass) {
        return type + containerClass;
    }
}
