package uet.oop.bomberman.info;

public class Timer {
    private static final int DEFAULT_TIMER = 5;
    private long timeValue;
    private long timeStart;

    public Timer() {
        timeStart = System.currentTimeMillis();
        timeValue = DEFAULT_TIMER;
    }

    public void update() {
        if (timeValue != 0) {
            timeValue = DEFAULT_TIMER - (System.currentTimeMillis() - timeStart) / 1000;
        }
    }

    public long getTimeValue() {
        return timeValue;
    }
}
