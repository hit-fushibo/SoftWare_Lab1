package org.example;

import java.util.ArrayList;
import java.util.Map;


public class test {
    public static void main(String[] args) {
        fileIO test = new fileIO();
        MyGraph G = new MyGraph();
        test.setFilePath("./src/test.txt");
        test.CreateGraph(G);
        VGraph g = new VGraph("test");
        Map<String, ArrayList<String>> vg = G.getVGraph();
        g.CreatGraph(vg);

        SwingUI UI = new SwingUI(g, G, test);
        UI.show();

    }
}
