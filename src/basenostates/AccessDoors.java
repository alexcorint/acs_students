package basenostates;

import java.util.ArrayList;

// Concrete visitor that traverses an Area hierarchy (Partition or Space) and collects
// all the access doors belonging to the visited areas.
// Used primarily by RequestArea to translate an area-level request into individual door requests.
public class AccessDoors implements Visitor {
  private final ArrayList<Door> doors;

  public AccessDoors() {
    this.doors = new ArrayList<>();
  }

  @Override
  public void visitSpace(Space s) {
    this.doors.addAll(s.getDoors());
  }

  @Override
  public void visitPartition(Partition p) {
    for (Area a : p.getSpaces()) {
      a.acceptVisitor(this);
    }
  }

  @Override
  public Area getArea() {
    return null;
  }

  @Override
  public ArrayList<Door> getDoors() {
    return doors;
  }
}
