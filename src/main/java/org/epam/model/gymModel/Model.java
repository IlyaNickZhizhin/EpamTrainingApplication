package org.epam.model.gymModel;

/**
 * This interface is used to mark all models in the project.
 * @see Trainer
 * @see Trainee
 * @see Training
 * @see TrainingType
 */
public interface Model {

    void setId(int id);
    int getId();

}
