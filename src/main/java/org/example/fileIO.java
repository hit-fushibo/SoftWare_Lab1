package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class fileIO {
    private String filePath;

    public fileIO() {
    }

    /**
     * 设置输入文件路径
     *
     * @param filePath
     * 要设置的输入文件路径
     *
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 从设置的输入文件构建图
     * @param G
     * 要构建的MyGraph图对象
     * @return
     * 返回是否构建成功
     */
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

    /**
     * 输出图结构
     * @param edges
     * 一个Map用每条边的起点作为索引。其对应的值为一个Map，用对应的终点作为索引，其值为边权值
     */
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

    /**
     * 输出一条路径
     * @param path
     * 为一个List，按顺序为要输出路径经过的节点名称
     */
    public void OutPutPath(ArrayList<String> path) {
        String out = "";
        for (int i = 0; i < path.size() - 1; i++) {
            out += path.get(i) + " -> ";
        }
        out += path.get(path.size() - 1);
        System.out.println(out);
    }

    /**
     * 输出查询出来的word1到word2的桥接词
     * @param bridgeWords
     * 一个List，为查询出来的桥接词
     * @param sour
     * word1
     * @param dest
     * word2
     */
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

    /**
     * 从输入文件中划分单词，以空格和换行符作为分割符，忽略非大小写字母的字符
     * @return
     * 按文件中出现的顺序返回的单词序列
     */
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
