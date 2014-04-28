package live.wallpaper;

public class Ticker {
    //like timer; calculate ticks

    private int limit;
    private int value;

    public boolean getIsNextRound() {
        return value==0;
    }

    public void tick() {
        value--;
        if (value<0) value=limit;
    }

    public Ticker(int limit) {
         this.limit=limit;
        value=0;
    }
}
