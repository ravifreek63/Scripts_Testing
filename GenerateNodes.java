import java.io.*;
import java.util.Random;

public class GenerateNodes {

public static void generateNodes(String[] args){
 Random rand = new Random();
 String scale = args[0];
 String edgeF = args[1];
 int numPairs = Integer.parseInt(args[2]);
 int NUM_NODES = (int) Math.pow((double)2, (double) Integer.parseInt(scale));
 int startNodeId, endNodeId; 
 int count = 0;
 BufferedWriter bw = null ;
 try{
   String fileName = "nodes_" + scale + "_" + edgeF + ".txt";
   FileWriter fw = new FileWriter(fileName);
   bw = new BufferedWriter(fw);
   while(count < numPairs){
    startNodeId = rand.nextInt(NUM_NODES);
    endNodeId = rand.nextInt(NUM_NODES);
    String content = Integer.toString(startNodeId) + "," + Integer.toString(endNodeId) + "\n";
    bw.write(content);
    count++;
   }
 } catch (IOException e){
    System.out.println(e.toString());
 } finally {
try{
   bw.close();
}catch(IOException e){
 System.out.println(e.toString());
}
}
}
public static void main(String[] args){
   generateNodes(args);
 }
}





