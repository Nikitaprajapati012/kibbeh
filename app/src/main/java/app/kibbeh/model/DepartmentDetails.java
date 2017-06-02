package app.kibbeh.model;

/**
 * Created by Ravi Archi on 12/21/2016.
 */
public class DepartmentDetails {

    public String department,departmentID,storeID;

    public DepartmentDetails(){}
    public DepartmentDetails(String department, String deparmentID, String storID) {

        this.department = department;
        this.departmentID = deparmentID;
        this.storeID = storID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    @Override
    public String toString() {
       /* return "DepartmentDetails{" +
                "department='" + department + '\'' +
                '}';*/
        return department;
    }
}
