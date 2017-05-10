/*import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.ArrayList;

public class LabeledEdges {
    private static final String friend = "friend";
    private static final String enemy = "enemy";

    public static void main(String[] args) {
        DirectedGraph<String, RelationshipEdge> graph =
                new DirectedMultigraph<String, RelationshipEdge>(
                        new ClassBasedEdgeFactory<String, RelationshipEdge>(RelationshipEdge.class));

        ArrayList<String> people = new ArrayList<String>();
        people.add("John");
        people.add("James");
        people.add("Sarah");
        people.add("Jessica");

        // John is everyone's friend
        for (String person : people) {
            graph.addVertex(person);
            graph.addEdge(people.get(0), person, new RelationshipEdge<String>(people.get(0), person, friend));
        }

        // Apparently James doesn't really like John
        graph.addEdge("James", "John", new RelationshipEdge<String>("James", "John", enemy));

        // Jessica is Sarah and James's friend
        graph.addEdge("Jessica", "Sarah", new RelationshipEdge<String>("Jessica", "Sarah", friend));
        graph.addEdge("Jessica", "James", new RelationshipEdge<String>("Jessica", "James", friend));

        // But Sarah doesn't really like James
        graph.addEdge("Sarah", "James", new RelationshipEdge<String>("Sarah", "James", enemy));

        for (RelationshipEdge edge : graph.edgeSet()) {
            if (edge.toString().equals("enemy")) {
                System.out.printf(edge.getV1() + "is an enemy of " + edge.getV2() + "\n");
            } else if (edge.toString().equals("friend")) {
                System.out.printf(edge.getV1() + " is a friend of " + edge.getV2() + "\n");
            }
        }
    }
}
*/