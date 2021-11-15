package me.upp.dali.openschedule.model.database.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableUserTime {
    TABLE_NAME("tbl_user_time"),
    ID("userTimeId"),
    PHONE("phone"),
    CODE("userCode"),
    TIME_START("tiempoInicio"),
    TIME_FINISH("tiempoFin");

    private final String value;
}
