package live.wallpaper.Configs;

public class IntegerField extends ConfigField{

    private int value;

    public Type getType() {
        return Type.Integer;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
         this.value=(int)value;
    }

    public IntegerField(String name) {
        super(name);
    }
}
