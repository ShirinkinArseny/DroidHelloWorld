package com.acidspacecompany.epicwallpaperfight.Configs;

public class IntegerField extends ConfigField{

    private int value;

    public Type getType() {
        return Type.Integer;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        try {
            this.value = (int) value;
        }
        catch (Exception e) {
            this.value = Integer.valueOf(value.toString());
        }
    }

    public IntegerField(String name) {
        super(name);
    }

    public ConfigField copy() {
        IntegerField copy=new IntegerField(super.getName());
        copy.value=value;
        return copy;
    }
}
