package memory;

import assembler.AsmHandler;
import system.ProcessScheduler;
import system.ProcessState;
import system.ShellCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Process extends Thread{

    private Map<Integer,Integer> partitionTable = new HashMap<>();
    private  Stack <String> stack= new Stack<>();
    private int numOfPartitions;
    private String processName;
    private String processName2;
    private boolean save = false;
    private int rezultat;
    private String saveFileName;
    private ArrayList<String> instructions = new ArrayList<>();
    public String currentInstruction;
    private int numExecutedInstructions = 0;
    private ProcessState stanje = ProcessState.READY;
    private long remainingSleepTime = 0;

    private int idProces;
    private final Object lock = new Object();

    public Process(String filePath, int id){
        this.processName = filePath;
        int x = -1;

        this.idProces = id;

        if(filePath.contains("/"))
            x = filePath.lastIndexOf("/");
        else if(filePath.contains("\\"))
            x = filePath.lastIndexOf("\\");
        try{
            this.processName2 = this.processName.substring(x+1) + "(" +id+")";
        }
        catch (StringIndexOutOfBoundsException e)
        {
            this.processName2=filePath+"("+id+")";
        }
    }
    @Override
    public void run() {


        AsmHandler asmHandler=new AsmHandler();

        FIFOController.fromDiskToRam(this);

        asmHandler.instructionReader(this);

        for(Integer i:partitionTable.keySet())
        {
            Ram.deallocateFrame(partitionTable.get(i));
        }

        if(this.stanje == ProcessState.DONE) {
            ShellCommands.threadSet.remove(this);
            ProcessScheduler.removeProcess(this);
        }

        if(this.save)
        {
            FIFOController.fromRamToDisk(this);
        }
    }

    public void kill() {
        this.interrupt();
        this.stanje = ProcessState.TERMINATED;
        ProcessScheduler.removeProcess(this);
        ShellCommands.threadSet.remove(this);
    }

    public void push(String element) {
        stack.push(element);
    }

    public String pop() {
        if (stack.isEmpty()) {
            return null; // or throw an exception, depending on your requirements
        }
        return stack.pop();
    }

    public String peek() {
        if (stack.isEmpty()) {
            return null; // or throw an exception, depending on your requirements
        }
        return stack.peek();
    }

    public int getIdProces() {
        return idProces;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getRezultat() {
        return rezultat;
    }

    public void setRezultat(int rezultat) {
        this.rezultat = rezultat;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public Map<Integer, Integer> getPartitionTable() {
        return partitionTable;
    }

    public int getNumExecutedInstructions() {
        return numExecutedInstructions;
    }

    public void addNumExecutedInstructions(int numExecutedInstructions) {
        this.numExecutedInstructions += numExecutedInstructions;
    }

    public void addToPartitionTable(int i, int j){
        partitionTable.put(i,j);
    }
    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setPartitionTable(Map<Integer, Integer> pageTable) {
        partitionTable = pageTable;
    }

    public void setNumOfPartitions(int numOfPages) {
        this.numOfPartitions = numOfPages;
    }




    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public String getProcessName() {
        return processName2;
    }

    public String getFilePath(){
        return this.processName;
    }

    public int getNumOfPartitions() {
        return numOfPartitions;
    }


    public Stack<String> getStack() {
        return stack;
    }

    public ProcessState getStanje() {
        return stanje;
    }

    public void setStanje(ProcessState stanje) {
        this.stanje = stanje;
    }

    public void setRemainingSleepTime(long remainingSleepTime) {
        this.remainingSleepTime = remainingSleepTime;
    }

    public long getRemainingSleepTime() {
        return remainingSleepTime;
    }

    public int getBurstTime() {
        return numExecutedInstructions;
    }

    @Override
    public String toString() {
        return processName2;
    }

}
