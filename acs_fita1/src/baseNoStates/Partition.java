package baseNoStates;

import java.util.ArrayList;

// the "composite" of the pattern, has other areas inside (spaces or partitions), its the tree.
// The methods are recursive, they ask to the areas inside and join what they return
public class Partition extends Area {
  private final ArrayList<Area> areas = new ArrayList<>();

  public Partition(String id, Partition partition) {
    super(id, partition);
  }

  public void addArea(Area area) {
    areas.add(area);
  }

  @Override
  public Area findAreaById(String id) {
    if (this.id.equals(id)) {
      return this;
    }
    for (Area area : areas) {
      Area found = area.findAreaById(id);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    ArrayList<Door> doors = new ArrayList<>();
    for (Area area : areas) {
      doors.addAll(area.getDoorsGivingAccess());
    }
    return doors;
  }

  @Override
  public ArrayList<Space> getSpaces() {
    ArrayList<Space> spaces = new ArrayList<>();
    for (Area area : areas) {
      spaces.addAll(area.getSpaces());
    }
    return spaces;
  }
}
