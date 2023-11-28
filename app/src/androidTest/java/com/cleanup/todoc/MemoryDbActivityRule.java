package com.cleanup.todoc;

import androidx.test.rule.ActivityTestRule;

import com.cleanup.todoc.data.database.AppDatabase;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.di.DI;

class MemoryDbActivityRule extends ActivityTestRule<MainActivity> {
    public MemoryDbActivityRule(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        DI.setInMemoryDB(true); // modifier le fameux boolean dans DI
        AppDatabase database = DI.getAppDatabase();
        database.projectDao().insertProject(new Project("Projet Tartampion", 0xFFEADAD1));
        database.projectDao().insertProject(new Project("Projet Lucidia", 0xFFB4CDBA));
        database.projectDao().insertProject(new Project("Projet Circus", 0xFFA3CED2));
        super.beforeActivityLaunched();
    }
}