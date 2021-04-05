package com.example.studentmarkscalculator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.studentmarkscalculator.integration.R;

import java.text.DecimalFormat;

/**
 * A Fragment that displays teh average marks of all students.
 * @author SaquibAnsari0101
 */
public class StudentsSummaryFragment extends Fragment {

    /**
     * Called to have this Fragment create its user interface; Sets the layout.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.smc_student_details, container, false);
//        Button deleteAllRecordButton = (Button) v.findViewById(R.id.deleteAllRecordButton);
//        deleteAllRecordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Jesus(view);
//            }
//        });
        return inflater.inflate(R.layout.smc_students_summary, container, false);
    }

    /**
     * Called when this Fragment beoomes visible to the user; Displays summary information from the database.
     */
    @Override
    public void onStart() {
        super.onStart();

        Cursor marks = ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().fetchAverageMarks();

        TextView labMarkField = (TextView) getActivity().findViewById(R.id.averageLabMark);
        TextView midtermMarkField = (TextView) getActivity().findViewById(R.id.averageMidtermMark);
        TextView finalExamMarkField = (TextView) getActivity().findViewById(R.id.averageFinalExamMark);
        TextView overallMarkField = (TextView) getActivity().findViewById(R.id.averageOverallMark);

        double labMark = marks.getDouble(marks.getColumnIndex(StudentRecordsDbAdapter.AVG_MARK_LAB));
        double midtermMark = marks.getDouble(marks.getColumnIndex(StudentRecordsDbAdapter.AVG_MARK_MIDTERM));
        double finalExamMark = marks.getDouble(marks.getColumnIndex(StudentRecordsDbAdapter.AVG_MARK_FINAL_EXAM));
        double overallMark = labMark + midtermMark + finalExamMark;

        DecimalFormat numberFormat = new DecimalFormat("#.00");

        labMarkField.setText(numberFormat.format(labMark));
        midtermMarkField.setText(numberFormat.format(midtermMark));
        finalExamMarkField.setText(numberFormat.format(finalExamMark));
        overallMarkField.setText(numberFormat.format(overallMark));

    }



    public void Jesus(View v){
                ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().deleteAllMarks();
                ((StudentMarksCalculatorActivity) getActivity()).getDbHelper().deleteAllStudents();
                getFragmentManager().popBackStackImmediate();
    }
}
