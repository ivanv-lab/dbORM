package org.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityWorker{
    Class annotatedClass;
    Entity annotation;
    ISQLWorker worker;

    public EntityWorker(Class annotatedClass, ISQLWorker worker){
        this.annotatedClass=annotatedClass;
        this.worker=worker;
    }

    public void workWithAnnotation(){
        annotation= (Entity) annotatedClass.getAnnotation(Entity.class);
        System.out.println(annotation.tableName());
    }
}
