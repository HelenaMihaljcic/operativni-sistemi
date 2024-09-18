package system;

import assembler.AsmHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import memory.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SysMain extends Application {

    public static void getData(Path p,FileInMemory file)
    {
        try {
            if(Files.isDirectory(p))
            {
                var stream = Files.newDirectoryStream(p);
                Disc.addFile(file);
                for(Path p1:stream)
                {
                    if(Files.isDirectory(p1)) {
                        FileInMemory f =  new FileInMemory(p1.getFileName().toString(),0,file);
                        file.getPodfajlovi().add(f);
                        getData(p1,f);
                    }
                    else {
                        ////
                        List<String> content = Files.readAllLines(p1);
                        FileInMemory file1 = new FileInMemory(p1.getFileName().toString(),content.size(),file);
                        file1.setContent(new ArrayList<>(content));

                        Disc.addFile(file1);
                        file.getPodfajlovi().add(file1);

                        double dummy = content.size()*1.0/Block.getSize();
                        int br  = (int) Math.ceil(dummy);
                        int index = 0;

                        for(int i=0;i<br;i++)
                        {
                            Block b = Disc.getSlobodanProstor().getBlock();
                            b.setFileName(file1.getName());

                            for(int y = 0; y < Block.getSize(); y++)
                            {
                                if(index <content.size()) {
                                    b.getContent().add(content.get(index));
                                    index++;
                                }
                                else
                                    break;
                            }

                            b.setOcuppied(true);
                            Disc.setSlobodanProstor(Disc.getSlobodanProstor().getSledbenik());
                            Disc.getSlobodanProstor().setPrethodnik(null);
                            Disc.addOccupiedBlock(b);
                            ////
                        }

                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        //AsmHandler.instructionReader("src/programs/sum.asm");
        for (int i = 100; i < Ram.getNumOfFrames(); i++) {
            Ram.setFrameState(1, 2);
        }

        int address =0 ;
        Disc.setSlobodanProstor(new Pointer(new Block(address)));
        address++;
        Pointer pointer = new Pointer(new Block(address));
        Disc.getSlobodanProstor().setSledbenik(pointer);
        pointer.setPrethodnik(Disc.getSlobodanProstor());

        for(int i=1;i<1024;i++)
        {
            address++;
            Pointer p1 = new Pointer(new Block(address));
            pointer.setSledbenik(p1);
            p1.setPrethodnik(pointer);
            pointer = p1;
        }

        Path p = Paths.get("Disk");
        FileInMemory file = new FileInMemory(p.getFileName().toString(),0,null);
        getData(p,file);

       /* for(Block b:Disc.zauzetProstor)
        {
            System.out.println(b.getFileName() + "  " + b.getAddress() + "  " + b.getContent());
        }*/

        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(SysMain.class.getResource("/gui/main.fxml"));
        Scene sc = new Scene(fxmlLoader.load());
        sc.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
        stage.setTitle("VHos");
        stage.setScene(sc);
        stage.show();
    }
}