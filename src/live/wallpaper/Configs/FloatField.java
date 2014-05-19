package live.wallpaper.Configs;

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
}
