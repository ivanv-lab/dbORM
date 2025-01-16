package org.orm.classes;

import org.orm.annotations.Entity;
import org.orm.annotations.Id;
import org.orm.annotations.Query;
import org.orm.interfaces.IAnnotationWorker;
import org.orm.interfaces.ISQLWorker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

public class AnnotationWorker implements IAnnotationWorker {
    Class annotatedClass;
    Method annotatedMethod;
    Entity entityAnnotation;
    Query queryAnnotation;
    String query;
    ISQLWorker worker;
    String tableName;

    public AnnotationWorker(Class annotatedClass, ISQLWorker worker){
        this.annotatedClass=annotatedClass;
        this.worker=worker;
    }

    public AnnotationWorker(Method annotatedMethod){
        this.annotatedMethod=annotatedMethod;
    }

    private String getTableName(){
        entityAnnotation= (Entity) annotatedClass.getAnnotation(Entity.class);
        return tableName=entityAnnotation.tableName();
    }


    public void createTableByEntityAnnotation() throws SQLException, ClassNotFoundException {
        getTableName();
        List<String> sqlFields=new ArrayList<>();
        List<Class> classes=Arrays.stream(annotatedClass.getClasses()).toList();

        //Определение типов и наименований полей
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

        return null;
    }

    public void addPKToTable() throws SQLException, ClassNotFoundException {
        String field=getIdFieldName();
        worker.addPK(tableName,field);
    }

    private String getQuery(){
        queryAnnotation=(Query) annotatedMethod.getAnnotation(Query.class);
        return query=queryAnnotation.queryString();
    }

    /**
     * Доделать
     */
    public void executeQuery(){
        getQuery();


    }
}
