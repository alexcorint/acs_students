package basenostates.requests;

import org.json.JSONObject;

// Command interface defining the contract for all client requests processed by the ACU.
// Implementing classes encapsulate the request parameters and execution logic,
// and serialize the result into a JSON response.
public interface Request {
  JSONObject answerToJson();

  String toString();

  void process();
}
