package basenostates;

import java.util.ArrayList;

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
