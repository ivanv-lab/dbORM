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

    private static void Conn() throws ClassNotFoundException, SQLException
    {
        conn=null;
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/",
                "postgres",
                "postgres"
        );
        System.out.println("Подключение установлено");
    }

    public static void InitDB(String dbName) throws SQLException, ClassNotFoundException {
        Conn();

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
        statmt.close();
        conn.close();
    }

    public static void WriteToDB(String table, List<String> values) throws SQLException
    {
        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+table+"');");
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

        String sql="INSERT INTO "+table+" (";
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
    }

    public static void UpdateToDBById(String table,int id, List<String> newValues) throws SQLException {
        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+table+"');");
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

        String sql="UPDATE "+table+" SET ";
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
    }

    public static void UpdateToDBByOldValues(String table,List<String> oldValues,
                                             List<String> newValues) throws SQLException {
        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+table+"');");
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

        String sql="UPDATE "+table+" SET ";
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
    }

    public static List<Map<String, String>> ReadFromDb(String table) throws SQLException {
        statmt=conn.createStatement();
        resSet=statmt.executeQuery("SELECT name FROM PRAGMA_TABLE_INFO('"+table+"');");
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
        sql=sql.concat(" FROM "+table);

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

        resSet.close();
        statmt.close();
        conn.close();

        return models;
    }

    public static void CloseDB() throws SQLException {
        resSet.close();
        statmt.close();
        conn.close();
    }
}
