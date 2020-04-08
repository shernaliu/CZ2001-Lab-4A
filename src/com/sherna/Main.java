package com.sherna;

/*
 * @author sherna
 * @created 09/04/2020
 * @project CZ2001-Lab4
 */

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Initializing graph, please wait...");
        Graph graph = new Graph.Reader("data/p2p-Gnutella08.txt").read(true);
        System.out.println("Initialization complete.");
        printMenu();

        String srcNode = null;
        String destNode = null;

        // get user input
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter input: ");
        int userInput = sc.nextInt();

        while (userInput != 0) {
            switch (userInput) {
                case 1:
                    System.out.println("(1) - Print edges");
                    graph.printEdges();
                    break;
                case 2:
                    System.out.println("(2) - Print node count");
                    graph.printNodeCount();
                    break;
                case 3:
                    System.out.println("(3) - Print edge count");
                    graph.printEdgeCount();
                    break;
                case 4:
                    System.out.println("(4) - Print degree of nodes");
                    graph.printNodeDegrees();
                    break;
                case 5:
                    System.out.println("(5) - Print node count for each k-value");
                    graph.printNodeCountForKValue();
                    break;
                case 6:
                    System.out.println("(6) - Check if source node has an edge to destination node");
                    System.out.println("Enter input for source node: ");
                    sc.nextLine();
                    srcNode = sc.nextLine();
                    System.out.println("Enter input for destination node: ");
                    destNode = sc.nextLine();
                    graph.hasEdge(srcNode, destNode);
                    break;
                case 7:
                    System.out.println("(7) - Output CSV");
                    graph.outputCSV();
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            printMenu();
            System.out.print("Enter input: ");
            userInput = sc.nextInt();
        }

        System.out.println("Goodbye!");
    }

    static void printMenu() {
        System.out.println("==================================");
        System.out.println("============== MENU ==============");
        System.out.println("Select a function:");
        System.out.println("(1) - Print edges");
        System.out.println("(2) - Print node count");
        System.out.println("(3) - Print edge count");
        System.out.println("(4) - Print degree of nodes");
        System.out.println("(5) - Print node count for each k-value");
        System.out.println("(6) - Check if source node has an edge to destination node");
        System.out.println("(7) - Output CSV");
        System.out.println("(0) - Exit");
        System.out.println("==================================");
    }
}