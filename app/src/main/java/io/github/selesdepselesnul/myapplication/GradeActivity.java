package io.github.selesdepselesnul.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.IDNA;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

import cz.msebera.android.httpclient.Header;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static android.view.View.INVISIBLE;
//import static io.github.selesdepselesnul.myapplication.R.id.progressBar;

public class GradeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private boolean isBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //progressBar.setVisibility(View.VISIBLE);

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
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading Nilai...");
        progress.setProgressStyle(R.style.ProgressBar);
        progress.setIndeterminate(true);

        progress.show();
//        progressBar.setVisibility(View.VISIBLE);

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
                                        grade.getString("nilaihuruf")
                                });
                            }
                            List<String[]> header = new ArrayList<String[]>();
                            TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
                            columnModel.setColumnWeight(0, isLandscape ? 10 : 4);
                            columnModel.setColumnWeight(1, 2);
                            columnModel.setColumnWeight(2, 2);
                            tableView.setColumnModel(columnModel);
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(self, new String[] {"Matkul", "SKS", "Nilai"}));
                            tableView.setDataAdapter(new SimpleTableDataAdapter(self, grades));

                            Context context = getApplicationContext();
                            CharSequence text = isLandscape ? "Ubah ke potrait untuk lihat berdasarkan kode MK"
                                                            : "Ubah ke landscape untuk lihat berdasarkan Nama MK";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);

                            toast.show();
                            progress.cancel();
//                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
                        }
                    }


                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.bookmarkButton) {
            if(isBookmarked) {
                item.setIcon(R.drawable.ic_turned_in_not_black_24dp);
                isBookmarked = false;
            } else {
                item.setIcon(R.drawable.ic_turned_in_black_24dp);
                isBookmarked = true;
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
