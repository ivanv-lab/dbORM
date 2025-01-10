package orm.test;

import org.orm.classes.AnnotationWorker;
import org.orm.classes.SQLWorker;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SQLWorker sqlWorker=new SQLWorker("dfdf");
        AnnotationWorker entityWorker=new AnnotationWorker(Car.class,
                sqlWorker);
        AnnotationWorker entityWorker1=new AnnotationWorker(Employee.class,
                sqlWorker);

        sqlWorker.InitDB();

        entityWorker.createTableByEntityAnnotation();
        entityWorker1.createTableByEntityAnnotation();
    }
}