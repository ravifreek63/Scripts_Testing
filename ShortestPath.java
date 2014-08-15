import org.neo4j.graphalgo.GraphAlgoFactory;
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

public class ShortestPath {
  private static final String DB_PATH = "/home/tandon/Neo/neo4j-community-2.1.3/scripts/databases/roadNet-CA.db";
  private static final int MAX_PATH=10;
  private static PathFinder<Path> Path_Finder;  
  private static int NUM_NODES = 1000000;

  private static Node getNode(GraphDatabaseService graph, int id){
  Transaction tx = graph.beginTx();
  Node node = graph.getNodeById(id);
  tx.success();
  return node;
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
    System.out.println("Loaded the database in memory .... \n\n\n");
  }   

  public static void runQueries(GraphDatabaseService graphDb){
   Node startNode, endNode;
   System.out.println("Running Queries Now  .....");
   Random rand = new Random();
   int numberQueries = 0, startNodeId, endNodeId;
   long totalTime = 0, nPaths = 0;
      long lStartTime = new Date().getTime();
   while (numberQueries < 1000){
      startNodeId = rand.nextInt(NUM_NODES);
      endNodeId = rand.nextInt(NUM_NODES);
      startNode = getNode(graphDb, startNodeId);
      endNode = getNode(graphDb, endNodeId);
      Iterable<Path> paths = Path_Finder.findAllPaths(startNode, endNode);
      //totalTime += difference;
      //System.out.println("Query completed..... Time taken = " + difference + " milliseconds. StartNode, EndNode" + startNodeId +"," + endNodeId);
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
  }

  public static void main(String[] args){
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        long lStartTime = new Date().getTime();
        loadGraph(graphDb);
        long lEndTime = new Date().getTime();
        long difference = lEndTime - lStartTime;
        System.out.println("Time taken to load the database = " + difference/1000 + " milliseconds.");
        Path_Finder = GraphAlgoFactory.allPaths(PathExpanders.allTypesAndDirections(), MAX_PATH);      
        runQueries(graphDb);
        graphDb.shutdown();
        System.out.println("ShutDown DB Done");
  }
} 
