package io.github.selesdepselesnul.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import cz.msebera.android.httpclient.Header;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class GradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean isLandscape = getApplicationContext().getResources().getBoolean(R.bool.is_landscape);

        setContentView(R.layout.activity_grade);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();

        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.ID_MESSAGE);

        requestParams.put("nim", id);

        final TableView tableView = (TableView) findViewById(R.id.tableView);
        tableView.setColumnCount(4);

        final GradeActivity self = this;

        Context context = getApplicationContext();
        CharSequence text = isLandscape ? "Rotate untuk melihat berdasarkan Kode MK" : "Rotate untuk melihat berdasarkan Nama MK";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        client.post("http://www.unla.ac.id/index.php/e_akademic/c_kartuhasilstudi/grid",
                requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {
                            JSONArray data = response.getJSONArray("data");

                            List<String[]> grades = new ArrayList<String[]>();
                            for (int i=0;i<data.length();i++)
                            {

                                JSONObject grade = (JSONObject)data.get(i);

                                grades.add(new String[] {
                                        grade.getString(isLandscape ? "nmmk" : "kdmk"),
                                        grade.getString("jmlsks"),
                                        grade.getString("nilaihuruf"),
                                        grade.getString("bobotnilai")
                                });
                            }
                            List<String[]> header = new ArrayList<String[]>();
                            TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
                            columnModel.setColumnWeight(0, isLandscape ? 10 : 2);
                            columnModel.setColumnWeight(1, 2);
                            columnModel.setColumnWeight(2, 2);
                            columnModel.setColumnWeight(3, 2);
                            tableView.setColumnModel(columnModel);
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(self, new String[] {isLandscape ? "Nama Matkul" : "Kode MK", "SKS", "Huruf", "Nilai"}));
                            tableView.setDataAdapter(new SimpleTableDataAdapter(self, grades));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
