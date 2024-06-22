import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Timer;
import java.util.TimerTask;

public class Obiadowanie {
    public static void main(String[] args) {
        int liczbaFilozof = 5;
        Filozof[] filozofs = new Filozof[liczbaFilozof];
        Lock[] widelce = new ReentrantLock[liczbaFilozof];
        SharedState sharedState = new SharedState(liczbaFilozof);
        StatusPanel statusPanel = new StatusPanel(liczbaFilozof, sharedState);

        JFrame frame = new JFrame("Obiadowanie Filozofów");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(statusPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        for (int i = 0; i < liczbaFilozof; i++) {
            widelce[i] = new ReentrantLock();
        }

        for (int i = 0; i < liczbaFilozof; i++) {
            Lock lWidelec = widelce[i];
            Lock pWidelec = widelce[(i + 1) % liczbaFilozof];
            filozofs[i] = new Filozof(i, lWidelec, pWidelec, sharedState);
            filozofs[i].start();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statusPanel.updateAllStatuses();
            }
        }, 0, 1000);

        for (int i = 0; i < liczbaFilozof; i++) {
            try {
                filozofs[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class StatusPanel extends JPanel {
    private final JLabel[] philosopherLabels;
    private final int liczbaFilozof;
    private final SharedState sharedState;

    public StatusPanel(int liczbaFilozof, SharedState sharedState) {
        this.liczbaFilozof = liczbaFilozof;
        this.sharedState = sharedState;
        this.philosopherLabels = new JLabel[liczbaFilozof];
        setLayout(new GridLayout(liczbaFilozof, 1));
        for (int i = 0; i < liczbaFilozof; i++) {
            philosopherLabels[i] = new JLabel();
            updateStatus(i, false, false, false, 0, 0);
            add(philosopherLabels[i]);
        }
    }

    public void updateAllStatuses() {
        boolean[] eating = sharedState.getEating();
        boolean[] hasLeftFork = sharedState.getHasLeftFork();
        boolean[] hasRightFork = sharedState.getHasRightFork();
        int[] eatCounts = sharedState.getEatCounts();
        int[] thinkCounts = sharedState.getThinkCounts();
        for (int i = 0; i < liczbaFilozof; i++) {
            updateStatus(i, eating[i], hasLeftFork[i], hasRightFork[i], eatCounts[i], thinkCounts[i]);
        }
    }

    public void updateStatus(int id, boolean isEating, boolean hasLeftFork, boolean hasRightFork, int eatCount, int thinkCount) {
        StringBuilder status = new StringBuilder("Filozof " + id + ": ");
        status.append(isEating ? "je" : "myśli");
        status.append(" | Lewy widelec: ").append(hasLeftFork ? "tak" : "nie");
        status.append(" | Prawy widelec: ").append(hasRightFork ? "tak" : "nie");
        status.append(" | Liczba jedzeń: ").append(eatCount);
        status.append(" | Liczba myśleń: ").append(thinkCount);
        philosopherLabels[id].setText(status.toString());
    }
}
