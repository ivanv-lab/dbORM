package org.orm.classes;

import org.orm.interfaces.IEntityWorker;
import org.orm.interfaces.ISQLWorker;

import java.util.List;

public class EntityORM {
    private ISQLWorker sqlWorker;
    private IEntityWorker entityWorker;

    public EntityORM(ISQLWorker sqlWorker, IEntityWorker entityWorker){
        this.sqlWorker=sqlWorker;
        this.entityWorker=entityWorker;
    }

    public void createDatabase(String databaseName){

    }

    public void createTable(String tableName, List<String> fields){

    }

    public void createRecord(){

    }

    public void readAllTable(){

    }

    public void readRecord(){

    }

    public void updateRecordById(){

    }

    public void updateRecordByFields(){

    }

    public void deleteRecord(){

    }
}
