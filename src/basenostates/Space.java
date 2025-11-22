package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * SPACE AREA.
 * Defines a group of doors contained.
 */
public class Space extends Area {
  protected final ArrayList<Door> doors;

  /**
   * Constructor for the Space class.
   * @param id    The identification string of the space.
   * @param doors The list of doors contained in this space.
   */
  public Space(String id, ArrayList<Door> doors) {
    super(id);
    this.doors = doors;
  }

  /**
   * Gets the list of doors in this space.
   * @return An ArrayList containing the Door objects.
   */
  public ArrayList<Door> getDoors() {
    return doors;
  }

  /**
   * Accepts a visitor to process this space.
   * @param v Visitor instance to be accepted.
   */
  @Override
  public void acceptVisitor(Visitor v) {
    v.visitSpace(this);
  }

  /**
   * Converts the space data into a JSON object.
   * @param depth The current depth level (unused in Space but required by signature).
   * @return JSON object representing the space and its doors.
   */
  public JSONObject toJson(int depth) {
    JSONObject obj = new JSONObject();
    JSONArray arr = new JSONArray();

    obj.put("class", "space");
    obj.put("id", this.id);

    for (Door door : this.doors) {
      arr.put(door.toJson());
    }

    obj.put("doors", arr);
    return obj;
  }
}
