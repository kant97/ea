package optimal.execution;

public class ProgressTracker {
    public synchronized void updateProgress() {
        System.out.print(".");
    }
}
