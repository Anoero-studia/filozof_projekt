import java.util.Arrays;

public class SharedState {
    private final boolean[] eating;
    private final boolean[] hasLeftFork;
    private final boolean[] hasRightFork;
    private final int[] eatCounts;
    private final int[] thinkCounts;

    public SharedState(int numberOfPhilosophers) {
        this.eating = new boolean[numberOfPhilosophers];
        this.hasLeftFork = new boolean[numberOfPhilosophers];
        this.hasRightFork = new boolean[numberOfPhilosophers];
        this.eatCounts = new int[numberOfPhilosophers];
        this.thinkCounts = new int[numberOfPhilosophers];
    }

    public synchronized void updatePhilosopher(int id, boolean isEating, boolean hasLeftFork, boolean hasRightFork, int eatCount, int thinkCount) {
        this.eating[id] = isEating;
        this.hasLeftFork[id] = hasLeftFork;
        this.hasRightFork[id] = hasRightFork;
        this.eatCounts[id] = eatCount;
        this.thinkCounts[id] = thinkCount;
    }

    public synchronized boolean isEating(int id) {
        return eating[id];
    }

    public synchronized boolean hasLeftFork(int id) {
        return hasLeftFork[id];
    }

    public synchronized boolean hasRightFork(int id) {
        return hasRightFork[id];
    }

    public synchronized int getEatCount(int id) {
        return eatCounts[id];
    }

    public synchronized int getThinkCount(int id) {
        return thinkCounts[id];
    }

    public synchronized boolean[] getEating() {
        return Arrays.copyOf(eating, eating.length);
    }

    public synchronized boolean[] getHasLeftFork() {
        return Arrays.copyOf(hasLeftFork, hasLeftFork.length);
    }

    public synchronized boolean[] getHasRightFork() {
        return Arrays.copyOf(hasRightFork, hasRightFork.length);
    }

    public synchronized int[] getEatCounts() {
        return Arrays.copyOf(eatCounts, eatCounts.length);
    }

    public synchronized int[] getThinkCounts() {
        return Arrays.copyOf(thinkCounts, thinkCounts.length);
    }

    public synchronized void incrementEatCount(int id) {
        this.eatCounts[id]++;
    }

    public synchronized void incrementThinkCount(int id) {
        this.thinkCounts[id]++;
    }
}
