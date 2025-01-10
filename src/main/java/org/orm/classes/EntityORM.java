package org.orm.classes;

import org.orm.interfaces.IAnnotationWorker;
import org.orm.interfaces.ISQLWorker;

import java.sql.SQLException;
import java.util.List;

public class EntityORM {
    private ISQLWorker sqlWorker;
    private IAnnotationWorker entityWorker;

    public EntityORM(ISQLWorker sqlWorker, IAnnotationWorker entityWorker){
        this.sqlWorker=sqlWorker;
        this.entityWorker=entityWorker;
    }

    //Создание БД с моделированием классов в сущности. В случае повторноо создания затирает базу
    public void createDatabase(String databaseName) throws SQLException, ClassNotFoundException {
        sqlWorker.InitDB();
    }

    //
    public void createTable(String tableName, List<String> fields) throws SQLException, ClassNotFoundException {
        sqlWorker.AddTable(tableName, fields);
    }

    public void createRecord(String tableName, List<String> fields) throws SQLException, ClassNotFoundException {
        sqlWorker.WriteToDB(tableName, fields);
    }

    public void readAllTable(String tableName) throws SQLException, ClassNotFoundException {
        sqlWorker.ReadFromDb(tableName);
    }

    public void readRecordById(String tableName, int id) throws SQLException, ClassNotFoundException {
        sqlWorker.readFromBdById(tableName, id);
    }

    public void updateRecordById(String tableName, int id, List<String> newFields) throws SQLException, ClassNotFoundException {
        sqlWorker.UpdateToDBById(tableName,id,newFields);
    }

    public void updateRecordByFields(String tableName, List<String> oldValues, List<String> newValues) throws SQLException, ClassNotFoundException {
        sqlWorker.UpdateToDBByOldValues(tableName,oldValues,newValues);
    }

    public void deleteRecordById(String tableName, int id) throws SQLException, ClassNotFoundException {
        sqlWorker.deleteFromDbById(tableName, id);
    }
}
