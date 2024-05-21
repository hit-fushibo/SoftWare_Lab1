package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class fileIO {
    private String filePath;

    public fileIO() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean CreateGraph(MyGraph G) {
        ArrayList<String> words = this.SplitTxt();
        int len = words.size();
        if (len < 2) {
            return true;
        }
        for (int i = 0; i < len - 1; i++) {
            String s = words.get(i).toLowerCase();
            String d = words.get(i + 1).toLowerCase();
            G.addNode(s);
            G.addNode(d);
            G.addRelation(s, d);
        }


        return true;
    }

    public void OutPutEdges(Map<String, Map<String, Integer>> edges) {
        Set<String> sources = edges.keySet();
        for (String source : sources) {
            Map<String, Integer> e = edges.get(source);
            Set<String> dist_s = e.keySet();
            for (String dist : dist_s) {
                int weight = e.get(dist);
                System.out.println(source + " -> " + dist + ":" + weight);

            }
        }
    }

    public void OutPutPath(ArrayList<String> path) {
        String out = "";
        for (int i = 0; i < path.size() - 1; i++) {
            out += path.get(i) + " -> ";
        }
        out += path.get(path.size() - 1);
        System.out.println(out);
    }

    public void OutPutBridgeWords(ArrayList<String> bridgeWords, String sour, String dest) {
        if (bridgeWords.isEmpty()) {
            System.out.println("No bridge words from " + sour + " to " + dest + "!");
        } else {
            String Out;
            if (bridgeWords.size() == 1) {
                Out = "The bridge words from " + sour + " to " + dest + " is:" + bridgeWords.get(0);
            } else {
                Out = "The bridge words from " + sour + " to " + dest + " are:";
                for (int i = 0; i < bridgeWords.size() - 1; i++) {
                    Out += bridgeWords.get(i) + ",";
                }
                Out += "and " + bridgeWords.get(bridgeWords.size() - 1);
            }
            System.out.println(Out);

        }
    }

    private @NotNull ArrayList<String> SplitTxt() {
        ArrayList<String> words = new ArrayList<>();
        File file = new File(filePath);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\t\r ");
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    word = word.replaceAll("[^a-zA-Z]", "");
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return words;
    }


}
