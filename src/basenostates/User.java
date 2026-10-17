package basenostates;

// Represents an individual user in the access control system.
// Following the Role-Based Access Control (RBAC) model, permissions are not assigned
// directly to users, but acquired through an associated AuthGroup corresponding to their role.
public class User {
  private final String name;
  private final String credential;
  private final String role;
  private AuthGroup authGroup;

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
