package memory;

import java.util.ArrayList;

public class Disc {

    private static ArrayList<FileInMemory> listaFile = new ArrayList<>();

    private static int NumOfBlocks = 1024 / Block.getSize();

    private static int SIZE = 1024;

    private static ArrayList<Block> zauzetProstor = new ArrayList<>(NumOfBlocks);
    private static Pointer slobodanProstor;

    public static ArrayList<FileInMemory> getListaFile() {
        return listaFile;
    }

    public static int getNumOfBlocks() {
        return NumOfBlocks;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public static ArrayList<Block> getZauzetProstor() {
        return zauzetProstor;
    }

    public static Pointer getSlobodanProstor() {
        return slobodanProstor;
    }

    public static void setSlobodanProstor(Pointer slobodanProstor) {
        Disc.slobodanProstor = slobodanProstor;
    }

    public static void addFile(FileInMemory file) {
        listaFile.add(file);
    }

    // Method to remove a file from the list
    public static void removeFile(FileInMemory file) {
        listaFile.remove(file);
    }

    // Method to add a block to the occupied space list
    public static void addOccupiedBlock(Block block) {
        zauzetProstor.add(block);
    }

    // Method to remove a block from the occupied space list
    public static void removeOccupiedBlock(Block block) {
        zauzetProstor.remove(block);
    }
}
