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

  private boolean matches(String targetId) {
    if (targetId == null || this.id == null) {
      return false;
    }
    if (targetId.equalsIgnoreCase(this.id)) {
      return true;
    }
    String normTarget = targetId.replace("_", " ").trim();
    String normThis = this.id.replace("_", " ").trim();
    if (normTarget.equalsIgnoreCase(normThis)) {
      return true;
    }
    String stripTarget = targetId.replace("_", "").replace(" ", "").trim();
    String stripThis = this.id.replace("_", "").replace(" ", "").trim();
    return stripTarget.equalsIgnoreCase(stripThis);
  }

  @Override
  public void visitSpace(Space s) {
    if (matches(s.getId())) {
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
    if (matches(p.getId())) {
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
