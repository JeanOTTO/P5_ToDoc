package com.cleanup.todoc.data.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskWithProject {
    @Embedded
    public Task task;

    @Relation(parentColumn = "projectId", entityColumn = "id")
    public Project project;
}