package optimal.execution;

import optimal.execution.events.EventType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
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
            consumer.addWriter(new ResultWriter(OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME, EventType.OPTIMAL_RESULT_READY, StandardOpenOption.APPEND));
        }
        return consumer;
    }

    public synchronized void addWriter(@NotNull ResultWriter writer) {
        myWriters.add(writer);
    }

    public void waitAndLogResults() throws InterruptedException {
        QueueEntry entity = queue.take();
        for (ResultWriter writer : myWriters) {
            writer.writeResultsForMyType(entity.resultEntity, entity.eventType);
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

    public synchronized void consumeResult(@NotNull ResultEntity e, @NotNull EventType type) {
        queue.add(new QueueEntry(e, type));
    }


    private static class QueueEntry {
        final ResultEntity resultEntity;
        final EventType eventType;

        public QueueEntry(@NotNull ResultEntity resultEntity, @NotNull EventType eventType) {
            this.resultEntity = resultEntity;
            this.eventType = eventType;
        }
    }
}
