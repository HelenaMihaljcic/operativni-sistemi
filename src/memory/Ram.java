package memory;

import java.util.HashMap;
import java.util.Map;

public class Ram {

    private static final int numOfFrames = 128;

    private static int[] frames = new int[numOfFrames];
    private static Map<Integer,Partition> memory = new HashMap<>();

    public static int[] getFrames() {
        return frames;
    }

    public static int getNumOfFrames(){
        return numOfFrames;
    }

    public static Map<Integer, Partition> getMemory() {
        return memory;
    }

    public static void allocateFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < numOfFrames) {
            frames[frameIndex] = 1; // 1 indicates the frame is allocated
        }
    }

    public static void deallocateFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < numOfFrames) {
            frames[frameIndex] = 0; // 0 indicates the frame is deallocated
        }
    }

    public static void setFrameState(int index, int state) {
        if (index >= 0 && index < frames.length) {
            frames[index] = state;
        } else {
            // handle invalid index, e.g., throw an exception
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    public static void addPartition(int partitionId, Partition partition) {
        memory.put(partitionId, partition);
    }

    public static void removePartition(int partitionId) {
        memory.remove(partitionId);
    }

    public static Partition getPartition(int i) {
        return memory.get(i);
    }

}
