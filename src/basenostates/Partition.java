package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

// Composite node of the Composite design pattern for areas.
// A Partition represents a higher-level grouping of areas (spaces or sub-partitions),
// such as a floor or an entire building. It enables granting access privileges
// or performing bulk operations (like locking all doors in a partition) uniformly.
public class Partition extends Area {
  private final ArrayList<Area> spaces;

  public Partition(String id, ArrayList<Area> spaces) {
    super(id);
    this.spaces = spaces;
  }

  public ArrayList<Area> getSpaces() {
    return spaces;
  }

  @Override
  public void acceptVisitor(Visitor v) {
    v.visitPartition(this);
  }

  @Override
  public JSONObject toJson(int depth) {
    JSONObject obj = new JSONObject();
    JSONArray arr = new JSONArray();

    obj.put("class", "partition");
    obj.put("id", this.id);

    if (depth > 0) {
      for (Area a : spaces) {
        arr.put(a.toJson(depth - 1));
      }
      obj.put("areas", arr);
    }

    return obj;
  }
}
