package orm.test;

import org.orm.Entity;
import org.orm.EntityORM;
import org.orm.EntityWorker;
import org.orm.SQLWorker;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        Car car=new Car("asdsad",234);
//        EntityWorker ew=new EntityWorker(car.getClass());
//        ew.workWithAnnotation();

        EntityORM orm=new EntityORM(new SQLWorker("dbtest"));

    }
}