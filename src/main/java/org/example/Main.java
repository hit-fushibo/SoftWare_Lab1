package org.example;



public class Main {
    public static void main(String[] args) {
        FileIo test = new FileIo();
        MyGraph G = new MyGraph();
        test.setFilePath("./src/test.txt");
        test.CreateGraph(G);
        System.out.println(G.GetNewText("TO SEEK NEW LIFE"));
//        VGraph g = new VGraph("test");
//        Map<String, ArrayList<String>> vg = G.getVGraph();
//        g.CreatGraph(vg);
//
//        SwingUI UI = new SwingUI(g, G, test);
//        UI.show();

    }
}
