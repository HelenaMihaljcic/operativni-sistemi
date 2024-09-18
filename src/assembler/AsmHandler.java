package assembler;

import gui.Controller;
import memory.Partition;
import memory.Ram;
import memory.Process;
import system.ProcessState;
import system.ShellCommands;

import java.util.ArrayList;
import java.util.List;

public class AsmHandler {

    public void instructionReader(Process process) {
        List<String> asmFileLines = new ArrayList<>();
        Operations operations = new Operations(process);
        int[] frames = Ram.getFrames();
        // Collect instructions from the partitions allocated to the process
        for (int i = 0; i < Ram.getNumOfFrames(); i++) {
            if (frames[i] == 1 && Ram.getPartition(i).getProcessName().equalsIgnoreCase(process.getProcessName())) {
                Partition p = Ram.getPartition(i);
                asmFileLines.addAll(p.getContent());
            }
        }


        for (int i = 0; i < asmFileLines.size(); i++) {
            // Execute instructions according to SPN scheduling
            instructionRunner(asmFileLines.get(i), process, operations);
            process.addNumExecutedInstructions(1);
        }

        process.setStanje(ProcessState.DONE);
    }

    private void instructionRunner(String instruction, Process process, Operations operations) {
        process.currentInstruction = instruction;
        String[] arr = instruction.split(" ");

        switch (arr[0]) {
            case "ADD":
                operations.add();
                break;

            case "SUB":
                operations.sub();
                break;

            case "MUL":
                operations.mul();
                break;

            case "DIV":
                operations.div();
                break;

            case "PUSH":
                operations.push(arr[1]);
                break;

            case "POP":
                String val = operations.pop();
                process.setRezultat(Integer.parseInt(val));
                Controller.updateTa(process.getProcessName() + ":" + val);

                int[] frames = Ram.getFrames();
                for (int i = 100; i < Ram.getNumOfFrames(); i++) {
                    if (frames[i] == 2) {
                        Ram.setFrameState(2, 3);
                        Partition p = new Partition(Integer.parseInt(val));
                        Ram.addPartition(i, p);
                        break;
                    }
                }
                process.setStanje(ProcessState.DONE);
                ShellCommands.threadSet.remove(process);
                break;

            case "INC":
                operations.inc();
                break;

            case "DEC":
                operations.dec();
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + arr[0]);
        }
    }
}
