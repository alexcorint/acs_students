package basenostates;

import java.util.ArrayList;

// Concrete visitor that traverses the area hierarchy to search for an Area
// matching a specified identifier (or containing a specified door ID).
// This encapsulates the lookup logic outside the area tree classes.
public class AreaFinder implements Visitor {
  private Area area;
  private final String id;

  public AreaFinder(String id) {
    this.area = null;
    this.id = id;
  }

  @Override
  public Area getArea() {
    return this.area;
  }

  @Override
  public ArrayList<Door> getDoors() {
    return null;
  }

  @Override
  public void visitSpace(Space s) {
    if (s.getId().equalsIgnoreCase(this.id)) {
      this.area = s;
      return;
    }

    for (Door door : s.getDoors()) {
      if (door.getId().equalsIgnoreCase(this.id)) {
        this.area = s;
        return;
      }
    }
  }

  @Override
  public void visitPartition(Partition p) {
    if (p.getId().equalsIgnoreCase(this.id)) {
      this.area = p;
    } else {
      for (Area a : p.getSpaces()) {
        a.acceptVisitor(this);
        if (this.getArea() != null) {
          break;
        }
      }
    }
  }
}
