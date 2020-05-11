/* This program was written by Kayla Ritchie for Assignment 2 of CS4050. (Third version - The Redo)
     The program makes a word tree based on a file specified by the user.  Within the menu 'd' represents
     'default' and is hard coded with word.txt (which is the list given for the assignment) and 'c' represents 
     'custom'  and the word list wanting to be used is specified by the user. 
     The GUI consists of three pieces, the top label tells whether or not the string in the text field is a word, 
     the text field is where the user types, and the text area below where the word predictions are shown.
     This program uses path traversal to get to a point where inorder depth-first search can be used to find 
     the possible words. 
*/


import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class A2Redo {
   // Tree building variables
   private static Node root = new Node(' '); 
   private static Node current;
   private static ArrayList<String> temp;
   private static String[] library;
   private static Scanner userInput, in;

   // Word variables
   static ArrayList<String> wordTemp;
   
   public static void main(String[] args) {
      wordTemp = new ArrayList<String>();
      menu();
      fillLibrary();
      buildTree();
      buildGUI();
   }



   public static void menu() {
      userInput = new Scanner(System.in);
      String input;
      boolean done = false;
      
      while (!done) {
         say("Hello, would you like to use a custom list (c) or the default list (d)?");
         say("Enter 'c' or 'd'.");
         input = userInput.nextLine();
         //debug(input);
         if (input.equals("c") || input.equals("C")) {
            say("What is the file name of the list you want to use?");   
            input = userInput.nextLine();
         }
         else if (input.equals("d") || input.equals("D")) {
            input = "words.txt";
         }
         else {
            say("Enter 'c' or 'd'.");
         }
         try {
            in = new Scanner(new File(input));
            debug("Scanner made.");
            done = true;
         }
         catch (Exception e) {
             say("File does not exist, try again."); 
         }
      }   
      
   }

   public static void fillLibrary() {
      temp = new ArrayList<String>();
      while (in.hasNext()) {
         temp.add(in.nextLine().toLowerCase());
      }
      debug("ArrayList filled.");
      library = new String[temp.size()];
      library = temp.toArray(library);
      debug("Library made.");
   }

   public static void buildTree() {
      debug("Building Tree...");
      for (int i = 0; i < library.length; i++) {
         String word = library[i];
         debug(word);
         current = root;
         for (int j = 0; j < word.length(); j++) {
            char currChar = word.charAt(j);
            Node[] conn = current.getConnections();
            //debug("Conn size: " + current.getCount());
            if (current.getCount() > 0) {
               boolean found = false;
               int ind = -1;
               for (int k = 0; k < current.getCount(); k++) {
                  if (conn[k].getName() == currChar) {
                     found = true;
                     ind = k;
                  }
               }
               if (found) {
                  current = conn[ind];
                  debug("Moved to node: " + current.getName());
               }
               else {
                  Node newNode = new Node(currChar);
                  current.addConnection(newNode);
                  current = newNode;
                  debug("Made new and moved to node: " + current.getName());
               }
            }
            else {
               Node newNode = new Node(currChar);
               current.addConnection(newNode);
               current = newNode;
               debug("Made new and moved to node: " + current.getName());
            }
         }
         current.isWord();
         current.setWord(word);
         debug("Added and isWord: " + word);
      } // End for word
      debug("Tree built.");
   }

   public static void buildGUI()  {
      JFrame frame = new JFrame("Auto Complete Assignment - Kayla Ritchie");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JTextField text = new JTextField(20);
      JTextArea wordList = new JTextArea(10, 1);
      JLabel label = new JLabel();

      text.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent e) {
            String temp = text.getText();

            goToNode(temp);
            String[] words = getWords();
            wordList.setText(words[0] + "\n" + words[1] + "\n"  + words[2] + "\n"  + words[3] + "\n"  + words[4] + "\n"  + words[5] + "\n" 
                                         + words[6] + "\n"  + words[7] + "\n" + words[8] + "\n"  + words[9]);
            
            if (current != null) {
               if (current.getIsWord()) {
                  label.setText(current.getWord() + " is a word!");
               }  
               else {
                  label.setText(temp + " is not a word!");
               }
            }
            else {
                  label.setText(temp + " is not a word!");
            }
         }

         public void keyTyped(KeyEvent e) {}

         public void keyPressed(KeyEvent e) {}

      });

      
      JPanel center = new JPanel();
      JPanel top = new JPanel();
      JPanel bottom = new JPanel();
      JPanel left = new JPanel();
      JPanel right = new JPanel();
      label.setForeground(Color.white);
      label.setText("Type Something!");
      top.add(label);
      center.add(text);
      center.add(wordList);
     

      center.setBackground(Color.lightGray);


      frame.add(center, BorderLayout.CENTER);

      top.setBackground(Color.BLACK);
      frame.add(top, BorderLayout.NORTH);

      bottom.setBackground(Color.BLACK);
      frame.add(bottom, BorderLayout.SOUTH);

      left.setBackground(Color.BLACK);
      frame.add(left, BorderLayout.WEST);

      right.setBackground(Color.BLACK);
      frame.add(right, BorderLayout.EAST);

      center.setLayout(new BoxLayout( center, BoxLayout.PAGE_AXIS));
      frame.setSize(500, 300);
      frame.setVisible(true);

   }

   public static String[] getWords() {
      String[] words = new String[10];
      wordTemp.clear();
      if (current == null) {
         debug("current is null.");
         for (int i = 0; i < 10; i++) {
            words[i] = " ";
         }
      }
      else {
         inorder(current);
         if (wordTemp.size() > 10) {
            for (int i = 0; i < 10; i++) {
               words[i] = wordTemp.get(i);
            }
         }
         else {
            int start = wordTemp.size() - 1;
            for (int i = 0; i < start; i++) {
               words[i] = wordTemp.get(i);
            }
            for (int i = start; i < 10; i++) {
               words[i] = " ";
            }
         }
      }
      return words;
   }

   public static void inorder(Node node) { 
        if (node == null) 
            return; 
        int total = node.getCount(); 

        // All the children except the last 
        for (int i = 0; i < total; i++) {
            inorder(node.getConnections()[i]); 
        }
        // Print the current node's data
        if (node.getIsWord()) { 
           wordTemp.add(node.getWord()); 
           debug("Word added: " + node.getWord());
        }
    }

   public static void goToNode(String path) {
      current = root;
      for (int i = 0; i < path.length(); i++) {
            if (current.getCount() > 0 && current != null) {
               boolean found = false;
               int ind = -1;
               Node[] conn = current.getConnections();
               for (int j = 0; j < current.getCount(); j++) {
                  if (conn[j].getName() == path.charAt(i)) {
                     found = true;
                     ind = j;
                  }
               }
               if (found) {
                  current = conn[ind];
               }
               else {
                  current = null;
                  i = path.length();
               }
            }
            else {
               current = null;
               i = path.length();
            }
      }
   }

   public static void debug(String a) {
      // System.out.println(a);
   }

   public static void say(String a) {
      System.out.println(a);
   }
}