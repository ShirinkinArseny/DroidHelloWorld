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
         this.value=(float)value;
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
