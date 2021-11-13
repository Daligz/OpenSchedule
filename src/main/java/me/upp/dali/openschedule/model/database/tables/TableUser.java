package me.upp.dali.openschedule.model.database.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableUser {
    TABLE_NAME("tbl_user"),
    ID("userId"),
    NAME("name"),
    PHONE("phone");

    private final String value;
}
