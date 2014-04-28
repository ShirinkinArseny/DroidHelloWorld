package live.wallpaper;

public class Ticker {
    //like timer; calculate ticks

    private int limit;
    private int value;
    private boolean can;

    public boolean getIsNextRound() {
        if (can) {
            can=false;
            return true;
        }
        else
            return false;
    }

    public void tick() {
        value--;
        if (value<0) {
            value=limit;
            can=true;
        }
    }

    public Ticker(int limit) {
        this.limit=limit;
        value=0;
        can=false;
    }
}
