package org.example;

import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.images.Resolutions;
import org.graphstream.ui.swing.util.SwingFileSinkImages;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@SuppressWarnings({"FieldCanBeLocal", "StringConcatenationInLoop"})
public class SwingUI {
    private final VGraph graphs;
    private final MyGraph G;
    private final fileIO I;
    private final AtomicBoolean isRandomWalkFinished = new AtomicBoolean(false);
    private final AtomicBoolean stopTraversal = new AtomicBoolean(false);
    private final AtomicInteger isError = new AtomicInteger(0);
    private JButton readText;
    private JButton randomWalkBegin;
    private JButton randomWalkStop;
    private JButton searchBridgeWords;
    private JButton shortestPath;
    private JButton generateNewText;
    private JButton saveGraph;
    private Thread randomWalkThread;
    private JTextField inputWord1;
    private JTextField inputWord2;
    private JTextArea originText;
    private JPanel leftPanel;

    public SwingUI(VGraph graphs, MyGraph g, fileIO I) {
        this.graphs = graphs;
        this.G = g;
        this.I = I;
    }

    public void show() {
        JFrame frame = new JFrame("Swing UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));
        SwingViewer viewer = new SwingViewer(this.graphs.graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        View view = viewer.addDefaultView(false);
        view.getCamera().setViewPercent(1.5);
        this.leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillRect(50, 50, 200, 200);
            }
        };
        leftPanel.add((Component) view);
        // 左部放置 graphs.graph
        frame.add((Component) view);

        // 右部分为4行
        JPanel rightPanel = new JPanel(new GridLayout(4, 1));

        // 第一行放按钮
        JPanel buttonPanel = new JPanel();
        this.readText = new JButton("Read Text");
        this.randomWalkBegin = new JButton("Random Walk Begin");
        this.randomWalkStop = new JButton("Random Walk Stop");
        this.saveGraph = new JButton("save graph");

        this.readText.addActionListener(this::onReadTextButtonPressed);
        this.randomWalkBegin.addActionListener(this::onRandomWalkBeginButtonPressed);
        this.randomWalkStop.addActionListener(this::onRandomWalkStopButtonPressed);
        this.saveGraph.addActionListener(this::onSaveGraphButtonPressed);

        buttonPanel.add(this.readText);
        buttonPanel.add(this.randomWalkBegin);
        buttonPanel.add(this.randomWalkStop);
        buttonPanel.add(this.saveGraph);
        rightPanel.add(buttonPanel);

        // 第二行放输入框
        JPanel inputPanel = new JPanel();
        this.inputWord1 = new JTextField(10);
        this.inputWord2 = new JTextField(10);
        inputPanel.add(this.inputWord1);
        inputPanel.add(this.inputWord2);
        rightPanel.add(inputPanel);

        // 第三行放按钮
        JPanel buttonPanel2 = new JPanel();
        this.searchBridgeWords = new JButton("Search Bridge Words");
        this.shortestPath = new JButton("Shortest Path");
        this.generateNewText = new JButton("Generate New Text");

        this.searchBridgeWords.addActionListener(this::onSearchBridgeWordsButtonPressed);
        this.shortestPath.addActionListener(this::onShortestPathButtonPressed);
        this.generateNewText.addActionListener(this::onGenerateNewTextButtonPressed);

        buttonPanel2.add(this.searchBridgeWords);
        buttonPanel2.add(this.shortestPath);
        buttonPanel2.add(this.generateNewText);
        rightPanel.add(buttonPanel2);

        // 第四行放文本框
        this.originText = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(this.originText);
        rightPanel.add(scrollPane);

        frame.add(rightPanel);

        frame.pack();
        frame.setVisible(true);
    }

    private void onSaveGraphButtonPressed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
        fileChooser.setCurrentDirectory(new File("./src/"));
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            SwingFileSinkImages pic = new SwingFileSinkImages(FileSinkImages.OutputType.PNG, Resolutions.HD720);

            pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
            try {
                pic.writeAll(this.graphs.graph, selectedFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(selectedFilePath);
        }
    }

    private void onReadTextButtonPressed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            this.I.setFilePath(selectedFilePath);
            this.I.CreateGraph(this.G);
            this.graphs.clearGraph();
            Map<String, ArrayList<String>> vg = this.G.getVGraph();
            this.graphs.CreatGraph(vg);
        }
    }

    private void onRandomWalkBeginButtonPressed(ActionEvent e) {
        this.graphs.setAllNodeWhite();
        this.graphs.setAllEdgeDefault();
        this.stopTraversal.set(false);
        ArrayList<String> nodes = new ArrayList<>();
        this.randomWalkThread = new Thread(() -> {
            String currentNode = G.SetRandomWalkStartNode();

            String nextNode;
            this.graphs.setNodeFillBlue(currentNode);
            AtomicInteger isStop = new AtomicInteger(0);
            while (!this.stopTraversal.get()) {
                nodes.add(currentNode);
                nextNode = G.RandomWalkOneStep(isStop);
                if (isStop.get() == 1) {
                    this.graphs.setNodeFillWhite(currentNode);
                    break;
                } else {
                    this.graphs.setNodeFillWhite(currentNode);
                    this.graphs.setNodeFillBlue(nextNode);
                    currentNode = nextNode;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException error) {
                    throw new RuntimeException(error);
                }
            }
            isRandomWalkFinished.set(true);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("./src/"));

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                String path = "";
                for (int i = 0; i < nodes.size() - 1; i++) {
                    path += nodes.get(i) + " -> ";
                }
                path += nodes.get(nodes.size() - 1);
                try {
                    File file = new File(selectedFilePath);
                    FileWriter writer = new FileWriter(file);
                    writer.write(path);
                    writer.close();

                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });
        isRandomWalkFinished.set(false);

        this.randomWalkThread.start();


    }

    private void onRandomWalkStopButtonPressed(ActionEvent e) {
        if (!this.isRandomWalkFinished.get()) {
            this.stopTraversal.set(true);
            this.graphs.setAllNodeWhite();

        }
    }

    private void onSearchBridgeWordsButtonPressed(ActionEvent e) {
        this.graphs.setAllNodeWhite();
        this.graphs.setAllEdgeDefault();
        String sour = this.inputWord1.getText();
        String dest = this.inputWord2.getText();
        this.isError.set(0);
        ArrayList<String> bridgeWords = this.G.searchBridgeWord(sour, dest, this.isError);
        if (this.isError.get() == 1) {
            JOptionPane.showMessageDialog(null, "No \"" + sour + "\" in the graph!");
        } else if (this.isError.get() == 2) {
            JOptionPane.showMessageDialog(null, "No \"" + dest + "\" in the graph!");
        } else if (this.isError.get() == 3) {
            JOptionPane.showMessageDialog(null, "No \"" + sour + "\" and \"" + dest + "\" in the graph!");
        } else {
            this.graphs.setNodeFillRed(sour);
            this.graphs.setNodeFillRed(dest);
            for (String b : bridgeWords) {
                this.graphs.setNodeFillBlue(b);
            }
        }

    }

    private void onShortestPathButtonPressed(ActionEvent e) {
        this.graphs.setAllNodeWhite();
        this.graphs.setAllEdgeDefault();
        String sour = this.inputWord1.getText();
        String dest = this.inputWord2.getText();
        this.isError.set(0);
        ArrayList<String> path = this.G.shortestPath(sour, dest, this.isError);
        if (this.isError.get() == 1) {
            JOptionPane.showMessageDialog(null, "No \"" + sour + "\" in the graph!");
        } else if (this.isError.get() == 2) {
            JOptionPane.showMessageDialog(null, "No \"" + dest + "\" in the graph!");
        } else if (this.isError.get() == 3) {
            JOptionPane.showMessageDialog(null, "No \"" + sour + "\" and \"" + dest + "\" in the graph!");
        } else {
            this.graphs.setNodeFillRed(sour);
            this.graphs.setNodeFillRed(dest);
            for (int i = 0; i < path.size() - 1; i++) {
                String edgeId = path.get(i) + "->" + path.get(i + 1);
//                System.out.println(edgeId);
                this.graphs.setEdgeColorGreen(edgeId);
            }
        }
    }

    private void onGenerateNewTextButtonPressed(ActionEvent e) {
        String text = this.originText.getText();
        text = text.replaceAll("\n", "");
        String newText = this.G.GetNewText(text);
        this.originText.setText(newText);
    }

}
