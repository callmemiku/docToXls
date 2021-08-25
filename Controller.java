package core;

import java.io.File;
import java.io.IOException;

public class Controller {

    private final View view;
    private final Model model;
    private final WordParser wordParser;

    public static void main(String[] args) {
        new Controller();
    }

    public Controller() {
        view = new View(this);
        model = new Model();
        wordParser = new WordParser();
        initView();
    }

    public void initView(){
        view.init();
    }

    public void gatherDataAndClean(File... path) throws IOException {
        model.setDOC_FILES(wordParser.parseForDOC(path));
        model.setDOCX_FILES(wordParser.parseForDOCX(path));
        model.gatherData();
        model.createXSSFs();
        model.cleanXSSFs();
    }

    public void write(String path) throws IOException {
        model.writeXSSFs(path);
    }



}
