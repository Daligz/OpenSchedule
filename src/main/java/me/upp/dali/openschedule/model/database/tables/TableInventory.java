package me.upp.dali.openschedule.model.database.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableInventory {
    TABLE_NAME("tbl_inventory"),
    ID("inventoryId"),
    NAME("name"),
    ADMISSION_DATE("admissionDate"),
    STATE("state"),
    COST("cost");

    private final String value;
}
