package a.feedbackapp;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DatAdapter extends RecyclerView.Adapter<DatAdapter.ViewHolder> {

    private DatActivity dta;
    private HashMap<Question, ArrayList<RadioButton>> ral;
    private HashMap<Question, ArrayList<CheckBox>> bal;
    private HashMap<Question, ArrayList<Pair<Pair<TextView, EditText>, Integer>>> eal;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private Question q;
        private TextView ti;
        private LinearLayout lnr;
        private EditText et;
        private SeekBar rt;
        private Button rn, dp, sl, cn, dt;

        private TextView temp_1, temp_2;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void cor(int pos) {
            if (pos-- == 0) {
                et = (EditText) itemView.findViewById(R.id.dt_id);
                rn = (Button) itemView.findViewById(R.id.dt_rn);
                return;
            }
            if (pos + 1 > ResActivity.getResponseQuestionArray().size()) {
                dp = (Button) itemView.findViewById(R.id.dlf_dp);
                sl = (Button) itemView.findViewById(R.id.dlf_sl);
                cn = (Button) itemView.findViewById(R.id.dlf_cn);
                dt = (Button) itemView.findViewById(R.id.dlf_dt);
                return;
            }
            q = DatActivity.getResponseQuestionArray().get(pos);
            //TEMPORARY
            if (Question.typeString(q.getType()).equals("rating")) {
                ti = (TextView) itemView.findViewById(R.id.drt_ti);
                temp_1 = (TextView) itemView.findViewById(R.id.drt_temp_avr);
                temp_2 = (TextView) itemView.findViewById(R.id.drt_temp_num);
            }
            if (Question.typeString(q.getType()).equals("selection_single")) {
                ti = (TextView) itemView.findViewById(R.id.dol_ti);
                temp_1 = (TextView) itemView.findViewById(R.id.dol_temp_res);
                temp_2 = (TextView) itemView.findViewById(R.id.dol_temp_num);
            }
            if (Question.typeString(q.getType()).equals("selection_multiple")) {
                ti = (TextView) itemView.findViewById(R.id.dol_ti);
                temp_1 = (TextView) itemView.findViewById(R.id.dol_temp_res);
                temp_2 = (TextView) itemView.findViewById(R.id.dol_temp_num);
            }
            if (Question.typeString(q.getType()).equals("selection_ranking")) {
                ti = (TextView) itemView.findViewById(R.id.dol_ti);
                temp_1 = (TextView) itemView.findViewById(R.id.dol_temp_res);
                temp_2 = (TextView) itemView.findViewById(R.id.dol_temp_num);
            }
            if (Question.typeString(q.getType()).equals("open")) {
                ti = (TextView) itemView.findViewById(R.id.daq_ti);
                temp_1 = (TextView) itemView.findViewById(R.id.daq_temp_res);
                temp_2 = (TextView) itemView.findViewById(R.id.daq_temp_num);
            }
            //TEMPORARY
        }
    }

    public void setActivity(DatActivity dta) {this.dta = dta;}

    @Override
    public int getItemViewType(int position) {
        return position-- > 0 ? position < ResActivity.getResponseQuestionArray().size() ? ResActivity.getResponseQuestionArray().get(position).getType() : 6 : 5;
    }

    @Override
    public DatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int c;
        switch (i) {
            case 0: c = R.layout.card_dtrt; break;
            case 1: c = R.layout.card_dtol; break;
            case 2: c = R.layout.card_dtol; break;
            case 3: c = R.layout.card_dtol; break;
            case 4: c = R.layout.card_dtaq; break;
            case 5: c = R.layout.card_dtrn; break;
            case 6: c = R.layout.card_dtlf; break;
            default: c = 0; break;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(c, viewGroup, false);
        return new DatAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DatAdapter.ViewHolder viewHolder, int i) {
        viewHolder.cor(i);
        if (i-- == 0) {
            viewHolder.et.setText(dta.prf);
            viewHolder.rn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.rename(viewHolder.et.getText().toString());}});
            return;
        }
        //TEMPORARY
        //Further response details will be displayed upon clicking of view | listeners to be set
        if (i < ResActivity.getResponseQuestionArray().size()) {
            viewHolder.ti.setText(viewHolder.q.getTitle());
            if (Question.typeString(viewHolder.q.getType()).equals("rating")) {
                double sum = 0;
                for (Question.Response qr : viewHolder.q.getResponseSet()) sum += qr.getRating() * viewHolder.q.getInv() + viewHolder.q.getMin();
                viewHolder.temp_1.setText("Average rating: " + sum / viewHolder.q.getResponseSet().size());
                viewHolder.temp_2.setText("Responses: " + viewHolder.q.getResponseSet().size());
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_single")) {
                ArrayList<Integer> m = new ArrayList<>();
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) m.add(0);
                for (Question.Response qr : viewHolder.q.getResponseSet()) for (int j = 0; j < qr.getSelection().size(); j++) m.set(j, m.get(j) + qr.getSelection().get(j));
                int a_l = -1, b_l = -1;
                for (int j = 0; j < m.size(); j++) if (m.get(j) > a_l) {a_l = m.get(j); b_l = j;}
                viewHolder.temp_1.setText("Most frequent response: " + viewHolder.q.getOptions().get(b_l) + " (" + a_l + " selections)");
                viewHolder.temp_2.setText("Responses: " + viewHolder.q.getResponseSet().size());
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_multiple")) {
                ArrayList<Integer> m = new ArrayList<>();
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) m.add(0);
                for (Question.Response qr : viewHolder.q.getResponseSet()) for (int j = 0; j < qr.getSelection().size(); j++) m.set(j, m.get(j) + qr.getSelection().get(j));
                int a_l = -1, b_l = -1;
                for (int j = 0; j < m.size(); j++) if (m.get(j) > a_l) {a_l = m.get(j); b_l = j;}
                viewHolder.temp_1.setText("Most frequent response: " + viewHolder.q.getOptions().get(b_l) + " (" + a_l + " selections)");
                viewHolder.temp_2.setText("Responses: " + viewHolder.q.getResponseSet().size());
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_ranking")) {
                ArrayList<Integer> m = new ArrayList<>();
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) m.add(0);
                for (Question.Response qr : viewHolder.q.getResponseSet()) for (int j = 0; j < qr.getSelection().size(); j++) m.set(j, m.get(j) + qr.getSelection().get(j));
                int a_l = 1000000, b_l = -1;
                for (int j = 0; j < m.size(); j++) if (m.get(j) < a_l) {a_l = m.get(j); b_l = j;}
                viewHolder.temp_1.setText("Highest ranked response: " + viewHolder.q.getOptions().get(b_l) + " (" + a_l + " cumulative)");
                viewHolder.temp_2.setText("Responses: " + viewHolder.q.getResponseSet().size());
            }
            if (Question.typeString(viewHolder.q.getType()).equals("open")) {
                viewHolder.temp_1.setText("Most recent response: " + (!viewHolder.q.getResponseSet().isEmpty() ? viewHolder.q.getResponseSet().get(viewHolder.q.getResponseSet().size() - 1) : ""));
                viewHolder.temp_2.setText("Responses: " + viewHolder.q.getResponseSet().size());
            }
            //TEMPORARY
        } else {
            viewHolder.dp.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.duplicate();}});
            if (!dta.isSeleceted()) {
                viewHolder.sl.setText("Select");
                viewHolder.sl.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.select();}});
            } else {
                viewHolder.sl.setText("Upload");
                if (!DatActivity.getResponseQuestionArray().isEmpty() && !DatActivity.getResponseQuestionArray().get(0).getResponseSet().isEmpty()) viewHolder.sl.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.upload();}});
                else viewHolder.sl.setEnabled(false);
            }
            viewHolder.cn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.cancel();}});
            viewHolder.dt.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {dta.delete();}});
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        if (viewHolder.q != null) {
            //Temporarily redundant

            /*if (Question.typeString(viewHolder.q.getType()).equals("rating")) {
                viewHolder.q.setResponse(viewHolder.q.new Response());
                viewHolder.q.getResponse().setRating(viewHolder.rt.getProgress());
                viewHolder.rt.setOnSeekBarChangeListener(null);
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_single")) {
                viewHolder.q.setResponse(viewHolder.q.new Response());
                ArrayList<Integer> salt = new ArrayList<>();
                for (RadioButton rb : ral.get(viewHolder.q)) salt.add(rb.isChecked() ? 1 : 0);
                viewHolder.q.getResponse().setSelection(salt);
                ral.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_multiple")) {
                viewHolder.q.setResponse(viewHolder.q.new Response());
                ArrayList<Integer> salt = new ArrayList<>();
                for (CheckBox c : bal.get(viewHolder.q)) salt.add(c.isChecked() ? 1 : 0);
                viewHolder.q.getResponse().setSelection(salt);
                bal.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_ranking")) {
                viewHolder.q.setResponse(viewHolder.q.new Response());
                ArrayList<Integer> salt = new ArrayList<>();
                for (Pair<Pair<TextView, EditText>, Integer> p : eal.get(viewHolder.q)) {
                    salt.add(p.second);
                    p.first.first.setOnClickListener(null);
                    p.first.second.setOnClickListener(null);
                }
                viewHolder.q.getResponse().setSelection(salt);
                eal.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();
            }
            if (Question.typeString(viewHolder.q.getType()).equals("open")) viewHolder.q.getResponse().setAnswer(viewHolder.et.getText().toString());

            */ //Temporarily redundant
        } else {
            if (viewHolder.rn != null) {
                viewHolder.rn.setOnClickListener(null);
            } else {
                viewHolder.dp.setOnClickListener(null);
                viewHolder.sl.setOnClickListener(null);
                viewHolder.cn.setOnClickListener(null);
                viewHolder.dt.setOnClickListener(null);
            }
        }
    }

    @Override
    public int getItemCount() {return DatActivity.getResponseQuestionArray().size() + 2;}

}
