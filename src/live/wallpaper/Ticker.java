package live.wallpaper;

public class Ticker {

    private float limit;
    private float value;
    private boolean can;

    public boolean getIsNextRound() {
        if (can) {
            can=false;
            return true;
        }
        else
            return false;
    }

    public void tick(float delta) {
        value-=delta;
        if (value<0) {
            value+=limit;
            can=true;
        }
    }

    public Ticker(float limit) {
        this.limit=limit;
        value=0;
        can=true;
    }
}
