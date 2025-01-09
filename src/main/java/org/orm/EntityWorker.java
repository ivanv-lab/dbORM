package org.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityWorker{
    Class annotatedClass;
    Entity annotation;

    public EntityWorker(Class annotatedClass){
        this.annotatedClass=annotatedClass;
    }

    public void workWithAnnotation(){
        annotation= (Entity) annotatedClass.getAnnotation(Entity.class);

    }
}
