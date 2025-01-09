package org.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityWorker{
    Class annotatedClass;
    Entity annotation;
    ISQLWorker worker;
    String tableName;

    public EntityWorker(Class annotatedClass, ISQLWorker worker){
        this.annotatedClass=annotatedClass;
        this.worker=worker;
    }

    private String getTableName(){
        annotation= (Entity) annotatedClass.getAnnotation(Entity.class);
        return tableName=annotation.tableName();
    }

    public void createTableByEntityAnnotation(){

    }
}
