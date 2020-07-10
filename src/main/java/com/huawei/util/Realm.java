package com.huawei.util;

import java.util.stream.Stream;

public enum Realm {
    COLLECTOR("entidades"),
    SYSTEM("system"),
    OPERATOR("backoffice"),
    PAYER("pagadores"),
    CARRIER("carrier");

    private final String name;

    Realm(String name) {
        this.name = name;
    }

    /**
     * Retorna el name especificado para este enum.
     *
     * @return nombre del enum
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retorna enum {@link Realm} con el realm especificado. El nombre
     * debe coincidir exactamente con un realm declarado en el enum en este
     * tipo. (Espacios en blanco extraños los caracteres no están permitidos.)
     *
     * @param name el realm de la constante a retornar
     * @return enum del tipo de {@link Realm} especificado con el realm especificado
     */
    public static Realm fromName(String name) {
        for (Realm type : Realm.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

    public static final Boolean in(String value, Realm... realms) {
        Realm realm = Realm.fromName(value);
        return Stream.of(realms).anyMatch(s -> s.equals(realm));
    }
}
