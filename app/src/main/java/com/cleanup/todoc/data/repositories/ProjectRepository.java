package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.database.entities.Project;

import java.util.List;

public class ProjectRepository {
    private ProjectDao projectDao;
    public ProjectRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }
    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProjects();
    }

}
