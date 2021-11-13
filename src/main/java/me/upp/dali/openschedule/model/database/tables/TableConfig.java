package me.upp.dali.openschedule.model.database.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableConfig {
    TABLE_NAME("tbl_config"),
    TABLE_ID("configId"),
    ID("id"),
    VALUE("value");

    private final String value;
}
