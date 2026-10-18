package baseNoStates;

// locked: closed and nobody can open it until unlock or unlock shortly
public class Locked extends DoorState {

  public Locked(Door door) {
    super(door);
    name = States.LOCKED;
  }

  @Override
  public void open() {
    System.out.println("Can't open door " + door.getId() + " because it's locked");
  }

  @Override
  public void close() {
    // a locked door is allways closed
    System.out.println("Door " + door.getId() + " is locked, it's already closed");
  }

  @Override
  public void lock() {
    System.out.println("Door " + door.getId() + " is already locked");
  }

  @Override
  public void unlock() {
    door.setState(new Unlocked(door));
  }

  @Override
  public void unlockShortly() {
    door.setState(new UnlockedShortly(door));
  }
}
