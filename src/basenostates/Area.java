package basenostates;

import org.json.JSONObject;

/**
 * AREA CLASS.
 * Base class of a physical area 'that represents a group
 * of partitions that represents a group of spaces'.
 */
public abstract class Area {
  protected final String id;

  /**
   * Constructor for the Area class.
   * @param id the identification string of the area.
   */
  public Area(String id) {
    this.id = id;
  }

  /**
   * Accepts a visitor to process this area.
   * @param v Visitor instance to be accepted.
   */
  public void acceptVisitor(Visitor v) {}

  /**
   * Gets the identification of the area.
   * @return the ID string.
   */
  protected String getId() {
    return this.id;
  }

  /**
   * Converts the area data to a string.
   * @return string representation of the area.
   */
  @Override
  public String toString() {
    return "Area{ id=" + this.id + '\'' + " }";
  }

  /**
   * Converts the area data into a JSON object.
   * @param depth the current depth level for the JSON hierarchy.
   * @return JSON object representing the area.
   */
  public abstract JSONObject toJson(int depth);
}
