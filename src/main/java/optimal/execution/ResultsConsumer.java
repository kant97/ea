package optimal.execution;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ResultsConsumer implements Runnable {
    public static final String OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME = "results.csv";
    public static final String ALL_MUTATION_RATES_RESULTS_FILE_NAME = "allIntermediateResults.csv";

    protected final LinkedBlockingQueue<QueueEntry> queue = new LinkedBlockingQueue<>();
    private final ArrayList<ResultWriter> myWriters = new ArrayList<>();

    private ResultsConsumer() {
    }

    public static ResultsConsumer createResultsConsumer(boolean addDefaultWriter) throws IOException {
        ResultsConsumer consumer = new ResultsConsumer();
        if (addDefaultWriter) {
            // default writer
            consumer.addWriter(new ResultWriter(OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME, ResultType.OPTIMAL));
        }
        return consumer;
    }

    public synchronized void addWriter(@NotNull ResultWriter writer) {
        myWriters.add(writer);
    }

    public void waitAndLogResults() throws InterruptedException {
        QueueEntry entity = queue.take();
        for (ResultWriter writer : myWriters) {
            writer.writeResultsForMyType(entity.resultEntity, entity.resultType);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitAndLogResults();
            } catch (InterruptedException e) {
                break;
            }
        }
        try {
            for (ResultWriter writer : myWriters) {
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void consumeResult(@NotNull ResultEntity e, @NotNull ResultType type) {
        queue.add(new QueueEntry(e, type));
    }


    public enum ResultType {
        OPTIMAL {
            @Override
            public String toString() {
                return "OptimalResult";
            }
        }, INTERMEDIATE {
            @Override
            public String toString() {
                return "IntermediateResult";
            }
        }
    }

    private static class QueueEntry {
        final ResultEntity resultEntity;
        final ResultType resultType;

        public QueueEntry(@NotNull ResultEntity resultEntity, @NotNull ResultType resultType) {
            this.resultEntity = resultEntity;
            this.resultType = resultType;
        }
    }
}
