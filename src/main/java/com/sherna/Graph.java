package com.sherna;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

/*
 * @author sherna
 * @created 09/04/2020
 * @project CZ2001-Lab4
 */
public class Graph {
    // Each node maps to a list of all his neighbors
    public HashMap<Node, LinkedList<Node>> map;
    public boolean directed;
    public String fileName;

    /**
     * Constructor
     *
     * @param directed true if graph is directed
     *                 false if graph is undirected
     *                 note for undirected graph:
     *                 if text file has A -> B and B -> A, then pass parameter directed = true
     *                 if text file has A -> B but not B -> A, then pass parameter directed = false
     * @param fileName name of the file
     */
    public Graph(boolean directed, String fileName) {
        this.directed = directed;
        this.fileName = fileName;
        map = new HashMap<>();
    }

    /**
     * In the helper method, we'll also make a check for possible duplicate edges.
     * Before adding an edge between A and B, we'll first remove it and only then add it.
     * If it existed (we're adding a duplicate edge), it was removed and after adding it again, there's only one.
     *
     * @param a source node
     * @param b destination node
     */
    public void addEdgeHelper(Node a, Node b) {
        LinkedList<Node> tmp = map.get(a);

        if (tmp != null) {
            tmp.remove(b);
        } else tmp = new LinkedList<>();
        tmp.add(b);
        map.put(a, tmp);
    }

    /**
     * Actual method to add an edge from source node to destination node.
     *
     * @param source      source node
     * @param destination destination node
     */
    public void addEdge(Node source, Node destination) {

        // We make sure that every used node shows up in our .keySet()
        if (!map.keySet().contains(source))
            map.put(source, null);

        if (!map.keySet().contains(destination))
            map.put(destination, null);

        addEdgeHelper(source, destination);

        // If a graph is undirected, we want to add an edge from destination to source as well
        if (!directed) {
            addEdgeHelper(destination, source);
        }
    }

    /**
     * Print the edges of the graph.
     */
    public void printEdges() {
        for (Node node : map.keySet()) {
            System.out.print("The node " + node.name + " has an edge towards: ");
            // if the linkedlist for that node is empty, then there is no edge.
            if (map.get(node) != null) {
                for (Node neighbor : map.get(node)) {
                    System.out.print(neighbor.name + " ");
                }
            } else {
                System.out.print("none");
            }
            System.out.println();
        }
    }

    /**
     * Print the total number of nodes.
     */
    public void printNodeCount() {
        System.out.println("Node count: " + map.size());
    }

    /**
     * Print the total number of edges.
     */
    public void printEdgeCount() {
        int edgeCount = 0;
        for (Node node : map.keySet()) {
            if (map.get(node) != null) {
                edgeCount += map.get(node).size();
            }
        }
        System.out.println("Edge count: " + edgeCount);
    }

    /**
     * if the linkedlist for that node is empty, then its degree is 0
     */
    public void printNodeDegrees() {
        for (Node node : map.keySet()) {
            if (map.get(node) == null)
                System.out.println(node.name + " has degree 0");
            if (map.get(node) != null)
                System.out.println(node.name + " has degree " + map.get(node).size());
        }
    }

    /**
     * Obtain the maximum degree.
     *
     * @return maximum degree
     */
    public int maxDegree() {
        int maxDegree = 0;
        for (Node node : map.keySet()) {
            if (map.get(node) != null && maxDegree < map.get(node).size()) {
                maxDegree = map.get(node).size();
            }
        }
        return maxDegree;
    }

    /**
     * Print the number of nodes for each k-value
     * aka how many nodes have degree 0,1,2,3,...
     */
    public void printNodeCountForKValue() {
        int maxDegree = maxDegree();
        int noOfNodes = 0;

        // count no. of nodes for k=0 (degree of 0)
        for (Node node : map.keySet()) {
            if (map.get(node) == null) {
                noOfNodes += 1;
            }
        }
        System.out.println("k = " + 0 + " has " + noOfNodes + " number of nodes. ");
        noOfNodes = 0; // reset

        // count no. of nodes for each k-value starting from 1.
        for (int i = 1; i <= maxDegree; i++) {
            for (Node node : map.keySet()) {
                if (map.get(node) != null && map.get(node).size() == i) {
                    noOfNodes += 1;
                }
            }
            System.out.println("k = " + i + " has " + noOfNodes + " number of nodes. ");
            noOfNodes = 0; // reset
        }
    }

    /**
     * Checks if an edge exists from source node to destination node.
     *
     * @param source
     * @param destination
     */
    public void hasEdge(String source, String destination) {
        Node src = null;
        Node dest = null;

        // search for the source node if it exists
        for (Node node : map.keySet()) {
            if (node.name.equalsIgnoreCase(source)) {
                src = node;
                break;
            }
        }

        // if source node does not exist as a source, then it doesn't have an edge.
        if (src == null) {
            System.out.println(source.toUpperCase() + " is not an existing source node.");
            return;
        }

        // search for the destination node if it exists
        for (Node node : map.keySet()) {
            if (node.name.equalsIgnoreCase(destination)) {
                dest = node;
                break;
            }
        }

        if (map.get(src) != null) {
            if (map.containsKey(src) && map.get(src).contains(dest)) {
                System.out.println("The edge exists from " + source.toUpperCase() + " to " + destination.toUpperCase() + ".");
            } else {
                System.out.println("The edge does not exist from " + source.toUpperCase() + " to " + destination.toUpperCase() + ".");
            }
        } else {
            System.out.println("The edge does not exist from " + source.toUpperCase() + " to " + destination.toUpperCase() + ".");
        }
    }

    public void outputExcelFile() {
        int maxDegree = maxDegree();
        float noOfNodes = 0;
        float totalNoOfNodes = map.size();

        // Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Scatterplot");

        int rownum = 0;
        int cellnum = 0;
        Row row = null;
        Cell cell = null;

        // write header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("k-value");
        cell = row.createCell(cellnum++);
        cell.setCellValue("No. of nodes");
        cell = row.createCell(cellnum++);
        cell.setCellValue("ln k");
        cell = row.createCell(cellnum++);
        cell.setCellValue("ln P");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Total no. of nodes");
        cell = row.createCell(cellnum++);
        cell.setCellValue("bin_lower");
        cell = row.createCell(cellnum++);
        cell.setCellValue("bin_upper");
        cell = row.createCell(cellnum++);
        cell.setCellValue("bins (k-value)");
        cell = row.createCell(cellnum++);
        cell.setCellValue("rel. freq (no. of nodes)");

        // count no. of nodes for k=0 (degree of 0)
        for (Node node : map.keySet()) {
            if (map.get(node) == null) {
                noOfNodes += 1;
            }
        }

        // write no. of nodes for k=0 (degree of 0)
        cellnum = 0;
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue(0);
        cell = row.createCell(cellnum++);
        cell.setCellValue(noOfNodes);
        // ln(0) is undefined
        cell = row.createCell(cellnum++);
        cell.setCellValue(Math.log(0)); // ln k
        cell = row.createCell(cellnum++);
        cell.setCellValue(Math.log(noOfNodes)); // ln P
        cell = row.createCell(cellnum++);
        cell.setCellValue(map.size()); // total no. of nodes
        noOfNodes = 0; // reset

        // count no. of nodes for each k-value starting from 1.
        for (int i = 1, j = 5; i <= maxDegree; i++, j += 1) {
            for (Node node : map.keySet()) {
                if (map.get(node) != null && map.get(node).size() == i) {
                    noOfNodes += 1;
                }
            }

            // write no. of nodes for k=1,2,3,...,maxDegree
            cellnum = 0;
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue(i); // k-value
            cell = row.createCell(cellnum++);
            cell.setCellValue(noOfNodes);
            cell = row.createCell(cellnum++);
            cell.setCellValue(Math.log(i)); // ln k
            cell = row.createCell(cellnum++);
            cell.setCellValue(Math.log(noOfNodes)); // ln P
            noOfNodes = 0; // reset
        }


        try {
            FileOutputStream out = new FileOutputStream(new File("data/output-" + this.fileName + ".xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Done. Output file name is " + "data/output-" + this.fileName + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * static Reader class to read from the text file and initialize the graph.
     */
    static class Reader {
        String txtFileName;
        Graph graph;

        Reader(String txtFileName) {
            this.txtFileName = txtFileName;
        }

        Graph read(boolean isDirected, String fileName) {
            Graph g = null;
            try {
                g = scanGraph(isDirected, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return g;
        }

        Graph scanGraph(boolean isDirected, String fileName) throws FileNotFoundException {
            graph = new Graph(isDirected, fileName);
            Scanner sc = new Scanner(new File(txtFileName));
            sc.nextLine();
            while (sc.hasNextLine()) {
                //System.out.println(sc.nextLine());      // returns the line that was skipped
                String[] tokens = sc.nextLine().split("\t");
                String str1 = tokens[0];
                String str2 = tokens[1];

                Node srcNode = null;
                Node destNode = null;

                if (graph.map.keySet().size() != 0) {
                    // check if str1 is an existing node
                    for (Node n : graph.map.keySet()) {
                        if (n.name.equals(str1)) {
                            srcNode = n;
                            break;
                        }
                    }

                    // check if str2 is an existing node
                    for (Node n : graph.map.keySet()) {
                        if (n.name.equals(str2)) {
                            destNode = n;
                            break;
                        }
                    }
                } else {
                    // init
                    srcNode = new Node(str1);
                    destNode = new Node(str2);
                }

                // if srcNode / destNode is null, means it isnt an existing node
                if (srcNode == null) {
                    srcNode = new Node(str1);
                }

                if (destNode == null) {
                    destNode = new Node(str2);
                }

                // add edges from srcNode to destNode
                graph.addEdge(srcNode, destNode);
            }

            return graph;
        }
    }
}
