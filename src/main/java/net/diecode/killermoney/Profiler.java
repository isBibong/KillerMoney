package net.diecode.killermoney;

public class Profiler {

    private String title;
    private long startTime;

    public Profiler(String title) {
        this.title = title;
        this.startTime = System.currentTimeMillis();
    }

    public String getResult() {
        long endTime = System.currentTimeMillis();

        return title + ": " + (endTime - startTime) + " ms";
    }

}
