package org.orm;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLWorker implements ISQLWorker {

    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;
    private final String dbName;

    public SQLWorker(String dbName) {
        this.dbName = dbName;
    }

    //Подключение к СУБД
    /// Сделать так, чтобы сначала было подключение к конкретной БД,
    /// иначе подключение к СУБД без конкретной БД и использовался бы метод InitDB(),
    /// т.к. сейчас получается так, что повторное использование метода Conn()
    /// ведет к подключению к СУБД, а не к БД
    private void ConnDB() throws ClassNotFoundException, SQLException
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
                System.out.println("База данных перезаписана");
                return;
            }
        }

        sql= "CREATE DATABASE "+dbName+";";

        statmt.execute(sql);
        System.out.println("База данных создана");

        CloseDB();
    }

    //Создание таблицы
    public void AddTable(String dbName,String tableName) throws SQLException, ClassNotFoundException {
        ConnDB();

        /// Добавить автоопределение ID как первичного ключа,
        /// автоопределение типов полей

        CloseDB();
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

    //Закрытие подключения к БД
    private void CloseDB() throws SQLException {
        resSet.close();
        statmt.close();
        conn.close();
    }
}
