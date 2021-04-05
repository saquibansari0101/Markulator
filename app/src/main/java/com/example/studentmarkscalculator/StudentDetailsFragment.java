package com.example.studentmarkscalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentmarkscalculator.integration.R;

import java.text.DecimalFormat;

/**
 * A Fragment that displays a student's marks and allows the user to edit them.
 * @author SaquibAnsari0101
 */
public class StudentDetailsFragment extends Fragment implements View.OnClickListener{

    /**
     * savedInstanceState tag for the student Id.
     */
    public final static String ARG_STUDENT_ID = "studentId";

    /**
     * savedInstanceState tag for the student's first name.
     */
    public final static String ARG_STUDENT_FIRSTNAME = "studentFirstName";

    /**
     * savedInstanceState tag for the student's last name.
     */
    public final static String ARG_STUDENT_LASTNAME = "studentLastName";


    /**
     * The Id of the student to display details for.
     */
    private long studentId = -1;

    /**
     * The student's first name.
     */
    private String studentFirstName;

    /**
     * The student's last name.
     */
    private String studentLastName;

    /**
     * The header at the top of this Fragment that display the student Id and name.
     */
    TextView detailsHeader;


    /**
     * The EditText for the lab mark.
     */
    EditText labMarkField;

    /**
     * The EditText for the midterm mark.
     */
    EditText midtermMarkField;

    /**
     * The EditText for the final exam mark.
     */
    EditText finalExamMarkField;

    /**
     * The TextView for the overallMark.
     */
    TextView overallMarkField;

    /**
     * Stores a bit field of changing configuration parameters on destroy; Used to detect screen rotation.
     */
    private static int oldConfigInt;

    /**
     * Called when this Fragment creates its user interface; Sets field references, OnClickListeners
     * and MarkTextWatchers.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.smc_student_details, container, false);

        if (savedInstanceState != null) {
            studentId = savedInstanceState.getInt(ARG_STUDENT_ID);
            studentFirstName = savedInstanceState.getString(ARG_STUDENT_FIRSTNAME);
            studentLastName = savedInstanceState.getString(ARG_STUDENT_LASTNAME);
            //updateDetailsView(studentId, studentFirstName, studentLastName);
        }

        Button deleteStudentRecordButton = (Button) v.findViewById(R.id.deleteStudentRecordButton);
        deleteStudentRecordButton.setOnClickListener(this);

        Button saveMarksButton = (Button) v.findViewById(R.id.saveMarksButton);
        saveMarksButton.setOnClickListener(this);

        labMarkField = (EditText) v.findViewById(R.id.labMark);
        labMarkField.setFilters(new InputFilter[]{new InputFilterMinMax("0", "30")});
        midtermMarkField = (EditText) v.findViewById(R.id.midtermMark);
        midtermMarkField.setFilters(new InputFilter[]{new InputFilterMinMax("0","30")});
        finalExamMarkField = (EditText) v.findViewById(R.id.finalExamMark);
        finalExamMarkField.setFilters(new InputFilter[]{new InputFilterMinMax("0","40")});
        overallMarkField = (TextView) v.findViewById(R.id.overallMark);

        MarkTextWatcher markTextWatcher = new MarkTextWatcher();
        labMarkField.addTextChangedListener(markTextWatcher);
        midtermMarkField.addTextChangedListener(markTextWatcher);
        finalExamMarkField.addTextChangedListener(markTextWatcher);

        return v;
    }

    /**
     * Called when this Fragment becomes visible to the user; Updates details view if necessary.
     */
    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateDetailsView(args.getLong(ARG_STUDENT_ID), args.getString(ARG_STUDENT_FIRSTNAME), args.getString(ARG_STUDENT_LASTNAME));
        } else if (studentId != -1) {
            updateDetailsView(studentId, studentFirstName, studentLastName);
        }
    }

    /**
     * Called when this Fragment is destroyed; stores a static bit field (int) of changing
     * configuration parameters to detect screen rotation.
     */
    public void onDestroy() {
        super.onDestroy();
        oldConfigInt = getActivity().getChangingConfigurations();
    }

    /**
     * Called when the user clicks on a view; Handles Button presses.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.deleteStudentRecordButton:
                ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().deleteStudentAndMarksById(studentId);
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.saveMarksButton:
                new saveMarksTask().execute();
                break;
        }
    }

    /**
     * Updates the details view with the student data from the database, unless this Fragment was
     * recreated after the screen rotated, in which case it retains the data from the previous
     * instance.
     * @param id
     * @param firstName
     * @param lastName
     */
    public void updateDetailsView(long id, String firstName, String lastName) {

        //easier way of getting student name, but needs to query for the same data a second time
        /*detailsHeader = (TextView) getActivity().findViewById(R.taat_id.detailsHeader);
        Cursor studentInfo = ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().fetchStudentById(taat_id);
        String studentFirstName = studentInfo.getString(studentInfo.getColumnIndex(StudentRecordsDbAdapter.STUDENT_FIRSTNAME));
        String lastName = studentInfo.getString(studentInfo.getColumnIndex(StudentRecordsDbAdapter.STUDENT_LASTNAME));
        detailsHeader.setText(Long.toString(taat_id)+" "+studentFirstName+" "+lastName);*/

        detailsHeader = (TextView) getActivity().findViewById(R.id.header);
        detailsHeader.setText(Long.toString(id)+" "+firstName+" "+lastName);

        if ((oldConfigInt & ActivityInfo.CONFIG_ORIENTATION) != ActivityInfo.CONFIG_ORIENTATION) {
            Cursor marks = ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().fetchMarksByStudentId(id);

            String labMark = marks.getString(marks.getColumnIndex(StudentRecordsDbAdapter.MARK_LAB));
            String midtermMark = marks.getString(marks.getColumnIndex(StudentRecordsDbAdapter.MARK_MIDTERM));
            String finalExamMark = marks.getString(marks.getColumnIndex(StudentRecordsDbAdapter.MARK_FINAL_EXAM));

            labMarkField.setText(labMark);
            midtermMarkField.setText(midtermMark);
            finalExamMarkField.setText(finalExamMark);
            updateOverallMark();
        }

        studentId = id;
    }

    /**
     * Saves the student Id when this Fragment is destroyed.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putLong(ARG_STUDENT_ID, studentId);
    }

    /**
     * Updates the overall mark field.
     */
    private void updateOverallMark() {
        double overallMark = 0;
        for (EditText field : new EditText[] {labMarkField, midtermMarkField, finalExamMarkField}) {
            String content = field.getText().toString();
            try {
                overallMark += Double.parseDouble(content);
            }
            catch (NumberFormatException e) {}
        }

        DecimalFormat numberFormat = new DecimalFormat("#.00");
        overallMarkField.setText(numberFormat.format(overallMark));
    }

    /**
     * Saves the marks in the mark EditTexts to the database.
     * @return
     */
    private int saveMarks() throws NumberFormatException {
        Double[] marks = new Double[3];
        TextView[] fields = new TextView[] {labMarkField, midtermMarkField, finalExamMarkField};
        for (int i = 0; i < fields.length; i++) {
            String fieldText = fields[i].getText().toString();
            if (fieldText==null || fieldText.length()==0) {
                marks[i] = null;
            }
            else {
                try {
                    marks[i] = Double.valueOf(fields[i].getText().toString());
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().updateMarks(studentId, marks[0], marks[1], marks[2]);
    }

    /**
     * Listener class for when the user edits a mark field.
     */
    private class MarkTextWatcher implements TextWatcher {

        /**
         * Called before the text is changed; does nothing but is required.
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        /**
         * Called after the text is changed; does nothing but is required.
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        /**
         * Called after the text is changed; Updates the overall mark field.
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            updateOverallMark();
        }
    }

    /**
     * Runs an update query to save the marks to the database in the background.
     */
    private class saveMarksTask extends AsyncTask<Void, Void, Integer> {

        /**
         * Runs an update query to save the marks to the database in the background.
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(Void... params) {
            return saveMarks();
        }

        /**
         * Displays a message if the update was successful or not.
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) new AlertDialog.Builder(getActivity())
                    .setTitle("Marks saved.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
            else if (result == -1) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Marks not saved.")
                        .setMessage("A mark can be either a decimal number or empty")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }
}
