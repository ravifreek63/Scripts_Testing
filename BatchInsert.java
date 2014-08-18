import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BatchInsert{

private static final String DB_PATH = "./databases/toy_21_4.db";
private static final String DATA_PATH = "/home/tandon/data/toy_21_4.txt";
private static final int NUM_NODES = 2097152;
private GraphDatabaseService _graphDb;
private static final int batchSize = 10000;
private static RelationshipType connection = DynamicRelationshipType.withName("CONNECTED_TO");

public GraphDatabaseService getGraphDB(){
      return _graphDb;
}

public void setGraphDB(GraphDatabaseService g){ _graphDb = g; }

public void createNodes(){
  Node node;
  Label label = DynamicLabel.label( "Node" );
  for(int count = 0; count < NUM_NODES; count++){
    node = _graphDb.createNode();
    node.setProperty("id", count);
    node.addLabel(label);
  }
}

public void createRelationBetween(Node from, Node to){
   from.createRelationshipTo(to, connection);
}

public void createRelationShips(){
BufferedReader br = null; 
try {
    File file = new File(DATA_PATH);
    br = new BufferedReader(new FileReader(file));
    Node from, to;
    String  thisLine = null;
    while ((thisLine = br.readLine()) != null) {
      String[] parts = thisLine.split("\\s+");
        from = _graphDb.getNodeById(Long.parseLong(parts[1]));
        to = _graphDb.getNodeById(Long.parseLong(parts[2]));           
        createRelationBetween(from, to);
   }
} catch(IOException e){
  e.printStackTrace();
}finally {
    try {
        br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

public void batchDb(){
        // START SNIPPET: batchDb
GraphDatabaseService batchDb =
                BatchInserters.batchDatabase( "databases/toy_21_4.db");
        setGraphDB(batchDb);
        createNodes();
        createRelationShips();
        batchDb.shutdown();
    }
    
 /*   public void batchDbWithConfig()
    {
        // START SNIPPET: configuredBatchDb
        Map<String, String> config = new HashMap<>();
        config.put( "neostore.nodestore.db.mapped_memory", "90M" );
        GraphDatabaseService batchDb =
                BatchInserters.batchDatabase( "target/batchdb-example-config", fileSystem, config );
        // Insert data here ... and then shut down:
        batchDb.shutdown();
        // END SNIPPET: configuredBatchDb
    }*/

public BatchInsert(){
   batchDb();
}

public static void main(String[] args){
   BatchInsert batchInsert = new BatchInsert();    
}

}
