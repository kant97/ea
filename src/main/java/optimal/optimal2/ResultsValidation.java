package optimal.optimal2;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResultsValidation {
    private final BufferedWriter writer;
    private final static String[] ENTRIES = {"mutationProbability98", "mutationProbability99", "p(98->98)", "p(98->99)", "p(99->98)", "p(99->99)", "T98", "T99"};

    public ResultsValidation(Path outputPath) throws IOException {
        this.writer = Files.newBufferedWriter(outputPath);
        for (int i = 0; i < ENTRIES.length - 1; i++) {
            writer.write(ENTRIES[i]);
            writer.write(',');
        }
        writer.write(ENTRIES[ENTRIES.length - 1]);
        writer.write('\n');
        writer.flush();
    }

    private void logResults(double r98, double r99, double p11, double p12, double p21, double p22, double T1, double T2) {
        try {
            writer.write(Double.toString(r98));
            writer.write(',');
            writer.write(Double.toString(r99));
            writer.write(',');
            writer.write(Double.toString(p11));
            writer.write(',');
            writer.write(Double.toString(p12));
            writer.write(',');
            writer.write(Double.toString(p21));
            writer.write(',');
            writer.write(Double.toString(p22));
            writer.write(',');
            writer.write(Double.toString(T1));
            writer.write(',');
            writer.write(Double.toString(T2));
            writer.write('\n');
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void closeWriter() throws IOException {
        writer.close();
    }

    private static double fastPow(double a, int power) {
        double ans = 1.;
        while (power > 0) {
            if (power % 2 > 0) {
                ans = ans * a;
            }
            a = a * a;
            power = power / 2;
        }
        return ans;
    }

    private double[] probsFromHemming98(int n, double r) {
        double p98 = 2. * (n - 2) * fastPow(1 - r, n - 2) * r * r + (n - 3.) * (n - 2) / 2. * fastPow(1 - r, n - 4) * fastPow(r, 4);
        double p99 = 2. * (fastPow(1 - r, n - 1) * r + fastPow(1 - r, n) / (double) n) + (n - 2.) * fastPow(1 - r, n - 3) * r * r * r;
        double p100 = fastPow(1. - r, n - 2) * r * r;
        double p0 = 1 - p98 - p99 - p100;
        return new double[]{p0, p98, p99, p100};
    }

    private double[] probsFromHemming99(int n, double r) {
        double p98 = (n - 1.) * (fastPow(1. - r, n - 1) * r + fastPow(1 - r, n) / n) + (n - 2.) * (n - 1.) / 2. * fastPow(1 - r, n - 3) * r * r * r;
        double p99 = (n - 1) * fastPow(1 - r, n - 2) * r * r;
        double p100 = fastPow(1 - r, n - 1) * r + fastPow(1 - r, n) / n;
        double p0 = 1. - p98 - p99 - p100;
        return new double[]{p0, p98, p99, p100};
    }

    private double[] processTransitions(double[] transitionsFor1, int lambda) {
        double[] transitions = new double[4];
        transitions[0] = fastPow(transitionsFor1[0], lambda);
        transitions[3] = 1. - fastPow(1. - transitionsFor1[3], lambda);
        double toUs = 1. - transitions[0] - transitions[3];
        transitions[1] = transitionsFor1[1] / toUs;
        transitions[2] = transitionsFor1[2] / toUs;
        if (toUs > 0.) {
            double sum = transitions[0] + transitions[1] + transitions[2] + transitions[3];
            for (int i = 0; i < transitions.length; i++) {
                transitions[i] = transitions[i] / sum;
            }
        }
        return transitions;
    }

    public Pair<Double, Double> computeWithMutationRates(int n, int lambda, double r98, double r99) {
        double[] from98Lambda = processTransitions(probsFromHemming98(n, r98), lambda);
        double[] from99Lambda = processTransitions(probsFromHemming99(n, r99), lambda);
        double p11 = from98Lambda[0] + from98Lambda[1];
        double p12 = from98Lambda[2];
        double p21 = from99Lambda[1];
        double p22 = from99Lambda[0] + from99Lambda[2];
        double det = (p11 - 1.) * (p22 - 1.) - p12 * p21;
        final double T1, T2;
        if (Double.POSITIVE_INFINITY == p11 + p12 + p21 + p22 || det == 0) {
            if (Double.POSITIVE_INFINITY == p11 + p12 + p21 + p22 || p11 + p12 + p21 + p22 == 2. || (p11 == 1. && p21 > 0.) || (p22 == 1. && p12 > 0.)) {
                T1 = Double.POSITIVE_INFINITY;
                T2 = Double.POSITIVE_INFINITY;
            } else if (p11 < 1. && p12 == 0.) {
                T1 = 1. / (1. - p11);
                T2 = Double.POSITIVE_INFINITY;
            } else if (p22 < 1. && p21 == 0.) {
                T1 = Double.POSITIVE_INFINITY;
                T2 = 1 / (1. - p22);
            } else {
                T1 = Double.POSITIVE_INFINITY;
                T2 = Double.POSITIVE_INFINITY;
            }
        } else {
            T1 = (p12 - p22 + 1.) / det;
            T2 = (p21 - p11 + 1.) / det;
        }
        logResults(r98, r99, p11, p12, p21, p22, T1, T2);
        return new Pair<>(T1, T2);
    }

    public static void main(String[] args) throws IOException {
        final ResultsValidation resultsValidation = new ResultsValidation(Paths.get("honestPlateauk2.csv"));
        int n = 100, lambda = 1;
        double step = 0.0001;
        double minT1 = Double.POSITIVE_INFINITY, minT2 = Double.POSITIVE_INFINITY;
        double argminT1 = 0, argminT2 = 0;
        for (double r98 = step; r98 < 0.2; r98 += step) {
//            for (double r99 = step; r99 < 0.2; r99 += step) {
            double r99 = r98;
                final Pair<Double, Double> T98T99 = resultsValidation.computeWithMutationRates(n, lambda, r98, r99);
                double T1 = T98T99.getKey();
                double T2 = T98T99.getValue();
//                if (T1 < minT1 && T2 < minT2) {
//                    minT1 = T1;
//                    minT2 = T2;
//                    argminT1 = r98;
//                    argminT2 = r99;
//                }
                if (T1 < minT1) {
                    minT1 = T1;
                    argminT1 = r98;
                }
                if (T2 < minT2) {
                    minT2 = T2;
                    argminT2 = r99;
                }
//            }
        }
        resultsValidation.closeWriter();
        System.out.println("min T98 = " + minT1 + ", argmin T98 = " + argminT1 + " \nmin T99 = " + minT2 + ", argmin T99 = " + argminT2);
    }
}
