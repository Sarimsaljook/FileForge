import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FolderListing {
    public static List<String> listFiles(String uid) {
        List<String> fileList = new ArrayList<>();
        File folder = new File("C:/Users/sarim/Downloads/FileForgeDrive/" + uid);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String directoryName = file.getName() + "/";
                        // List files within the subdirectory
                        File[] subFiles = file.listFiles();
                        if (subFiles != null) {
                            for (File subFile : subFiles) {
                                if (subFile.isDirectory()) {
                                    String SubSubdirectoryName = subFile.getName() + "/";
                                    // List files within the subdirectory
                                    File[] SubSubFiles = subFile.listFiles();
                                    if (SubSubFiles != null) {
                                        for (File subsubfile : SubSubFiles) {
                                            if (subsubfile.isDirectory()) {
                                                String SubSubSubdirectoryName = subsubfile.getName() + "/";
                                                // List files within the subdirectory
                                                File[] SubSubSubFiles = subsubfile.listFiles();
                                                if (SubSubSubFiles != null) {
                                                    for (File subsubsubfile : SubSubSubFiles) {
                                                        fileList.add(directoryName + SubSubSubdirectoryName + subsubsubfile.getName());
                                                    }
                                                }

                                            } else {
                                                fileList.add(directoryName + SubSubdirectoryName + subsubfile.getName());
                                            }
                                        }
                                    }

                                } else {
                                    fileList.add(directoryName + subFile.getName());
                                }
                            }
                        }
                    } else {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        return fileList;
    }
}
