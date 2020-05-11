public class Node {
   private Node[] connections;
   private boolean isWord; 
   private int count;
   private String word;
   private char name;
   
   public Node(char a) {
      connections = new Node[26];
      isWord = false;
      word = "";
      name =  a;
      count = 0;
   }

   public Node[] getConnections() {
      return connections;
   }

   public boolean getIsWord() {
      return isWord;
   }

   public String getWord() {
      return word;
   }

   public char getName() {
      return name;
   }

   public int getCount() {
      return count;
   }

   public void addConnection(Node a) {
       connections[count] = a;
       count++;
      
   }

   public void isWord() {
      isWord = true;
   }

   public void setWord(String a) {
      word = a;
   }
}