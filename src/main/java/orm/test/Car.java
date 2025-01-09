package orm.test;

import org.orm.annotations.Entity;

@Entity(tableName = "Car")
public class Car {
    private String name;
    private int power;
    private double q;
    private Long w;
    private boolean b;
    private float c;

    public Car(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
