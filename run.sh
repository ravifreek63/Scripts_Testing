#!/bin/bash
#javac -classpath "/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-graph-algo-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-kernel-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-primitive-collections-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-graph-matching-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-jmx-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-shell-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/neo4j-udc-2.1.3.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/blueprints-neo4j2-graph-2.5.0.jar:/home/tandon/Neo/neo4j-community-2.1.3/lib/blueprints-neo4j-graph-1.2.jar"  ShortestPath.java 
javac -cp "../lib/*:." ShortestPath.java
java -Xmx3g -cp "../lib/*:." ShortestPath
