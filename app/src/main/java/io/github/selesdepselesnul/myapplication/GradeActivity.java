package io.github.selesdepselesnul.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import cz.msebera.android.httpclient.Header;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class GradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();

        requestParams.put("nim", "41155050140062");

        final TableView tableView = (TableView) findViewById(R.id.tableView);
        tableView.setColumnCount(4);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        final GradeActivity self = this;


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
                                        grade.getString("nmmk"),
                                        grade.getString("jmlsks"),
                                        grade.getString("nilaihuruf"),
                                        grade.getString("bobotnilai")
                                });
                            }

                            tableView.setDataAdapter(new SimpleTableDataAdapter(self, grades));
                            //TextView helloTextView = (TextView) findViewById(R.id.helloTextView);
                            //helloTextView.setText(data.toString());
                            //Log.i("mantab", data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
