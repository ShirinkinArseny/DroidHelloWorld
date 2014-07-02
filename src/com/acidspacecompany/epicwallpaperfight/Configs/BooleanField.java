package com.acidspacecompany.epicwallpaperfight.Configs;

public class BooleanField extends ConfigField{

    private boolean value;

    public Type getType() {
        return Type.Boolean;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        try {
            this.value = (boolean) value;
        }
        catch (Exception e) {
            this.value = Boolean.valueOf(value.toString());
        }
    }

    public BooleanField(String name) {
        super(name);
    }

    public ConfigField copy() {
        BooleanField copy=new BooleanField(super.getName());
        copy.value=value;
        return copy;
    }
}
