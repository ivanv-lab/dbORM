package org.orm;

import java.util.List;
import java.util.Map;

public interface ISQLWorker {

    private static void Conn() {}

    static void InitDB(){}

    static void Add(String table, List<String> values){}

    static void Update(String table,int id, List<String> newValues){}

    static void Update(String table,List<String> oldValues,
                              List<String> newValues){}

    static List<Map<String, String>> Read(String table){
        return List.of();
    }

    static void CloseDB(){}
}
