import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(10, 5, 5, 10.0);
        GameProgress gameProgress2 = new GameProgress(20, 10, 10, 20.0);
        GameProgress gameProgress3 = new GameProgress(30, 15, 15, 15.5);

        saveGame("/Users/vitalijsmelev/Games/savegames/progress1.dat", gameProgress1);
        saveGame("/Users/vitalijsmelev/Games/savegames/progress2.dat", gameProgress2);
        saveGame("/Users/vitalijsmelev/Games/savegames/progress3.dat", gameProgress3);

        zipFiles("/Users/vitalijsmelev/Games/savegames/zip_output.zip",
                Arrays.asList("/Users/vitalijsmelev/Games/savegames/progress1.dat",
                        "/Users/vitalijsmelev/Games/savegames/progress2.dat",
                        "/Users/vitalijsmelev/Games/savegames/progress3.dat"));

        removeNotZipFiles("/Users/vitalijsmelev/Games/savegames/");
    }


    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private static void zipFiles(String zipFile, List<String> filePathList) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String filePath : filePathList) {
                FileInputStream fis = new FileInputStream(filePath);
                try {
                    ZipEntry entry = new ZipEntry(filePath);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                } catch (Exception e) {
                    fis.close();
                }
            }
            zout.closeEntry();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void removeNotZipFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] listFiles = directory.listFiles(new FileFilter() {
            private String getExtension(File pathname)
            {
                String filename = pathname.getPath();
                int i = filename.lastIndexOf('.');
                if ((i > 0) && (i < filename.length()-1)) {
                    return filename.substring(i+1).toLowerCase();
                }
                return "";
            }

            @Override
            public boolean accept(File pathname) {
                String extension = getExtension(pathname);
                if ("zip".equalsIgnoreCase(extension)) {
                        return false;
                }
                return true;
            }
        });
        for (File f : listFiles) {
            f.delete();
        }
    }
}
