package org.orm;

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

    public void CloseDB() throws SQLException;
}
