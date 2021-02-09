package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OneMaxNeutral3 implements Problem {
    private boolean[] individual;
    private int fitness;
    private boolean[] chunks;
    private int[] chunksCount;
    private int[] chunksCountByOnesNumber;
    private int tail;

    public OneMaxNeutral3(int n) {
        individual = new boolean[n];
        Random rand = new Random();
        tail = n % 3;
        chunks = new boolean[n / 3 + (tail == 0 ? 0 : 1)];
        chunksCount = new int[n / 3 + (tail == 0 ? 0 : 1)];
        chunksCountByOnesNumber = new int[4];
        fitness = 0;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
        }

        int count = 0;
        int curChunk = 0;
        for (int i = 0; i < n - tail; i += 3) {
            for (int j = 0; j < 3; ++j) {
                if (individual[i + j]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            chunksCountByOnesNumber[count]++;
            if (count >= 2) {
                chunks[curChunk] = true;
                fitness++;
            }
            curChunk++;
            count = 0;
        }
        if (n % 3 != 0) {
            for (int i = n - tail; i < n; ++i) {
                if (individual[i]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            chunksCountByOnesNumber[count]++;
            if (count > tail / 2) {
                chunks[curChunk] = true;
                fitness++;
            }
        }
    }

    public OneMaxNeutral3(int n, int fitness) {
        this(n);

        Arrays.fill(individual, false);
        Arrays.fill(chunks, false);
        Arrays.fill(chunksCount, 0);
        Arrays.fill(chunksCountByOnesNumber, 0);

        ArrayList<Integer> toKeep = new ArrayList<>(chunks.length);
        for (int i = 0; i < chunks.length; i++) {
            toKeep.add(i);
        }

        ArrayList<Integer> toTransorm = new ArrayList<>(fitness);
        Random random = new Random();
        for (int i = 0; i < fitness; i++) {
            int r = random.nextInt(toKeep.size());
            int chunkToTransformId = toKeep.get(r);
            toKeep.remove(r);
            toTransorm.add(chunkToTransformId);
        }

        boolean lastToKeep = false;

        for (int id : toTransorm) {
            if (id == chunks.length - 1) {
                continue;
            }
            for (int i = id * 3; i < id * 3 + 3; i++) {
                individual[i] = true;
            }
            int keep = getBitToKeep(random);
            int count = 3;
            if (keep != -1) {
                individual[id * 3 + keep] = false;
                count = 2;
            }
            chunks[id] = true;
            chunksCount[id] = count;
            chunksCountByOnesNumber[count]++;
        }

        for (int id : toKeep) {
            if (id == chunks.length - 1) {
                lastToKeep = true;
                continue;
            }
            int reverse = getBitToKeep(random);
            int count = 0;
            if (reverse != -1) {
                individual[id * 3 + reverse] = true;
                count = 1;
            }
            chunks[id] = false;
            chunksCount[id] = count;
            chunksCountByOnesNumber[count]++;
        }

        if (!lastToKeep) {
            int lastChunkId = chunks.length - 1;
            for (int i = lastChunkId * 3; i < individual.length; i++) {
                individual[i] = true;
            }
            chunks[lastChunkId] = true;
            chunksCount[lastChunkId] = tail;
            chunksCountByOnesNumber[tail]++;
        } else {
            chunksCountByOnesNumber[0]++;
        }

        this.fitness = fitness;

    }

    public int getBitToKeep(Random random) {
        int b = random.nextInt(2);
        if (b == 1) {
            return random.nextInt(3);
        }
        return -1;
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = fitness;
        int curChunk = -1;
        int curChunkCount = -1;
        for (Integer i : patch) {
            if (curChunk != i / 3) { // if a new item from the patch does not belong to the previous chunk
                //apply result for the previous chunk
                if (curChunk >= 0) { //not start
                    if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                        if (chunks[curChunk]) {
                            newFitness--;
                        }
                    } else {
                        if (!chunks[curChunk]) {
                            newFitness++;
                        }
                    }
                }
                //go to the next chunk
                curChunk = i / 3;
                curChunkCount = chunksCount[curChunk];
            }
            //count ones in the current chunk
            if (individual[i]) {
                curChunkCount--;
            } else {
                curChunkCount++;
            }
        }

        if (curChunk >= 0) { //apply last patch item
            if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) {
                if (chunks[curChunk]) {
                    newFitness--;
                }
            } else {
                if (!chunks[curChunk]) {
                    newFitness++;
                }
            }
        }

        return newFitness;
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        int curChunk = -1;
        int curChunkCount = -1;
        for (Integer i : patch) {
            if (curChunk != i / 3) { // if a new item from the patch does not belong to the previous chunk
                //apply result for the previous chunk
                if (curChunk >= 0) { //not start
                    if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                        chunks[curChunk] = false;
                    } else {
                        chunks[curChunk] = true;
                    }
                    chunksCountByOnesNumber[chunksCount[curChunk]]--;
                    chunksCount[curChunk] = curChunkCount;
                    chunksCountByOnesNumber[curChunkCount]++;
                }
                //go to the next chunk
                curChunk = i / 3;
                curChunkCount = chunksCount[curChunk];
            }
            //count ones in the current chunk
            if (individual[i]) {
                curChunkCount--;
            } else {
                curChunkCount++;
            }
            individual[i] = !individual[i];
        }

        if (curChunk >= 0) { //apply last patch item
            if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                chunks[curChunk] = false;
            } else {
                chunks[curChunk] = true;
            }
            chunksCountByOnesNumber[chunksCount[curChunk]]--;
            chunksCount[curChunk] = curChunkCount;
            chunksCountByOnesNumber[curChunkCount]++;
        }
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getOnesCount(int fitness) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getLength() {
        return individual.length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == chunks.length;
    }

    @Override
    public String getInfo() {
        StringBuilder s = new StringBuilder();
        for (int i : chunksCountByOnesNumber) {
            s.append(", ").append(i);
        }
        return s.toString();
    }

    @Override
    public int getOptimum() {
        return 0;
    }
}
