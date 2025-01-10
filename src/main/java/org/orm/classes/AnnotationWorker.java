package org.orm.classes;

import org.orm.annotations.Entity;
import org.orm.annotations.Id;
import org.orm.annotations.Query;
import org.orm.interfaces.IAnnotationWorker;
import org.orm.interfaces.ISQLWorker;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

public class AnnotationWorker implements IAnnotationWorker {
    Class annotatedClass;
    Entity entityAnnotation;
    Id idAnnotation;
    Query queryAnnotation;
    ISQLWorker worker;
    String tableName;

    public AnnotationWorker(Class annotatedClass, ISQLWorker worker){
        this.annotatedClass=annotatedClass;
        this.worker=worker;
    }

    private String getTableName(){
        entityAnnotation= (Entity) annotatedClass.getAnnotation(Entity.class);
        return tableName=entityAnnotation.tableName();
    }


    public void createTableByEntityAnnotation() throws SQLException, ClassNotFoundException {
        getTableName();
        List<String> sqlFields=new ArrayList<>();

        List<Field> fields= Arrays.stream(annotatedClass.getDeclaredFields()).toList();
        for(Field f:fields){
            String name=f.getName();
            String type= String.valueOf(f.getType());

            if(type.contains("String")) sqlFields.add(name+" CHARACTER VARYING(255)");
            if(type.contains("int")) sqlFields.add(name+" INTEGER");
            if(type.contains("double")) sqlFields.add(name+" REAL");
            if(type.contains("Long")) sqlFields.add(name+" BIGINT");
            if(type.contains("boolean")) sqlFields.add(name+" BOOLEAN");
            if(type.contains("float")) sqlFields.add(name+" DECIMAL");
        }

        worker.ConnDB();
        worker.AddTable(tableName,sqlFields);
        worker.CloseDB();
    }

    private String getIdFieldName(){
        List<Field> fields= Arrays.stream(annotatedClass.getDeclaredFields()).toList();
        for(Field f:fields){
            f.setAccessible(true);
            if(f.isAnnotationPresent(Id.class))
                return f.getName();
        }
    }

    public void addPKToTable(){

    }


}
