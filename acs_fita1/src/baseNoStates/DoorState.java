package baseNoStates;

// State pattern. Each state is a subclass that decides what each action does and if the door
// changes to another state. If later we need more states (do not disturb, idle...) we only
// add a class and Door doesnt change
public abstract class DoorState {
  protected final Door door; // to be able to change the door
  protected String name; // the name we send to the simulator

  public DoorState(Door door) {
    this.door = door;
  }

  public String getName() {
    return name;
  }

  public abstract void open();

  public abstract void close();

  public abstract void lock();

  public abstract void unlock();

  public abstract void unlockShortly();
}
