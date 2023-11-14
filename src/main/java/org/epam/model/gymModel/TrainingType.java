package org.epam.model.gymModel;

import lombok.Getter;

@Getter
public enum TrainingType {
    BASIC(1, "Basic"),
    CARDIO(2, "Cardio"),
    STRENGTH(3, "Strength"),
    WORKOUT(4, "Workout"),
    YOGA(5, "Yoga"),
    PILATES(6, "Pilates"),
    DANCE(7, "Dance"),
    BOXING(8, "Boxing"),
    BODYBUILDING(9, "Bodybuilding"),
    CROSSFIT(10, "Crossfit"),
    AEROBICS(11, "Aerobics"),
    ZUMBA(12, "Zumba");

    final int id;
    final String name;

    TrainingType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
