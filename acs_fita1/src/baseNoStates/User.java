package baseNoStates;

// the user has the permisions of his group
public class User {
  private final String name;
  private final String credential;
  private UserGroup group;

  public User(String name, String credential, UserGroup group) {
    this.name = name;
    this.credential = credential;
    this.group = group;
  }

  public String getName() {
    return name;
  }

  public String getCredential() {
    return credential;
  }

  public UserGroup getGroup() {
    return group;
  }

  // for example to move him to blank
  public void setGroup(UserGroup group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "User{name=" + name + ", credential=" + credential + ", group=" + group.getName() + "}";
  }
}
