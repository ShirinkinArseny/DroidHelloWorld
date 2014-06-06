package com.acidspacecompany.epicwallpaperfight.Configs;

public abstract class ConfigField {

    public enum Type {Float, Integer, String, Boolean}
    private final String name;

    public abstract Type getType();

    public abstract Object getValue();

    public abstract void setValue(Object value);

    public String getName() {
        return name;
    }

    public ConfigField(String name) {
        this.name=name;
    }

    public abstract ConfigField copy();
}
