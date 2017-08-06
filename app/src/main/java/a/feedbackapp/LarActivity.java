package a.feedbackapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class LarActivity extends AppCompatActivity {

    private static ArrayList<Pair<String,Pair<Integer, Integer>>> lad;
    private RecyclerView rc;
    private RecyclerView.LayoutManager lm;
    private LarAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lar);
        rc =(RecyclerView) findViewById(R.id.lr_rc);
        lm = new LinearLayoutManager(this);
        rc.setLayoutManager(lm);
        ra = new LarAdapter();
        ra.setActivity(this);
        lad = new ArrayList<>();
        rc.setAdapter(ra);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    private void load() {
        String s = "";
        try {
            FileInputStream fIn = openFileInput("mta.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead=isr.read(inputBuffer)) > 0) {
                s += String.copyValueOf(inputBuffer,0,charRead);
                inputBuffer = new char[100];
            }
            isr.close();
        } catch (FileNotFoundException ex) {
            Log.d("app", ex.toString());
            s = "";
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            s = "";
        } finally {
            lad = new ArrayList<>();
            String[] a = s.split(";");
            for (String t : a) {
                String[] b = t.split("'");
                lad.add(new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
            }
            ra.notifyDataSetChanged();
        }
    }

    public static ArrayList<Pair<String,Pair<Integer, Integer>>> getQuestionDataArray() {return lad;}

}
