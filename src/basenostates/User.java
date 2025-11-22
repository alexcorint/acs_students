package basenostates;

public class User {
  private final String name;
  private final String credential;
  private final String role;
  private AuthGroup authGroup;

  /**
   * Constructor for User class.
   * @param name User's full name.
   * @param credential User's numeric credential.
   * @param role The name of the role (e.g., "employee", "admin").
   */
  public User(String name, String credential, String role) {
    this.name = name;
    this.credential = credential;
    this.role = role;
  }

  public void setAuthGroup(AuthGroup authGroup) {
    this.authGroup = authGroup;
  }

  public String getCredential() {
    return credential;
  }

  public String getName() {
    return name;
  }

  public String getRole() {
    return role;
  }

  public AuthGroup getAuthGroup() {
    return authGroup;
  }

  @Override
  public String toString() {
    return "User{name=" + name + ", credential=" + credential + "}";
  }
}
