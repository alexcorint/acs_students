package basenostates.requests;

import basenostates.Area;
import basenostates.DirectoryAreas;
import org.json.JSONObject;

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
    Area a = DirectoryAreas.findArea(area);
    jsonTree = a.toJson(1);
  }
}
