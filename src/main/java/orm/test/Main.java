package orm.test;

import org.orm.classes.EntityWorker;
import org.orm.classes.SQLWorker;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SQLWorker sqlWorker=new SQLWorker("dfdf");
        EntityWorker entityWorker=new EntityWorker(Car.class,
                sqlWorker);
        EntityWorker entityWorker1=new EntityWorker(Employee.class,
                sqlWorker);

        sqlWorker.InitDB();

        entityWorker.createTableByEntityAnnotation();
        entityWorker1.createTableByEntityAnnotation();
    }
}