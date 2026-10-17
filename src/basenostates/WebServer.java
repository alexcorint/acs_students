package basenostates;

import basenostates.requests.Request;
import basenostates.requests.RequestArea;
import basenostates.requests.RequestChildren;
import basenostates.requests.RequestReader;
import basenostates.requests.RequestRefresh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// WebServer acts as the facade of the ACU (Access Control Unit). It listens for
// HTTP requests on port 8080 from the simulator or mobile app, instantiates the
// corresponding Request object, executes it and returns the JSON answer.
// Activity is recorded to activity.log via SLF4J and Logback.
public class WebServer {
  private static final int PORT = 8080;
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private static final Logger log = LoggerFactory.getLogger(WebServer.class);

  public WebServer() {
    try {
      ServerSocket serverConnect = new ServerSocket(PORT);
      log.info("Server started. Listening for connections on port : {} ...", PORT);

      while (true) {
        new SocketThread(serverConnect.accept());
      }
    } catch (IOException e) {
      log.error("Server Connection error : {}", e.getMessage());
    }
  }

  private class SocketThread extends Thread {
    private final Socket insocked;

    SocketThread(Socket insocket) {
      this.insocked = insocket;
      this.start();
    }

    @Override
    public void run() {
      BufferedReader in;
      PrintWriter out;
      String resource;

      try {
        in = new BufferedReader(new InputStreamReader(insocked.getInputStream()));
        out = new PrintWriter(insocked.getOutputStream());
        String input = in.readLine();

        if (input == null) {
          insocked.close();
          return;
        }

        log.debug("SocketThread received input : {}", input);

        StringTokenizer parse = new StringTokenizer(input);
        String method = parse.nextToken().toUpperCase();
        if (!method.equals("GET")) {
          log.warn("501 Not Implemented : {} method.", method);
        } else {
          resource = parse.nextToken();
          log.debug("HTTP GET resource: {}", resource);

          parse = new StringTokenizer(resource, "/[?]=&");
          int i = 0;
          String[] tokens = new String[20];
          while (parse.hasMoreTokens()) {
            tokens[i] = parse.nextToken();
            i++;
          }

          Request request = makeRequest(tokens);
          if (request != null) {
            String typeRequest = tokens[0];
            log.debug("Created request: {} ({})", typeRequest, request);
            request.process();
            log.debug("Processed request: {} ({})", typeRequest, request);

            logActivity(request);

            String answer = makeJsonAnswer(request);
            log.debug("Answer: {}", answer);

            out.println(answer);
            out.flush();
          }
        }

        in.close();
        out.close();
        insocked.close();
      } catch (Exception e) {
        log.error("Exception in SocketThread : {}", e.getMessage());
      }
    }

    private void logActivity(Request request) {
      if (request instanceof RequestRefresh) {
        log.info("refresh");
      } else if (request instanceof RequestArea areaReq) {
        log.info("Request area\nuserName {} action {} datetime {}\nareaId {} authorized {}",
            areaReq.getUserName(), areaReq.getAction(), areaReq.getNow(),
            areaReq.getAreaId(), areaReq.isAuthorized());
      } else if (request instanceof RequestReader readerReq) {
        String reasonsStr = readerReq.isAuthorized() ? "" : ", reasons " + readerReq.getReasons();
        log.info("Request reader\nuserName {} action {} datetime {}\ndoorId {} authorized {}{}",
            readerReq.getUserName(), readerReq.getAction(), readerReq.getNow(),
            readerReq.getDoorId(), readerReq.isAuthorized(), reasonsStr);
      } else if (request instanceof RequestChildren childrenReq) {
        log.info("Request children\nareaId {}", childrenReq.getArea());
      }
    }

    private Request makeRequest(String[] tokens) {
      Request request;
      switch (tokens[0]) {
        case "refresh":
          request = new RequestRefresh();
          break;
        case "reader":
          request = makeRequestReader(tokens);
          break;
        case "area":
          request = makeRequestArea(tokens);
          break;
        case "get_children":
          request = makeRequestChildren(tokens);
          break;
        default:
          log.warn("Unknown request: {}", tokens[0]);
          assert false : "unknown request " + tokens[0];
          request = null;
          System.exit(-1);
      }
      return request;
    }

    private RequestReader makeRequestReader(String[] tokens) {
      String credential = tokens[2];
      String action = tokens[4];
      LocalDateTime dateTime = LocalDateTime.parse(tokens[6], formatter);
      String doorId = tokens[8];
      return new RequestReader(credential, action, dateTime, doorId);
    }

    private RequestArea makeRequestArea(String[] tokens) {
      String credential = tokens[2];
      String action = tokens[4];
      LocalDateTime dateTime = LocalDateTime.parse(tokens[6], formatter);
      String areaId = tokens[8];
      return new RequestArea(credential, action, dateTime, areaId);
    }

    private String makeHeaderAnswer() {
      String answer = "";
      answer += "HTTP/1.0 200 OK\r\n";
      answer += "Content-type: application/json\r\n";
      answer += "Access-Control-Allow-Origin: *\r\n";
      answer += "\r\n";
      return answer;
    }

    private String makeJsonAnswer(Request request) {
      String answer = makeHeaderAnswer();
      answer += request.answerToJson().toString();
      return answer;
    }

    private Request makeRequestChildren(String[] tokens) {
      String areaId = tokens[2];
      return new RequestChildren(areaId);
    }
  }
}