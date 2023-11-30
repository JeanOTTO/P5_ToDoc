package com.cleanup.todoc.data.database;

public enum ProjectsName {
    PROJET_1("Tartampion", 0xFFEADAD1),
    PROJET_2("Lucidia", 0xFFB4CDBA),
    PROJET_3("Circus", 0xFFA3CED2);

    private final String name;
    private final int color;
    ProjectsName(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public int getColor() {
        return color;
    }
}
