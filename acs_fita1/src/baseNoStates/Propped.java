package baseNoStates;

// propped: the door stayed open after the unlock shortly (here an alarm should sound).
// It can only be closed and then goes back to locked
public class Propped extends DoorState {

  public Propped(Door door) {
    super(door);
    name = States.PROPPED;
  }

  @Override
  public void open() {
    System.out.println("Door " + door.getId() + " is propped, it's already open");
  }

  @Override
  public void close() {
    door.setClosed(true);
    door.setState(new Locked(door));
  }

  @Override
  public void lock() {
    System.out.println("Can't lock door " + door.getId() + " because it's open, close it first");
  }

  @Override
  public void unlock() {
    System.out.println("Door " + door.getId() + " is propped, close it first");
  }

  @Override
  public void unlockShortly() {
    System.out.println("Door " + door.getId() + " is propped, close it first");
  }
}
