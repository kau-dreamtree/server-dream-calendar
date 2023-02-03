package org.standard.dreamcalendar.domain.schedule.type;

public enum ScheduleProperties {
    ID("id", true),
    USER_ID("id", true),
    TITLE("id", true),
    TAG("id", true),
    IS_ALL_DAY("id", true),
    START_AT("id", false),
    END_AT("id", false);

    private final String id;
    private final boolean isNotNull;

    ScheduleProperties(String id, boolean isNotNull) {
        this.id = id;
        this.isNotNull = isNotNull;
    }
}
