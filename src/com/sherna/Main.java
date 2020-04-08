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
        Graph graph = new Graph.Reader("p2p-Gnutella08.txt").read(true);
        System.out.println("Initialization complete.");
        printMenu();

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
                default:
                    System.out.println("Invalid input.");
            }
            System.out.println("----------------------------------");
            System.out.print("Enter input: ");
            userInput = sc.nextInt();
        }

        System.out.println("Goodbye!");
    }

    static void printMenu() {
        System.out.println("----------------------------------");
        System.out.println("Welcome to CZ2001 Algorithms!");
        System.out.println("Select a function:");
        System.out.println("(1) - Print edges");
        System.out.println("(2) - Print node count");
        System.out.println("(3) - Print edge count");
        System.out.println("(4) - Print degree of nodes");
        System.out.println("(-1) - Exit");
        System.out.println("----------------------------------");
    }
}