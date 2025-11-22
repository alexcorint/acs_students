package basenostates;

import java.util.ArrayList;

/**
 * VISITOR INTERFACE.
 * Defines the visitor pattern for the areas to visit.
 */
public interface Visitor {

  /**
   * Visits a specific Space node in the area hierarchy.
   * @param s The Space instance to be visited.
   */
  void visitSpace(Space s);

  /**
   * Visits a specific Partition node in the area hierarchy.
   * @param p The Partition instance to be visited.
   */
  void visitPartition(Partition p);

  /**
   * Gets the Area currently being processed or stored by the visitor.
   * @return The Area instance.
   */
  Area getArea();

  /**
   * Retrieves the list of doors found or processed during the visit.
   * @return An ArrayList containing the Door objects.
   */
  ArrayList<Door> getDoors();
}
