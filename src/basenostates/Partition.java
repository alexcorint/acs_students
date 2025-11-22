package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * PARTITION CLASS.
 * Defines a group of spaces contained.
 */
public class Partition extends Area {
  private final ArrayList<Area> spaces;

  /**
   * Constructor for the Partition class.
   * @param id     The identification string of the partition.
   * @param spaces The list of spaces contained in this
   *               partition.
   */
  public Partition(String id, ArrayList<Area> spaces) {
    super(id);
    this.spaces = spaces;
  }

  /**
   * Gets the list of spaces in this partition.
   * @return An ArrayList containing the Area objects.
   */
  public ArrayList<Area> getSpaces() {
    return spaces;
  }

  /**
   * Accepts a visitor to process this space.
   * @param v Visitor instance to be accepted.
   */
  public void acceptVisitor(Visitor v) {
    v.visitPartition(this);
  }

  /**
   * Converts the space data into a JSON object.
   * @param depth The current depth level (unused in Space but required by signature).
   * @return JSON object representing the space and its doors.
   */
  public JSONObject toJson(int depth) {
    JSONObject obj = new JSONObject();
    JSONArray arr = new JSONArray();

    obj.put("class", "partition");
    obj.put("id", this.id);

    if (depth > 0) {
      for (Area a : spaces) {
        arr.put(a.toJson(depth - 1));
      }

      obj.put("spaces", arr);
    }

    return obj;
  }
}
