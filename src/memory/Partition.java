package memory;

import java.util.ArrayList;

public class Partition {
    private static final int SIZE = 2;
    private ArrayList<String> content;
    private String processName;
    private int partitionNumber;
    private boolean occupied;
    private int value;

    public Partition(int partitionNumber, String processName) {
        this.partitionNumber = partitionNumber;
        this.content = new ArrayList<>(SIZE);
        this.processName = processName;
        this.occupied = true; // When a partition is created, it is occupied
    }


    public Partition(int value){
        this.value = value;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public int getPartitionNumber() {
        return partitionNumber;
    }

    public String getProcessName() {
        return processName;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static int getSIZE() {
        return SIZE;
    }

    @Override
    public String toString() {
        return "Process: " + this.processName + " -> Partition " + this.partitionNumber + " : content = " + this.getContent();
    }
}
