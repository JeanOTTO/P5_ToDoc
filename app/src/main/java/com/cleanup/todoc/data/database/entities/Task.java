package com.cleanup.todoc.data.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "tasks", foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "projectId",
        onDelete = ForeignKey.CASCADE))
//FOREIGN KEY
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long projectId;
    private String name;
    private Date createdAt;
    private int colorProject;

    public Task(long projectId, String name, Date createdAt) {
        this.projectId = projectId;
        this.name = name;
        this.createdAt = createdAt;
    }


    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColorProject() {
        return colorProject;
    }

    public void setColorProject(int colorProject) {
        this.colorProject = colorProject;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task otherTask = (Task) obj;
        return id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}