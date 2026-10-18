package baseNoStates;

// Before executing enable assertions :
// https://se-education.org/guides/tutorials/intellijUsefulSettings.html

public class Main {
  public static void main(String[] args) {
    DirectoryAreas.makeAreas();
    DirectoryUsers.makeUsers(); // ojo, despues de makeAreas porque los grupos usan las areas
    Clock.getInstance().start(); // el thread del timer
    new WebServer();
  }
}
