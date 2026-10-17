package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

// Leaf element of the Composite design pattern for areas.
// A Space represents an individual room, corridor, or hall that contains the doors
// giving access to it (access_doors). It implements acceptVisitor to allow
// operations like searching or listing doors via the Visitor pattern.
public class Space extends Area {
  protected final ArrayList<Door> doors;

  public Space(String id, ArrayList<Door> doors) {
    super(id);
    this.doors = doors;
  }

  public ArrayList<Door> getDoors() {
    return doors;
  }

  @Override
  public void acceptVisitor(Visitor v) {
    v.visitSpace(this);
  }

  @Override
  public JSONObject toJson(int depth) {
    JSONObject obj = new JSONObject();
    JSONArray arr = new JSONArray();

    obj.put("class", "space");
    obj.put("id", this.id);

    for (Door door : this.doors) {
      arr.put(door.toJson());
    }

    obj.put("access_doors", arr);
    return obj;
  }
}
