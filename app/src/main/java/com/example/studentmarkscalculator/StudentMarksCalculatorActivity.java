package com.example.studentmarkscalculator;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.studentmarkscalculator.NavigationActionBarActivity;
import com.example.studentmarkscalculator.integration.R;


/**
 * Main Activity for the student marks calculator sub-application.
 * @author SaquibAnsari0101
 */
public class StudentMarksCalculatorActivity extends NavigationActionBarActivity {

    /**
     * Adapter for interacting with an SQL database.
     */
    private StudentRecordsDbAdapter dbHelper;

    /**
     * Called when this Activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smc_student_marks_calculator);

        dbHelper = new StudentRecordsDbAdapter(this);
        dbHelper.open();

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            StudentsListFragment firstFragment = new StudentsListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    /**
     * Called when a student is selected; Opens the StudentDetailsFragment.
     * @param id
     * @param firstName
     * @param lastName
     */
    public void onStudentSelected(long id, String firstName, String lastName) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        StudentDetailsFragment detailsFrag = (StudentDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.details_fragment);

        if (detailsFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            detailsFrag.updateDetailsView(id, firstName, lastName);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            StudentDetailsFragment newFragment = new StudentDetailsFragment();
            Bundle args = new Bundle();
            args.putLong(StudentDetailsFragment.ARG_STUDENT_ID, id);
            args.putString(StudentDetailsFragment.ARG_STUDENT_FIRSTNAME, firstName);
            args.putString(StudentDetailsFragment.ARG_STUDENT_LASTNAME, lastName);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    /**
     * Opens the StudentsSummaryFragment
     */
    public void openSummary() {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        StudentDetailsFragment detailsFrag = (StudentDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.details_fragment);

        if (detailsFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            //insert summary frag

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            StudentsSummaryFragment newFragment = new StudentsSummaryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    /**
     * Returns the database adapter.
     * @return
     */
    public StudentRecordsDbAdapter getDbHelper() {return dbHelper;}
}
