package org.orm.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISQLWorker {
    public void ConnDB() throws ClassNotFoundException, SQLException;

    public void InitDB() throws ClassNotFoundException, SQLException;

    public void AddTable(String tableName, List<String> sqlFields) throws SQLException, ClassNotFoundException;

    public void WriteToDB(String tableName, List<String> values) throws SQLException, ClassNotFoundException;

    public void UpdateToDBById(String tableName,int id, List<String> newValues) throws SQLException, ClassNotFoundException;

    public void UpdateToDBByOldValues(String tableName,List<String> oldValues,
                                      List<String> newValues) throws SQLException, ClassNotFoundException;

    public List<Map<String, String>> ReadFromDb(String tableName) throws SQLException, ClassNotFoundException;

    public Map<String, String> readFromBdById(String tableName,int id) throws SQLException, ClassNotFoundException;

    public void deleteFromDbById(String tableName,int id) throws SQLException, ClassNotFoundException;

    public void addPK(String tableName, String fieldName) throws SQLException, ClassNotFoundException;

    public void executeQueryNonReturn(String queryString) throws SQLException, ClassNotFoundException;

    public String executeQueryReturnString(String queryString) throws SQLException, ClassNotFoundException;

    public void addTableRelations(String tableOne,String fieldOne,
                                  String tableTwo,String fieldTwo) throws SQLException, ClassNotFoundException;

    public void CloseDB() throws SQLException;
}
