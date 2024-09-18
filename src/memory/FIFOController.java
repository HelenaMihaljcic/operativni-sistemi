package memory;


// OVO NE VALJA, TREBA DA UZIMA PRVI PROCES KOJI STIGNE U RAM I SALJE GA ASEMBLERU I DA IH DALJE SALJE PO FIFO
// MSM DA TREBA ICI NA FAZON DMA kod borisa i borisa ALI BAS NE KONTAM
// treba uzeti prvi proces u redu sa rama i dati mu da obradi fajl (prenos sa diska na ram)
// a obrnuto ne znam (prvom procesu u redu dati da nesto radi, sta ne znam)
// spominje se kod procesa u run, poslije 51 linije

import system.ShellCommands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FIFOController {
    private Queue<Process> requestQueue;
    private Ram ram;
    private Disc disc;
    private Lock lock = new ReentrantLock();

    public FIFOController(Queue<Process> requestQueue, Ram ram, Disc disk) {
        this.requestQueue = requestQueue;
        this.ram = ram;
        this.disc = disk;
    }

    public static void fromDiskToRam(Process procces)
    {
        try {

            procces.setInstructions((ArrayList<String>) Files.readAllLines(Paths.get(procces.getFilePath())));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
        }

        procces.setNumOfPartitions(procces.getInstructions().size() / Partition.getSIZE());
        int br = 0;

        for(int i=0;i<procces.getNumOfPartitions();i++)
        {
            Partition p = new Partition(i, procces.getProcessName());
            int x = 0;

            while(x<Partition.getSIZE())
            {
                if(br<procces.getInstructions().size())
                    p.getContent().add(procces.getInstructions().get(br));
                x++;
                br++;
            }

            for(int j = 0;j< Ram.getNumOfFrames();j++)
            {
                int[] frames = Ram.getFrames();
                if(frames[j] == 0)
                {
                    Ram.setFrameState(j, 1);
                    Ram.addPartition(j, p);
                    procces.addToPartitionTable(i, j);
                    break;
                }
            }
        }
    }


    public static void fromRamToDisk(Process p) {
        try {
        File newFile = new File(ShellCommands.getCurrentDir() + "\\" + p.getSaveFileName() + ".txt");
        if (!newFile.exists()) {
            for (FileInMemory f : Disc.getListaFile())
                if (f.getName().equals(ShellCommands.trenutniDirNaziv)) {
                    FileInMemory fim = new FileInMemory(p.getSaveFileName(), 1, f);
                    Disc.addFile(fim);
                    f.addToPodfajl(fim);


                    newFile.createNewFile();

                    FileWriter fw = new FileWriter(newFile);
                    String poruka = "Rezultat izvrsavanja: " + p.getRezultat();
                    fw.write(poruka);
                    fw.close();
                    ArrayList<String> lista = new ArrayList<>();
                    lista.add(poruka);

                    ////
                    fim.setContent(lista);


                    Block b = Disc.getSlobodanProstor().getBlock();
                    b.setFileName(p.getSaveFileName());
                    b.getContent().add(poruka);

                    b.setOcuppied(true);
                    Disc.setSlobodanProstor(Disc.getSlobodanProstor().getSledbenik());
                    Disc.getSlobodanProstor().setPrethodnik(null);
                    Disc.addOccupiedBlock(b);

                    break;

                } else {
                    System.out.println("directory already exists.");
                    return;
                }


        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }



//
//    public void fromDiskToRamInternal(Request request) {
//        lock.lock();
//        try {
//            String data = disc.read();
//            String[] partitions = data.split("\n");
//            for (String partition : partitions) {
//                String[] parts = partition.split(",");
//                int size = Integer.parseInt(parts[0].trim());
//                int startAddress = Integer.parseInt(parts[1].trim());
//                Partition p = new Partition(size, startAddress);
//                Ram.memory.put(startAddress, p);
//            }
//        } catch (Exception e) {
//            System.err.println("Error reading from disk: " + e.getMessage());
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void fromRamToDiskInternal(Request request) {
//        lock.lock();
//        try {
//            StringBuilder data = new StringBuilder();
//            for (Partition p : Ram.memory.values()) {
//                data.append(p.getSize()).append(",").append(p.getStartAddress()).append("\n");
//            }
//            disk.write(data.toString());
//        } catch (Exception e) {
//            System.err.println("Error writing to disk: " + e.getMessage());
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void processRequests() {
//        lock.lock();
//        try {
//            while (!requestQueue.isEmpty()) {
//                Request request = requestQueue.poll();
//                if (request.getType() == Request.Type.DISKTORAM) {
//                    fromDiskToRamInternal(request);
//                } else {
//                    fromRamToDiskInternal(request);
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    private class Request {
//        public enum Type {
//            DISKTORAM,
//            RAMTODISK
//        }
//
//        private Type type;
//        private String data;
//        private int address;
//
//        public Request(Type type, String data, int address) {
//            this.type = type;
//            this.data = data;
//            this.address = address;
//        }
//
//        public Type getType() {
//            return type;
//        }
//
//        public String getData() {
//            return data;
//        }
//
//        public int getAddress() {
//            return address;
//        }
//    }
}


