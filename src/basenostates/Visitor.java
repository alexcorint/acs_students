package basenostates;

import java.util.ArrayList;

// Visitor interface implementing the Visitor design pattern (Section 6.5).
// By separating traversal operations from the Composite tree classes (Partition and Space),
// new functionalities (finding areas, listing access doors, collecting propped doors)
// can be implemented without modifying the area hierarchy classes.
public interface Visitor {

  void visitSpace(Space s);

  void visitPartition(Partition p);

  Area getArea();

  ArrayList<Door> getDoors();
}
