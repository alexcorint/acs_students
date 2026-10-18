package baseNoStates;

import java.util.ArrayList;

// Composite pattern: an area can be a Space (the leaf) or a Partition (that has other areas
// inside). The permisions of the groups are given over areas, never over doors directly
public abstract class Area {
  protected final String id;

  // the area adds itself to its partition, building has null because its the root
  public Area(String id, Partition partition) {
    this.id = id;
    if (partition != null) {
      partition.addArea(this);
    }
  }

  public String getId() {
    return id;
  }

  public abstract Area findAreaById(String id);

  // the doors to enter in the area (the ones that have as "to" a space of the area)
  public abstract ArrayList<Door> getDoorsGivingAccess();

  // all the spaces inside, to know if a user can be there
  public abstract ArrayList<Space> getSpaces();
}
