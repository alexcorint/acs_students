package basenostates;

import org.json.JSONObject;

// Component base class of the Composite design pattern for physical areas.
// Represents both leaf nodes (Space) and composite nodes (Partition).
// Allows the client to treat individual spaces and grouped partitions uniformly.
// It also serves as the Element interface for the Visitor design pattern.
public abstract class Area {
  protected final String id;

  public Area(String id) {
    this.id = id;
  }

  public abstract void acceptVisitor(Visitor v);

  public String getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "Area{id='" + this.id + "'}";
  }

  public abstract JSONObject toJson(int depth);
}
