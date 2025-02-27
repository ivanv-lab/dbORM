package org.orm.classes;

import org.orm.interfaces.ISQLWorker;

import java.sql.*;
import java.util.*;

public class SQLWorker implements ISQLWorker {

    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;
    private final String dbName;

    public SQLWorker(String dbName) {
        this.dbName = dbName;
    }

    //Подключение к СУБД
    public void ConnDB() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/"+dbName,
                "postgres",
                "postgres"
        );
        System.out.println("Подключение установлено");
    }

    //Инициализация БД
    public void InitDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/",
                "postgres",
                "postgres"
        );
        System.out.println("Подключение установлено");

        String sql="SELECT datname FROM pg_catalog.pg_database";
        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT datname FROM pg_catalog.pg_database");
        while (resSet.next()){
            if(resSet.getString("datname").equals(dbName)){
                System.out.println("База данных уже существует");
                statmt.executeUpdate("DROP DATABASE "+dbName+";");
                System.out.println("База данных удалена");
                break;
            }
        }

        sql= "CREATE DATABASE "+dbName+";";

        statmt.execute(sql);
        System.out.println("База данных создана");

        CloseDB();
    }

    //Создание таблицы
    public void AddTable(String tableName, List<String> sqlFields) throws SQLException, ClassNotFoundException {
        statmt=conn.createStatement();
        String sql="CREATE TABLE "+tableName+"(";
        for(int i=0;i<sqlFields.size();i++){
            sql=sql.concat(sqlFields.get(i)+", ");
        }
        sql=sql.substring(0,sql.length()-2);
        sql=sql.concat(");");

        statmt.executeUpdate(sql);
    }

    //Добавление записи в таблицу
    public void WriteToDB(String tableName, List<String> values) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+tableName+"');");
        List<String> titles=new ArrayList<>();
        int columnNumber=0;
        while(resSet.next()){
            titles.add(resSet.getString("name"));
            columnNumber++;
        }

        if(columnNumber-1!= values.size()) {
            System.out.println("Количество передаваемых значений больше или меньше количества колонок в таблице!");
            return;
        }

        String sql="INSERT INTO "+tableName+" (";
        for(String column:titles){
            if(column.equals("Id"))
                continue;
            sql=sql.concat(column+",");
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql.concat(")");

        sql=sql.concat(" VALUES (");
        for(String value:values){
            sql=sql.concat("'"+value+"',");
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql.concat(");");

        System.out.println(sql);

        int rowAffected=statmt.executeUpdate(sql);
        if(rowAffected>0)
            System.out.println("Запись добавлена");
        else
            System.out.println("Ошибка");

        CloseDB();
    }

    //Изменение записи в таблице по Id
    public void UpdateToDBById(String tableName,int id, List<String> newValues) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+tableName+"');");
        List<String> titles=new ArrayList<>();
        int columnNumber=0;
        while(resSet.next()){
            titles.add(resSet.getString("name"));
            columnNumber++;
        }

        if(columnNumber-1!= newValues.size()) {
            System.out.println("Количество передаваемых значений больше или меньше количества колонок в таблице!");
            return;
        }

        String sql="UPDATE "+tableName+" SET ";
        for(int i=0;i< newValues.size();i++){
            sql=sql.concat(titles.get(i+1)+"='"+newValues.get(i)+"',");
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql.concat(" Where Id='"+id+"'");

        System.out.println(sql);

        int rowAffected=statmt.executeUpdate(sql);
        if(rowAffected>0)
            System.out.println("Запись обновлена");
        else
            System.out.println("Ошибка");

        CloseDB();
    }

    //Изменение записи в таблице по старым значениям
    public void UpdateToDBByOldValues(String tableName,List<String> oldValues,
                                             List<String> newValues) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+tableName+"');");
        List<String> titles=new ArrayList<>();
        int columnNumber=0;
        while(resSet.next()){
            titles.add(resSet.getString("name"));
            columnNumber++;
        }

        if(columnNumber-1!= newValues.size()) {
            System.out.println("Количество передаваемых значений больше или меньше количества колонок в таблице!");
            return;
        }

        String sql="UPDATE "+tableName+" SET ";
        for(int i=0;i< newValues.size();i++){
            sql=sql.concat(titles.get(i+1)+"='"+newValues.get(i)+"',");
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql.concat(" Where ");

        for(int i=0;i<oldValues.size();i++){
            sql=sql.concat(titles.get(i+1)+"='"+oldValues.get(i)+"' AND ");
        }
        sql=sql.substring(0,sql.length()-4);

        System.out.println(sql);

        int rowAffected=statmt.executeUpdate(sql);
        if(rowAffected>0)
            System.out.println("Запись обновлена");
        else
            System.out.println("Ошибка");

        CloseDB();
    }

    //Чтение всех данных из таблицы
    public List<Map<String, String>> ReadFromDb(String tableName) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+tableName+"');");
        List<String> titles=new ArrayList<>();
        while (resSet.next()){
            if(!resSet.getString("name").equals("Id")) {
                titles.add(resSet.getString("name"));
            }
        }

        String sql="SELECT ";
        for(String column:titles){
            sql=sql.concat(column+",");
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql.concat(" FROM "+tableName);

        System.out.println(sql);

        resSet=statmt.executeQuery(sql);

        List<Map<String,String>> models=new ArrayList<>();
        int c=0;
        while (resSet.next()){
            Map<String,String> columns=new HashMap<>();
            for(int i=0;i< titles.size();i++){
                columns.put(titles.get(i),resSet.getString(titles.get(i)));
            }
            models.add(columns);
        }

        CloseDB();

        return models;
    }

    //Чтение записи по id
    public Map<String, String> readFromBdById(String tableName,int id) throws SQLException, ClassNotFoundException {
        ConnDB();

        CloseDB();

        return Map.of();
    }

    //Удаление записи по id
    public void deleteFromDbById(String tableName,int id) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        statmt.executeQuery("DELETE FROM "+tableName+" WHERE ID="+id+";");

        CloseDB();
    }

    //Добавление первичного ключа для таблицы
    public void addPK(String tableName, String fieldName) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        statmt.executeUpdate("ALTER TABLE "+tableName+" " +
                "ADD PRIMARY KEY "+fieldName+";");

        CloseDB();
    }

    //Закрытие подключения к БД
    public void CloseDB() throws SQLException {
        resSet.close();
        statmt.close();
        conn.close();
    }

    //Выполнение нативного SQL-запроса без возврата значения
    public void executeQueryNonReturn(String queryString) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt=conn.createStatement();
        statmt.executeQuery(queryString);

        CloseDB();
    }

    /**
     * Метод не готов - подумать над тем, как универсально возвращать значения при нативном запросе
     * Думаю может с помощью коллекции String или что то типа
     * @param queryString
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String executeQueryReturnString(String queryString) throws SQLException, ClassNotFoundException {
        ConnDB();

        statmt= conn.createStatement();
        resSet=statmt.executeQuery(queryString);

        CloseDB();
        return null;
    }

    public void addTableRelations(String tableOne,String fieldOne,
                                  String tableTwo,String fieldTwo) throws SQLException, ClassNotFoundException {
        ConnDB();

        String sql="ALTER TABLE "+tableOne+" " +
                "ADD CONSTRAINT "+tableTwo+" FOREIGN KEY ("+fieldOne+") " +
                "REFERENCES "+tableTwo+" ("+fieldTwo+")";
        statmt=conn.createStatement();
        statmt.executeUpdate(sql);

        CloseDB();
    }
}
