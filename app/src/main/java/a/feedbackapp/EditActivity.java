package a.feedbackapp;

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

public class EditActivity extends AppCompatActivity {

    private static ArrayList<Question> ead;
    private RecyclerView rc;
    private RecyclerView.LayoutManager lm;
    private EditAdapter ra;
    private String rsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        rsn = getIntent().getStringExtra("qnr");
        rc =(RecyclerView) findViewById(R.id.rs_rc);
        lm = new LinearLayoutManager(this);
        rc.setLayoutManager(lm);
        ra = new EditAdapter();
        ra.setActivity(this);
        ead = new ArrayList<>();
        rc.setAdapter(ra);
        load();
    }

    public void save(String npr) {
        try {
            rsn = npr;
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
            String[] a = s.split(";");
            ArrayList<Pair<String, Pair<Integer, Integer>>> lad = new ArrayList<>();
            for (String t : a) {
                String[] b = t.split("'");
                if (rsn.equals(b[0])) {
                    Toast.makeText(this, "Question set title already in use", Toast.LENGTH_SHORT).show();
                    return;
                }
                lad.add(new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
            }
            lad.add(0, new Pair<>(rsn, new Pair<>(ead.size(), 0)));
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();
            out = new OutputStreamWriter(openFileOutput("lyc_" + rsn + ".txt", MODE_APPEND));
            out.write(rsn + ";;;");
            for (int i = 0; i < ead.size(); i++) out.write(ead.get(i).toString() + (i < ead.size() - 1 ? ";;;" : ""));
            out.flush();
            out.close();
            Toast.makeText(this, "Question set successfully saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    private void load() {
        String s = "";
        try {
            if (rsn != null) {
                FileInputStream fIn = openFileInput("lyc_" + rsn + ".txt");
                InputStreamReader isr = new InputStreamReader(fIn);
                char[] inputBuffer = new char[100];
                int charRead;
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    s += String.copyValueOf(inputBuffer, 0, charRead);
                    inputBuffer = new char[100];
                }
                isr.close();
            }
        } catch (FileNotFoundException ex) {
            Log.d("app", ex.toString());
            s = "";
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            s = "";
        } finally {
            ead = new ArrayList<>();
            int i;
            String[] a = s.split(";;;");
            for (i = 1; i < a.length; i++) {
                String[] b = a[i].split("'''");
                ead.add(new Question(Question.typeNum(b[0]), b[1], b[2]));
            }
            ra.notifyDataSetChanged();
        }
    }

    public static ArrayList<Question> getEditQuestionArray() {return ead;}

}
