import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        GameProgress gameProgress1 = new GameProgress(95,5,10,130.34);
        GameProgress gameProgress2 = new GameProgress(84,4,16,280.64);
        GameProgress gameProgress3 = new GameProgress(100,11,35,840.76);

        String saved1 = "/Users/yaroslavvoitiuk/Games/savegames/save1.dat";
        String saved2 = "/Users/yaroslavvoitiuk/Games/savegames/save2.dat";
        String saved3 = "/Users/yaroslavvoitiuk/Games/savegames/save3.dat";

        File saves1 = new File(saved1);
        File saves2 = new File(saved2);
        File saves3 = new File(saved3);

        saveProgress(saved1, gameProgress1);
        saveProgress(saved2, gameProgress2);
        saveProgress(saved3, gameProgress3);

        String zipDirectory = "/Users/yaroslavvoitiuk/Games/savegames/savings.zip";

        List<String> savingList = new ArrayList<>();
        savingList.add(saved1);
        savingList.add(saved2);
        savingList.add(saved3);
        zipFiles(zipDirectory,savingList);

        if (saves1.delete() && saves2.delete() && saves3.delete()) System.out.println("Незаархивированные файли удалены");

        openZip(zipDirectory,"/Users/yaroslavvoitiuk/Games/savegames/");

        System.out.println(openProgress(saved1));

    }

    public static void saveProgress(String fileDirectory, GameProgress gameProgress){
        try {
            FileOutputStream fos = new FileOutputStream(fileDirectory);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameProgress);
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void zipFiles(String zipDirectory, List<String> savedFiles){
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipDirectory))){
            for (int i = 0; i < savedFiles.size(); i ++) {
                FileInputStream fis = new FileInputStream(savedFiles.get(i));
                String [] fileName = savedFiles.get(i).split("/");
                ZipEntry zipEntry = new ZipEntry(fileName[fileName.length-1]);
                zout.putNextEntry(zipEntry);

                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void openZip(String zipDirectory, String unpackedZip){
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipDirectory))){
            ZipEntry entry;
            String name;

            while ((entry = zis.getNextEntry()) != null){
                name = entry.getName();
                FileOutputStream fos = new FileOutputStream(unpackedZip + name);
                for (int c = zis.read(); c != -1; c = zis.read()){
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public static GameProgress openProgress(String savedFile) {
        GameProgress gameProgress = null;
        try{
            FileInputStream fis = new FileInputStream(savedFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            e.getMessage();
        }
        return gameProgress;
    }



}
