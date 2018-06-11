package camarena.daniel.capstone.fms;

public class Employees
{
    private String position, email, name, phonenumber;

    public Employees() { }

    public Employees(String position, String email, String name, String phonenumber)
    {
        this.position = position;
        this.email = email;
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
