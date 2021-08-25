package core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordParser {
    public List<Path> parseForDOCX(File... path) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (File f: path){
            Files.walk(f.toPath()).filter(x -> x.getFileName().toString().toLowerCase().endsWith(".docx"))
                    .forEach(paths::add);
        }
        return paths;
    }

    public List<Path> parseForDOC(File... path) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (File f: path){
            Files.walk(f.toPath()).filter(x -> x.getFileName().toString().toLowerCase().endsWith(".doc"))
                    .forEach(paths::add);
        }
        return paths;
    }
}