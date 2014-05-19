package live.wallpaper.Configs;

public class StringField extends ConfigField{

    private String value;

    public Type getType() {
        return Type.Float;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
         this.value=(String)value;
    }

    public StringField(String name) {
        super(name);
    }
}
