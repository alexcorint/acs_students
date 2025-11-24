package basenostates;

import java.util.ArrayList;

/**
 * AREA FINDER CLASS.
 * Returns an Area by looking for its ID.
 */
public class AreaFinder implements Visitor {
  private Area area;
  private final String id;

  /**
   * Constructor for FindArea class.
   * @param id the ID of the Area.
   */
  public AreaFinder(String id) {
    this.area = null;
    this.id = id;
  }

  /**
   * Returns the Area of a selected ID.
   * @return Area class.
   */
  @Override
  public Area getArea() {
    return this.area;
  }

  /**
   * Returns de doors within an area.
   * @return an ArrayList of Door class.
   */
  @Override
  public ArrayList<Door> getDoors() {
    return null;
  }

  /**
   * Visitor for the space selected.
   * @param s The Space instance to be visited.
   */
  @Override
  public void visitSpace(Space s) {
    if (s.getId().equals(this.id)) {
      this.area = s;
      return;
    }

    for (Door door : s.getDoors()) {
      if (door.getId().equals(this.id)) {
        this.area = s;
        return;
      }
    }
  }

  /**
   * Visitor for the partition selected.
   * @param p The Partition instance to be visited.
   */
  @Override
  public void visitPartition(Partition p) {
    if (p.getId().equals(this.id)) {
      this.area = p;
    } else {
      for (Area area : p.getSpaces()) {
        area.acceptVisitor(this);
        if (this.getArea() != null) {
          break;
        }
      }
    }
  }
}
