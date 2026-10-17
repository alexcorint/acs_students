package basenostates.requests;

import basenostates.Area;
import basenostates.DirectoryAreas;
import org.json.JSONObject;

// Request to retrieve the immediate child hierarchy (depth 1) of an area.
// Used by the mobile app client to navigate through the tree of partitions and spaces.
public class RequestChildren implements Request {
  private final String area;
  private JSONObject jsonTree;

  public RequestChildren(String area) {
    this.area = area;
  }

  public String getArea() {
    return area;
  }

  @Override
  public JSONObject answerToJson() {
    return jsonTree;
  }

  @Override
  public String toString() {
    return "RequestChildren {areaId=" + area + "}";
  }

  public void process() {
    Area a = DirectoryAreas.getInstance().findArea(area);
    if (a != null) {
      jsonTree = a.toJson(1);
    } else {
      jsonTree = new JSONObject();
    }
  }
}
