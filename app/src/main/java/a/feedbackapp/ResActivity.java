package a.feedbackapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ResActivity extends AppCompatActivity {

    private static ArrayList<Question> qad;
    private static String rsn;
    private RecyclerView rc;
    private RecyclerView.LayoutManager lm;
    private ResAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        rsn = null;
        rc =(RecyclerView) findViewById(R.id.rs_rc);
        lm = new LinearLayoutManager(this);
        rc.setLayoutManager(lm);
        ra = new ResAdapter();
        ra.recal();
        ra.setActivity(this);
        qad = new ArrayList<>();
        rc.setAdapter(ra);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    public void complete(boolean saveData) {
        if (saveData) {
            for (int i = 0; i < qad.size(); i++) if (!qad.get(i).getResponse().validate()) {
                Toast.makeText(this, "Invalid answer in Queston " + i, Toast.LENGTH_SHORT).show();
                return;
            }
            save();
        }
        finish();
    }

    public void modify() {
        Intent in = new Intent(getApplicationContext(), LogActivity.class);
        startActivity(in);
    }

    private void save() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("acr_" + rsn + ".txt", MODE_APPEND));
            for (int i = 0; i < qad.size(); i++) out.write(qad.get(i).getResponse().toString() + (i < qad.size() - 1 ? "'''" : ";;;"));
            out.flush();
            out.close();
            String s = "";
            FileInputStream fIn = openFileInput("mta.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                s += String.copyValueOf(inputBuffer, 0, charRead);
                inputBuffer = new char[100];
            }
            isr.close();
            ArrayList<Pair<String, Pair<Integer, Integer>>> lad = new ArrayList<>();
            String[] a = s.split(";");
            for (String t : a) {
                String[] b = t.split("'");
                lad.add(new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]) + (rsn.equals(b[0]) ? 1 : 0))));
            }
            out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();
            Toast.makeText(this, "Response successfully submitted", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    private void load() {
        String s = "";
        try {
            FileInputStream fIn = openFileInput("qur.txt");
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
            //TEMPORARY
            try {
                s = "init;;;rating'''How has your experience with this interactive display project been?'''1,,,10,,,1;;;selection_single'''Which functionality did you appreciate the most?'''Option 1,,,Option 2,,,Option 3,,,Option 4,,,Option 5;;;open'''Any additional comments & feedback (optional)''';;;open'''Name (optional)'''";
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput("lyc_init.txt", MODE_PRIVATE));
                out.write(s);
                out.flush();
                out.close();
                out = new OutputStreamWriter(openFileOutput("qur.txt", MODE_PRIVATE));
                out.write(s);
                out.flush();
                out.close();
                out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
                out.write("init'4'0");
                out.flush();
                out.close();
            } catch (IOException ix) {
                Log.e("app", ix.toString());
                s = "";
            }
            //TEMPORARY
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            s = "";
        } finally {
            qad = new ArrayList<>();
            int i;
            String[] a = s.split(";;;");
            rsn = a[0];
            for (i = 1; i < a.length; i++) {
                String[] b = a[i].split("'''", -1);
                qad.add(new Question(Question.typeNum(b[0]), b[1], b[2]));
            }
            ra.recal();
            ra.notifyDataSetChanged();
        }
    }

    public static boolean isValid() {return rsn != null;}

    public static ArrayList<Question> getResponseQuestionArray() {return qad;}

}
