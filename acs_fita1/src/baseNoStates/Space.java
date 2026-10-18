package baseNoStates;

import java.util.ArrayList;

// leaf of the composite. Each space has the doors to enter in it (the ones with it as "to"),
// we need them for the area requests
public class Space extends Area {
  private final ArrayList<Door> doorsGivingAccess = new ArrayList<>();

  public Space(String id, Partition partition) {
    super(id, partition);
  }

  // es crida desde el constructor de Door
  public void addDoor(Door door) {
    doorsGivingAccess.add(door);
  }

  @Override
  public Area findAreaById(String id) {
    if (this.id.equals(id)) {
      return this;
    }
    return null;
  }

  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    return doorsGivingAccess;
  }

  @Override
  public ArrayList<Space> getSpaces() {
    // only itself
    ArrayList<Space> spaces = new ArrayList<>();
    spaces.add(this);
    return spaces;
  }
}
