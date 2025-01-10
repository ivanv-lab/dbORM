package org.orm.interfaces;

import java.sql.SQLException;

public interface IAnnotationWorker {
    public void createTableByEntityAnnotation() throws SQLException, ClassNotFoundException;

    public void addPKToTable() throws SQLException, ClassNotFoundException;
}
