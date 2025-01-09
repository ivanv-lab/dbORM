package org.orm;

public class EntityORM {
    private ISQLWorker worker;
    private Entity entity;

    public EntityORM(ISQLWorker worker){
        this.worker=worker;
    }

}
