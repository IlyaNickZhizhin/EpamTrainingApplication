package org.epam.model.gymModel;

import lombok.ToString;

public interface Model {

    String getEntityName();
    void setId(int id);
    int getId();

}
