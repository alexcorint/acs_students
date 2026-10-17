package basenostates;

import java.util.ArrayList;
import java.util.Arrays;

// Defines constants for all valid actions supported by the access control system.
public final class Actions {
  public static final String LOCK = "lock";
  public static final String UNLOCK = "unlock";
  public static final String UNLOCK_SHORTLY = "unlock_shortly";
  public static final String OPEN = "open";
  public static final String CLOSE = "close";

  public static final ArrayList<String> ALL_ACTIONS = new ArrayList<>(Arrays.asList(
      UNLOCK, LOCK, OPEN, CLOSE, UNLOCK_SHORTLY
  ));

  private Actions() {}
}
