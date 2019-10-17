package com.wazinsure.qsure.UI.quizpackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.wazinsure.qsure.UI.quizpackage.QuizContract.QuestionsTable;

import java.util.ArrayList;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("The owner of a particular insurance policy must have a relationship of \n" +
                "financial interest that is legal by law between the insured and the" +
                "insured object ", "Insurable Interest ", "uberrimate fidei", "Indemnity", 1);
        addQuestion(q1);
        Question q2 = new Question("Both parties must accurately and fully disclose all relevant facts and" +
                "material information to endure fairness ", "Indemnity", "Utmost Good Faith" +
                "(uberrimate fidei)", "Subrogation", 2);
        addQuestion(q2);
        Question q3 = new Question("Property is insured only against the incidents (perils) that are mentioned in the policy. This comes into play when more than one event or bad actor causes an accident or injury.", "Contribution", "Subrogation", "Proximate (Nearest) Cause", 3);
        addQuestion(q3);
        Question q4 = new Question("A is correct again", "Contribution", "Subrogation", "Proximate (Nearest) Cause", 1);
        addQuestion(q4);
        Question q5 = new Question("The amount of your compensation for a loss is directly related to the amount of loss that you actually suffered.", "Contribution", "Indemnity", "Insurable Interest", 2);
        addQuestion(q5);
        Question q6 = new Question("The owner of a particular insurance policy must have a relationship of financial interest that is legal by law between the insured and the insured object", "Utmost Good Faith", "Indemnity", "Subrogation", 2);
        addQuestion(q6);

        Question q7 = new Question("Insurance contract protects you from and compensates you to the extent of the damage, loss, or injury and not to allow you to make a profit.", "Contribution", "Indemnity", "Subrogation", 1);
        addQuestion(q7);

        Question q8 = new Question("Renovation is done only to the parts of the house which are affected by the fire.", "Contribution", "Insurable Interest", "Indemnity", 3);
        addQuestion(q8);

    }


    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}