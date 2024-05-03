public class User {
  private int id;
  private String name;
  private String password;

  public User(int id, String name, String password) {
    this.id = id;
    this.name = name.trim();
    this.password = password.trim();
  }

  public User(int id, String name) {
    this.id = id;
    this.name = name.trim();

  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String toString() {
    return id + "\t" + name + "\t" + password;
  }

  public String toCSVString() {
    return id + ",\t" + name + ",\t" + password;
  }
}
