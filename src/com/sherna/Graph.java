package com.sherna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/*
 * @author sherna
 * @created 09/04/2020
 * @project CZ2001-Lab4
 */
public class Graph {
    // Each node maps to a list of all his neighbors
    public HashMap<Node, LinkedList<Node>> adjacencyMap;
    public boolean directed;

    /**
     * Constructor
     *
     * @param directed true if graph is directed
     *                 false if graph is undirected
     */
    public Graph(boolean directed) {
        this.directed = directed;
        adjacencyMap = new HashMap<>();
    }

    /**
     * In the helper method, we'll also make a check for possible duplicate edges.
     * Before adding an edge between A and B, we'll first remove it and only then add it.
     * If it existed (we're adding a duplicate edge), it was removed and after adding it again, there's only one.
     *
     * @param a
     * @param b
     */
    public void addEdgeHelper(Node a, Node b) {
        LinkedList<Node> tmp = adjacencyMap.get(a);

        if (tmp != null) {
            tmp.remove(b);
        } else tmp = new LinkedList<>();
        tmp.add(b);
        adjacencyMap.put(a, tmp);
    }

    /**
     * Actual method to add an edge from source node to destination node.
     *
     * @param source
     * @param destination
     */
    public void addEdge(Node source, Node destination) {

        // We make sure that every used node shows up in our .keySet()
        if (!adjacencyMap.keySet().contains(source))
            adjacencyMap.put(source, null);

        if (!adjacencyMap.keySet().contains(destination))
            adjacencyMap.put(destination, null);

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
        for (Node node : adjacencyMap.keySet()) {
            System.out.print("The node " + node.name + " has an edge towards: ");
            // if the linkedlist for that node is empty, then there is no edge.
            if (adjacencyMap.get(node) != null) {
                for (Node neighbor : adjacencyMap.get(node)) {
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
        System.out.println("Node count: " + adjacencyMap.size());
    }

    /**
     * Print the total number of edges.
     */
    public void printEdgeCount() {
        int edgeCount = 0;
        for (Node node : adjacencyMap.keySet()) {
            if (adjacencyMap.get(node) != null) {
                edgeCount += adjacencyMap.get(node).size();
            }
        }
        System.out.println("Edge count: " + edgeCount);
    }

    public void printNodeDegrees() {
        for (Node node : adjacencyMap.keySet()) {
            // if the linkedlist for that node is empty, then its degree is 0
            if (adjacencyMap.get(node) != null) {
                System.out.println("The node " + node.name + " has a degree of: " + adjacencyMap.get(node).size());
            } else {
                System.out.println("The node " + node.name + " has a degree of: 0");
            }
        }
    }

    /**
     * Check if an edge exists from source node to destination node
     *
     * @param source      source node
     * @param destination destination node
     * @return true if an edge exists from source node to destination node
     * false if an edge does not exists from source node to destination node
     */
    public boolean hasEdge(Node source, Node destination) {
        if (adjacencyMap.get(source) != null) {
            return adjacencyMap.containsKey(source) && adjacencyMap.get(source).contains(destination);
        } else {
            return false;
        }
    }

    /**
     * Obtain the maximum degree.
     *
     * @return
     */
    public int maxDegree() {
        int maxDegree = 0;
        for (Node node : adjacencyMap.keySet()) {
            if (adjacencyMap.get(node) != null && maxDegree < adjacencyMap.get(node).size()) {
                maxDegree = adjacencyMap.get(node).size();
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
        for (Node node : adjacencyMap.keySet()) {
            if (adjacencyMap.get(node) == null) {
                noOfNodes += 1;
            }
        }
        System.out.println("k = " + 0 + " has " + noOfNodes + " number of nodes. ");
        noOfNodes = 0; // reset

        // count no. of nodes for each k-value starting from 1.
        for (int i = 1; i <= maxDegree; i++) {
            for (Node node : adjacencyMap.keySet()) {
                if (adjacencyMap.get(node) != null && adjacencyMap.get(node).size() == i) {
                    noOfNodes += 1;
                }
            }
            System.out.println("k = " + i + " has " + noOfNodes + " number of nodes. ");
            noOfNodes = 0; // reset
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

        Graph read(boolean isDirected) {
            Graph g = null;
            try {
                g = scanGraph(isDirected);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return g;
        }

        Graph scanGraph(boolean isDirected) throws FileNotFoundException {
            graph = new Graph(isDirected);
            Scanner sc = new Scanner(new File(txtFileName));
            sc.nextLine();
            while (sc.hasNextLine()) {
                //System.out.println(sc.nextLine());      // returns the line that was skipped
                String[] tokens = sc.nextLine().split("\t");
                String str1 = tokens[0];
                String str2 = tokens[1];

                Node srcNode = null;
                Node destNode = null;

                if (graph.adjacencyMap.keySet().size() != 0) {
                    // check if str1 is an existing node
                    for (Node n : graph.adjacencyMap.keySet()) {
                        if (n.name.equals(str1)) {
                            srcNode = n;
                            break;
                        }
                    }

                    // check if str2 is an existing node
                    for (Node n : graph.adjacencyMap.keySet()) {
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
