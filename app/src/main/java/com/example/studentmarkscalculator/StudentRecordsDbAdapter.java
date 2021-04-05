package com.example.studentmarkscalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Database adapter for interacting with an SQLite database.
 * @author SaquibAnsari0101
 */
public class StudentRecordsDbAdapter {
    /**
     * Student Id column name.
     */
    public static final String STUDENT_ID = "_id";

    /**
     * Student first name column name.
     */
    public static final String STUDENT_FIRSTNAME = "firstname";

    /**
     * Student last name column name.
     */
    public static final String STUDENT_LASTNAME = "lastname";


    /**
     * Column name for the marks reference to the student that they belong to.
     */
    public static final String MARK_STUDENT_ID = "_id";

    /**
     * Lab mark column name.
     */
    public static final String MARK_LAB = "labmark";

    /**
     * Midterm mark column name.
     */
    public static final String MARK_MIDTERM = "midtermmark";

    /**
     * Final exam mark column name.
     */
    public static final String MARK_FINAL_EXAM = "finalexammark";


    /**
     * Average lab mark column name.
     */
    public static final String AVG_MARK_LAB = "avg("+MARK_LAB+")";

    /**
     * Average midterm mark column name.
     */
    public static final String AVG_MARK_MIDTERM = "avg("+MARK_MIDTERM+")";

    /**
     * Average final exam mark column name.
     */
    public static final String AVG_MARK_FINAL_EXAM = "avg("+MARK_FINAL_EXAM+")";


    /**
     * This class' tag for debugging.
     */
    private static final String TAG = "StudentRecordsDbAdapter";

    /**
     * Database helper for creating and upgrading the database.
     */
    private DatabaseHelper mDbHelper;

    /**
     * Writable database.
     */
    private SQLiteDatabase mDb;

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "studentrecords";

    /**
     * Student table name.
     */
    private static final String SQLITE_STUDENT_TABLE = "student";

    /**
     * Marks table name.
     */
    private static final String SQLITE_MARKS_TABLE = "marks";

    /**
     * Database version number.
     */
    private static final int DATABASE_VERSION = 6;

    /**
     * The Activity that is using this adapter.
     */
    private final Context mCtx;

    /**
     * SQLite query for that creates the student table.
     */
    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE if not exists " + SQLITE_STUDENT_TABLE + " ("
                    + STUDENT_ID + " integer PRIMARY KEY NOT NULL,"
                    + STUDENT_FIRSTNAME + " NOT NULL,"
                    + STUDENT_LASTNAME + " NOT NULL);"
            ;

    /**
     * SQLite query that creates the marks table.
     */
    private static final String CREATE_MARKS_TABLE =
            "CREATE TABLE if not exists " + SQLITE_MARKS_TABLE + " ("
                    + STUDENT_ID + " integer PRIMARY KEY NOT NULL,"
                    + MARK_LAB + " real,"
                    + MARK_MIDTERM + " real,"
                    + MARK_FINAL_EXAM + " real,"
                    + "CONSTRAINT fk_student FOREIGN KEY (" + MARK_STUDENT_ID + ")"
                    + " REFERENCES " + SQLITE_STUDENT_TABLE + "(" + STUDENT_ID + ")"
                    + ");"
            ;

    /**
     * SQLite query that inserts sample students.
     */
    public static final String INSERT_SAMPLE_STUDENTS =
            "INSERT INTO "+SQLITE_STUDENT_TABLE+" ("+STUDENT_ID+","+STUDENT_FIRSTNAME+","+STUDENT_LASTNAME+") VALUES "
                    + "(123001,'Ganesh','Vispute'),"
                    + "(123002,'Vishal','Wanve'),"
                    + "(123003,'Joshep','Jones'),"
                    + "(123004,'Abhishek','Palve'),"
                    + "(123005,'Atharva','Nikam'),"
                    + "(123006,'Faraz','Khan'),"
                    + "(123007,'Paurnima','Sali'),"
                    + "(123008,'Rehan','Chanegaon'),"
                    + "(123009,'Diya','Jain'),"
                    + "(123010,'Vaishali','Patel'),"
                    + "(123011,'Narendra','Modi'),"
                    + "(123012,'Rahul','Gandhi'),"
                    + "(123013,'Sabrina','Kausar'),"
                    + "(123014,'Tushar','Bhamre'),"
                    + "(123016,'Rutuja','Rane'),"
                    + "(123017,'Tarak','Mehta'),"
                    + "(123018,'Raju','Peake'),"
                    + "(123019,'Baburao','Apte'),"
                    + "(123020,'Mohandas','Gandhi');";

    /**
     * SQLite query that inserts sample marks.
     */
    public static final String INSERT_SAMPLE_MARKS =
            "INSERT INTO "+SQLITE_MARKS_TABLE+" ("+STUDENT_ID+","+MARK_LAB+","+MARK_MIDTERM+","+MARK_FINAL_EXAM+") VALUES "
                    + "(123001,12,30,20),"
                    + "(123002,28,23,40),"
                    + "(123003,15,16,34),"
                    + "(123004,10,28,20),"
                    + "(123005,0,23,40),"
                    + "(123006,30,30,40),"
                    + "(123007,23,null,null),"
                    + "(123008,25,null,null),"
                    + "(123009,14,null,null),"
                    + "(123010,null,30,40),"
                    + "(123011,23,29,37),"
                    + "(123012,12,20,23),"
                    + "(123013,14,15,5),"
                    + "(123014,27,29,26),"
                    + "(123016,29,30,39),"
                    + "(123017,25,29,38),"
                    + "(123018,22,27,37),"
                    + "(123019,20,14,36),"
                    + "(123020,28,23,34);";

    /**
     * Database helper for creating and upgrading the database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * Constructor.
         * @param context
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Creates the database tables.
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STUDENT_TABLE);
            db.execSQL(CREATE_MARKS_TABLE);
            db.execSQL(INSERT_SAMPLE_STUDENTS);
            db.execSQL(INSERT_SAMPLE_MARKS);
        }

        /**
         * Upgrades the database.
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_STUDENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_MARKS_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor
     * @param ctx
     */
    public StudentRecordsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Initializes the database helper and the database.
     * @return
     * @throws SQLException
     */
    public StudentRecordsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database helper.
     */
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     * Inserts a student into the student table.
     * @param studentNumber
     * @param firstname
     * @param lastname
     * @return
     */
    public long insertStudent(int studentNumber, String firstname, String lastname) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(STUDENT_ID, studentNumber);
        initialValues.put(STUDENT_FIRSTNAME, firstname);
        initialValues.put(STUDENT_LASTNAME, lastname);

        return mDb.insertOrThrow(SQLITE_STUDENT_TABLE, null, initialValues);
    }

    /**
     * Inserts a student's marks into the marks table.
     * @param studentId
     * @param labMark
     * @param midtermMark
     * @param finalExamMark
     * @return
     */
    public long insertMarks(int studentId, Double labMark, Double midtermMark, Double finalExamMark) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(STUDENT_ID, studentId);
        initialValues.put(MARK_LAB, labMark);
        initialValues.put(MARK_MIDTERM, midtermMark);
        initialValues.put(MARK_FINAL_EXAM, finalExamMark);

        return mDb.insertOrThrow(SQLITE_MARKS_TABLE, null, initialValues);
    }

    /**
     * Fetches all students from the database.
     * @return
     */
    public Cursor fetchAllStudents() {
        Cursor mCursor = mDb.query(SQLITE_STUDENT_TABLE, new String[] {STUDENT_ID, STUDENT_FIRSTNAME, STUDENT_LASTNAME},
                null, null, null, null, STUDENT_ID);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Fetches a student from the database given their Id.
     * @param id
     * @return
     */
    public Cursor fetchStudentById(Long id) {
        Log.w(TAG, id.toString());
        Cursor mCursor = null;
        if (id == null  ||  id.toString().length () == 0)  {
            mCursor = mDb.query(SQLITE_STUDENT_TABLE, new String[] {STUDENT_ID,
                            STUDENT_FIRSTNAME, STUDENT_LASTNAME},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_STUDENT_TABLE, new String[] {STUDENT_ID,
                            STUDENT_FIRSTNAME, STUDENT_LASTNAME},
                    STUDENT_ID + " = " + id, null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Fetches a student's marks given the student's Id.
     * @param studentId
     * @return
     */
    public Cursor fetchMarksByStudentId(long studentId) {
        Cursor mCursor = mDb.query(SQLITE_MARKS_TABLE,
                new String[] {MARK_LAB, MARK_MIDTERM, MARK_FINAL_EXAM},
                STUDENT_ID + " = ?",
                new String[] {Long.toString(studentId)},
                null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Fetches the average marks from the database.
     * @return
     */
    public Cursor fetchAverageMarks() {
        Cursor mCursor = mDb.query(SQLITE_MARKS_TABLE,
                new String[] {AVG_MARK_LAB, AVG_MARK_MIDTERM, AVG_MARK_FINAL_EXAM},
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Updates a student's marks.
     * @param studentId
     * @param labMark
     * @param midtermMark
     * @param finalExamMark
     * @return
     */
    public int updateMarks(long studentId, Double labMark, Double midtermMark, Double finalExamMark) {
        ContentValues newValues = new ContentValues();
        newValues.put(MARK_LAB, labMark);
        newValues.put(MARK_MIDTERM, midtermMark);
        newValues.put(MARK_FINAL_EXAM, finalExamMark);
        return mDb.update(
                SQLITE_MARKS_TABLE,
                newValues,
                STUDENT_ID + " = ?",
                new String[] {Long.toString(studentId)}
        );
    }

    /**
     * Deletes a student and their marks from the database.
     * @param id
     * @return
     */
    public boolean deleteStudentAndMarksById(long id) {
        int doneDelete = 0;
        doneDelete += mDb.delete(
                SQLITE_STUDENT_TABLE,
                STUDENT_ID + " = ?",
                new String[] {Long.toString(id)}
        );
        doneDelete += mDb.delete(
                SQLITE_MARKS_TABLE,
                STUDENT_ID + " = ?",
                new String[] {Long.toString(id)}
        );
        return doneDelete == 2;
    }

    /**
     * Deletes all students from the database.
     * @return
     */
    public boolean deleteAllStudents() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_STUDENT_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    /**
     * Deletes all marks from the database.
     * @return
     */
    public boolean deleteAllMarks() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_MARKS_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }
}
