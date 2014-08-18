import org.neo4j.graphalgo.GraphAlgoFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Date;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import org.neo4j.graphdb.Path;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PathExpanders;
import java.util.Iterator;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

/* Argument 0 is the scale and argument 1 is edge factor, argument 2 is the number of queries.
 * */

public class ShortestPath {
  private static  String DB_PATH ;//= "/home/tandon/Neo/neo4j-community-2.1.3/scripts/databases/toy_20_8.db";
  private static final int MAX_PATH=4;
  private static PathFinder<Path> Path_Finder;  
  private static int NUM_NODES = 0;
  private static int NUM_QUERIES = 0;
  private static int[][] Nodes;//new int[NUM_QUERIES][2];
  private static int SCALE = 0;
  private static int EDGE_FACTOR = 0;
  private static String arrayFileName = "";
 
  public static void init(String[] args){
    String SCALE_Str = args[0];
    String EDGE_FACTOR_Str = args[1];
    SCALE = Integer.parseInt(SCALE_Str);
    EDGE_FACTOR = Integer.parseInt(EDGE_FACTOR_Str);
    arrayFileName = "nodes_" + SCALE_Str + "_"+ EDGE_FACTOR_Str +".txt"; 
    NUM_NODES = (int) Math.pow((double) 2, (double)SCALE);
    NUM_QUERIES = Integer.parseInt(args[2]);
    DB_PATH = "/home/tandon/Neo/neo4j-community-2.1.3/scripts/databases/toy_" + SCALE_Str + "_" + EDGE_FACTOR_Str + ".db";
    Nodes = new int[NUM_QUERIES][2];
  }

  private static Node getNode(GraphDatabaseService graph, int id){
  Transaction tx = graph.beginTx();
  Node node = graph.getNodeById(id);
  tx.success();
  return node;
  }
 
 public static void loadArray(){
   BufferedReader br = null; int count = 0;
   try{ 
     File file = new File(arrayFileName);
     br = new BufferedReader(new FileReader(file));
     String thisLine = null;
     while ((thisLine = br.readLine()) != null) {
       count++;
       if(count > NUM_QUERIES)
          break;
       String[] parts = thisLine.split(",");
       Nodes[count][0] = Integer.parseInt(parts[0]);
       Nodes[count][1] = Integer.parseInt(parts[1]); 
     }
     br.close();
   } catch (IOException e){
     System.out.println(e.toString());
   }finally{
    try {
       br.close();
    } catch (IOException e){
       System.out.println(e.toString());
      }
    }
 }
  
 public static void loadGraph(GraphDatabaseService graphDb){
    System.out.println("Loading the database in memory ....");
    int count = 0;
    Transaction tx = graphDb.beginTx();
    Iterable<Node> nodes = GlobalGraphOperations.at(graphDb).getAllNodes();
    tx.success();
    for(Node node : nodes){
       tx = graphDb.beginTx();
         count += IteratorUtil.count(node.getRelationships());
       tx.success();
    }
    System.out.println("Count = " + count);  
    System.out.println("Loaded the database in memory ...");
  }   

  public static void runQueries(GraphDatabaseService graphDb){
   Node startNode, endNode;
   System.out.println("Running Queries Now  .....");
   Random rand = new Random();
   int numberQueries = 0, startNodeId, endNodeId;
   long totalTime = 0, nPaths = 0, nEdges = 0;
      long lStartTime = new Date().getTime();
   try{
   FileWriter fw = new FileWriter("nodes_21_4.txt");
   BufferedWriter bw = new BufferedWriter(fw);
   while (numberQueries < NUM_QUERIES){
      startNodeId = rand.nextInt(NUM_NODES);
      endNodeId = rand.nextInt(NUM_NODES);
      Nodes[numberQueries][0] = startNodeId; 
      Nodes[numberQueries][1] = endNodeId;
      String content = Integer.toString(startNodeId) + "," + Integer.toString(endNodeId) + "\n";
      bw.write(content);
      startNode = getNode(graphDb, startNodeId);
      endNode = getNode(graphDb, endNodeId);
      Iterable<Path> paths = Path_Finder.findAllPaths(startNode, endNode);
      for(Path p: paths)
        nEdges += p.length();
      //totalTime += difference;
      System.out.println("Query completed....." + numberQueries);// Time taken = " + difference + " milliseconds. StartNode, EndNode" + startNodeId +"," + endNodeId);
      nPaths += IteratorUtil.count(paths);
     
      //System.out.println("Number of paths :" + nPaths);
     // if (numberQueries % 100 == 0){
          
       //   System.out.println("Query Rate = " + );
     // }
      numberQueries++;
   }
      long lEndTime = new Date().getTime();
      long difference = lEndTime - lStartTime;
      System.out.println("TotalTime = " + difference);
      System.out.println("TotalPaths = " + nPaths);
      System.out.println("TotalEdges = " + nEdges);
      bw.close();
    } catch(IOException e){
      System.out.println(e.toString());
    }
  }

  public static void main(String[] args){
        init(args);
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
       // long lStartTime = new Date().getTime();
        //loadGraph(graphDb);
       // long lEndTime = new Date().getTime();
       // long difference = lEndTime - lStartTime;
       // System.out.println("Time taken to load the database = " + difference/1000 + " seconds.");
        loadArray();
        Path_Finder = GraphAlgoFactory.allPaths(PathExpanders.allTypesAndDirections(), MAX_PATH);      
        runQueries(graphDb);
        graphDb.shutdown();
        System.out.println("ShutDown DB Done");
  }
} 
