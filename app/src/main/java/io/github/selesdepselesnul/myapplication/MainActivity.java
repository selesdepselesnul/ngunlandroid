package io.github.selesdepselesnul.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String ID_MESSAGE = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String bookmarkedId = sharedPref.getString(getString(R.string.bookmarked_id), "");
        TextView idEditText = (TextView) findViewById(R.id.idEditText);
        idEditText.setText(bookmarkedId);
    }

    public void onClickSeeGrade(View view) {
        Intent intent = new Intent(this, GradeActivity.class);
        EditText editText = (EditText) findViewById(R.id.idEditText);
        String message = editText.getText().toString();
        intent.putExtra(ID_MESSAGE, message);
        startActivity(intent);
    }

    public void onClickCreatedBy(View view) {
        Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/selesdepselesnul"));
        startActivity(browser);
    }
}
