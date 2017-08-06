package a.feedbackapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DatActivity extends AppCompatActivity {

    private static ArrayList<Question> qad;
    private RecyclerView rc;
    private RecyclerView.LayoutManager lm;
    private DatAdapter ra;
    public String prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat);
        prf = getIntent().getStringExtra("qnr");
        rc = (RecyclerView) findViewById(R.id.dt_rc);
        lm = new LinearLayoutManager(this);
        rc.setLayoutManager(lm);
        ra = new DatAdapter();
        ra.setActivity(this);
        qad = new ArrayList<>();
        rc.setAdapter(ra);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    public void rename(String npr) {
        if (npr.equals("")) {
            Toast.makeText(this, "Invalid question set title", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
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
                if (npr.equals(b[0])) {
                    Toast.makeText(this, "Question set title already in use", Toast.LENGTH_SHORT).show();
                    return;
                }
                lad.add(new Pair<>(prf.equals(b[0]) ? npr : b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
            }
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();

            Log.d("halp", "file " + new File("lyc_" + prf + ".txt").renameTo(new File("lyc_" + npr + ".txt"))); //INDETERMINATE
            Log.d("halp", "file " + new File("acr_" + prf + ".txt").renameTo(new File("acr_" + npr + ".txt"))); //INDETERMINATE

            prf = npr;
            Toast.makeText(this, "Question set successfully renamed", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    public void duplicate() {
        Intent in = new Intent(getApplicationContext(), EditActivity.class);
        in.putExtra("qnr", prf);
        startActivity(in);
        finish();
    }

    public void select() {
        try {
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
                if (prf.equals(b[0])) lad.add(0, new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
                else lad.add(new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
            }
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();
            FileChannel af = new FileInputStream("lyc_" + prf + ".txt").getChannel();
            FileChannel bf = new FileOutputStream("qur.txt").getChannel();
            bf.transferFrom(af, 0, af.size());
            Toast.makeText(this, "Question set successfully selected for usage", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    public void upload() {
        Intent in = new Intent(Intent.ACTION_SEND);
        in.setType("message/rfc822");
        in.putExtra(Intent.EXTRA_EMAIL, new String[]{"appventure@nushigh.edu.sg"}); //TEMPORARY
        in.putExtra(Intent.EXTRA_SUBJECT, "Appventure Feedback Responses for Question Set " + prf);
        String s = "";
        int i, j;
        for (i = 0; i < qad.get(0).getResponseSet().size(); i++) {
            for (j = 0; j < qad.size(); j++) s += qad.get(j).getResponseSet().get(i).toString() + (j < qad.size() - 1 ? "'''" : "");
            s += i < qad.get(0).getResponseSet().size() - 1 ? ";;;" : "";
        }
        in.putExtra(Intent.EXTRA_TEXT, s);
        try {
            startActivity(Intent.createChooser(in, "Upload responses to email"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients available", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isSeleceted() {
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
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            return false;
        }
        String[] a = s.split(";;;"), b = a[0].split("'''", -1);
        return b[0].equals(prf);
    }

    public void cancel() {
        finish();
    }

    public void delete() {
        if (isSeleceted()) {
            try {
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput("acr_" + prf + ".txt", MODE_PRIVATE));
                out.write("");
                out.flush();
                out.close();
                qad.clear();
                ra.notifyDataSetChanged();
                Toast.makeText(this, "Question set responses successfully deleted", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Log.e("app", ex.toString());
            }
            return;
        }
        try {
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
                if (!prf.equals(b[0])) lad.add(new Pair<>(b[0], new Pair<>(Integer.parseInt(b[1]), Integer.parseInt(b[2]))));
            }
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("mta.txt", MODE_PRIVATE));
            for (int i = 0; i < lad.size(); i++) out.write(lad.get(i).first + "'" + Integer.toString(lad.get(i).second.first) + "'" + Integer.toString(lad.get(i).second.second) + (i < lad.size() - 1 ? ";" : ""));
            out.flush();
            out.close();


            Log.d("halp", "file " + new File("lyc_" + prf + ".txt").delete()); //INDETERMINATE
            Log.d("halp", "file " + new File("acr_" + prf + ".txt").delete()); //INDETERMINATE


            Toast.makeText(this, "Question set successfully deleted", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException ex) {
            Log.e("app", ex.toString());
        }
    }

    private void load() {
        String s = "", t = "";
        try {
            FileInputStream fIn = openFileInput("lyc_" + prf + ".txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                s += String.copyValueOf(inputBuffer, 0, charRead);
                inputBuffer = new char[100];
            }
            isr.close();
        }  catch (FileNotFoundException ex) {
            Log.d("app", ex.toString());
            s = "";
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            s = "";
        }
        try {
            FileInputStream fIn = openFileInput("acr_" + prf + ".txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead=isr.read(inputBuffer)) > 0) {
                t += String.copyValueOf(inputBuffer,0,charRead);
                inputBuffer = new char[100];
            }
            isr.close();
        } catch (FileNotFoundException ex) {
            Log.d("app", ex.toString());
            t = "";
        } catch (IOException ex) {
            Log.e("app", ex.toString());
            t = "";
        }
        qad = new ArrayList<>();
        int i, j;
        String[] a = s.split(";;;");
        for (i = 1; i < a.length; i++) {
            String[] b = a[i].split("'''", -1);
            qad.add(new Question(Question.typeNum(b[0]), b[1], b[2]));
            qad.get(i - 1).setResponseSet(new ArrayList<Question.Response>());
        }
        a = t.split(";;;");
        if (!a[0].equals("")) for (i = 0; i < a.length; i++) {
            String[] b = a[i].split("'''", -1);
            for (j = 0; j < b.length; j++) qad.get(j).getResponseSet().add(qad.get(j).new Response(b[j]));
        }
        ra.notifyDataSetChanged();
    }

    public static ArrayList<Question> getResponseQuestionArray() {return qad;}
}
