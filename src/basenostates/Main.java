package basenostates;

// Before executing enable assertions :
// https://se-education.org/guides/tutorials/intellijUsefulSettings.html

public class Main {
  public static void main(String[] args) {
    DirectoryDoors.makeDoors();
    DirectoryAreas.defineAreas(DirectoryDoors.getAllDoors());
    DirectoryUsers.makeUsers();
    new WebServer();
  }
}
