package org.orm.interfaces;

import java.sql.SQLException;

public interface IEntityWorker {
    public void createTableByEntityAnnotation() throws SQLException, ClassNotFoundException;
}
