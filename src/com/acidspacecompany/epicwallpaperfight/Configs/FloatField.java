package com.acidspacecompany.epicwallpaperfight.Configs;

public class FloatField extends ConfigField{

    private float value;

    public Type getType() {
        return Type.Float;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        try {
            this.value = (float) value;
        }
        catch (Exception e) {
            this.value = Float.valueOf(value.toString());
        }
    }

    public FloatField(String name) {
        super(name);
    }

    public ConfigField copy() {
        FloatField copy=new FloatField(super.getName());
        copy.value=value;
        return copy;
    }
}
