import java.util.concurrent.locks.Lock;

class Filozof extends Thread {
    private final int id;
    private final Lock lWidelec;
    private final Lock pWidelec;
    private final SharedState sharedState;

    public Filozof(int id, Lock lWidelec, Lock pWidelec, SharedState sharedState) {
        this.id = id;
        this.lWidelec = lWidelec;
        this.pWidelec = pWidelec;
        this.sharedState = sharedState;
    }

    private void myslenie() throws InterruptedException {
        sharedState.incrementThinkCount(id);
        sharedState.updatePhilosopher(id, false, false, false, sharedState.getEatCount(id), sharedState.getThinkCount(id));
        Thread.sleep(1000);  // Think for 1 second
    }

    private void jedzenie() throws InterruptedException {
        sharedState.incrementEatCount(id);
        sharedState.updatePhilosopher(id, true, false, false, sharedState.getEatCount(id), sharedState.getThinkCount(id));
        Thread.sleep(1000);  // Eat for 1 second
    }

    @Override
    public void run() {
        try {
            while (true) {
                myslenie();
                if (lWidelec.tryLock()) {
                    try {
                        sharedState.updatePhilosopher(id, false, true, false, sharedState.getEatCount(id), sharedState.getThinkCount(id));
                        if (pWidelec.tryLock()) {
                            try {
                                sharedState.updatePhilosopher(id, false, true, true, sharedState.getEatCount(id), sharedState.getThinkCount(id));
                                jedzenie();
                            } finally {
                                sharedState.updatePhilosopher(id, false, true, false, sharedState.getEatCount(id), sharedState.getThinkCount(id));
                                pWidelec.unlock();
                            }
                        }
                    } finally {
                        sharedState.updatePhilosopher(id, false, false, false, sharedState.getEatCount(id), sharedState.getThinkCount(id));
                        lWidelec.unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
