package com.lunev2k.schedule.di;

import com.lunev2k.schedule.activities.AddLearnerActivity;
import com.lunev2k.schedule.activities.AddLessonActivity;
import com.lunev2k.schedule.activities.DayActivity;
import com.lunev2k.schedule.activities.EditLearnerActivity;
import com.lunev2k.schedule.activities.MainActivity;
import com.lunev2k.schedule.activities.MoveLessonActivity;
import com.lunev2k.schedule.activities.RangeDateActivity;
import com.lunev2k.schedule.activities.ViewLearnerActivity;
import com.lunev2k.schedule.activities.ViewLessonActivity;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.fragments.LearnersFragment;
import com.lunev2k.schedule.fragments.LessonsFragment;
import com.lunev2k.schedule.fragments.TotalsFragment;
import com.lunev2k.schedule.fragments.dialogs.ChoiceLearnerFragment;
import com.lunev2k.schedule.utils.PrefsUtils;
import com.lunev2k.schedule.utils.RangeDateUtil;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(PrefsUtils prefsUtils);

    void inject(RangeDateUtil rangeDateUtil);

    void inject(DatabaseRepository databaseRepository);

    void inject(MainActivity mainActivity);

    void inject(RangeDateActivity rangeDateActivity);

    void inject(AddLearnerActivity addLearnerActivity);

    void inject(AddLessonActivity addLessonActivity);

    void inject(ViewLessonActivity viewLessonActivity);

    void inject(ViewLearnerActivity viewLearnerActivity);

    void inject(EditLearnerActivity editLearnerActivity);

    void inject(LearnersFragment learnersFragment);

    void inject(LessonsFragment lessonsFragment);

    void inject(TotalsFragment totalsFragment);

    void inject(ChoiceLearnerFragment choiceLearnerFragment);

    void inject(MoveLessonActivity moveLessonActivity);

    void inject(DayActivity dayActivity);
}
