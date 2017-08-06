package a.feedbackapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.ViewHolder> {

    private ResActivity rsa;
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
        private Button sv, cn, op;
        private TextWatcher tw;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void cor(int pos) {
            if (pos + 1 > ResActivity.getResponseQuestionArray().size()) {
                sv = (Button) itemView.findViewById(R.id.rlf_sv);
                cn = (Button) itemView.findViewById(R.id.rlf_cn);
                op = (Button) itemView.findViewById(R.id.rlf_op);
                return;
            }
            q = ResActivity.getResponseQuestionArray().get(pos);
            if (Question.typeString(q.getType()).equals("rating")) {
                ti = (TextView) itemView.findViewById(R.id.rrt_ti);
                lnr = null;
                et = (EditText) itemView.findViewById(R.id.rs_eq);
                rt = (SeekBar) itemView.findViewById(R.id.rs_rt);
            }
            if (Question.typeString(q.getType()).equals("selection_single")) {
                ti = (TextView) itemView.findViewById(R.id.rol_ti);
                lnr = (LinearLayout) itemView.findViewById(R.id.rs_lt);
                et = null;
                rt = null;
            }
            if (Question.typeString(q.getType()).equals("selection_multiple")) {
                ti = (TextView) itemView.findViewById(R.id.rol_ti);
                lnr = (LinearLayout) itemView.findViewById(R.id.rs_lt);
                et = null;
                rt = null;
            }
            if (Question.typeString(q.getType()).equals("selection_ranking")) {
                ti = (TextView) itemView.findViewById(R.id.rol_ti);
                lnr = (LinearLayout) itemView.findViewById(R.id.rs_lt);
                et = null;
                rt = null;
            }
            if (Question.typeString(q.getType()).equals("open")) {
                ti = (TextView) itemView.findViewById(R.id.raq_ti);
                lnr = null;
                et = (EditText) itemView.findViewById(R.id.rs_pr);
                rt = null;
            }
        }
    }

    public void recal() {
        ral = new HashMap<>();
        bal = new HashMap<>();
        eal = new HashMap<>();
    }

    public void setActivity(ResActivity rsa) {this.rsa = rsa;}

    @Override
    public int getItemViewType(int position) {
        return position < ResActivity.getResponseQuestionArray().size() ? ResActivity.getResponseQuestionArray().get(position).getType() : 5;
    }

    @Override
    public ResAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int c;
        switch (i) {
            case 0: c = R.layout.card_rsrt; break;
            case 1: c = R.layout.card_rsol; break;
            case 2: c = R.layout.card_rsol; break;
            case 3: c = R.layout.card_rsol; break;
            case 4: c = R.layout.card_rsaq; break;
            case 5: c = R.layout.card_rslf; break;
            default: c = 0; break;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(c, viewGroup, false);
        return new ResAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResAdapter.ViewHolder viewHolder, int i) {
        viewHolder.cor(i);
        if (i < ResActivity.getResponseQuestionArray().size()) {
            viewHolder.ti.setText(viewHolder.q.getTitle());
            if (Question.typeString(viewHolder.q.getType()).equals("rating")) {
                viewHolder.rt.setMax((int) Math.round((viewHolder.q.getMax() - viewHolder.q.getMin()) / viewHolder.q.getInv()));
                viewHolder.et.addTextChangedListener(viewHolder.tw = new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {viewHolder.rt.setProgress((int) Math.round((Double.parseDouble(viewHolder.et.getText().toString()) - viewHolder.q.getMin()) / viewHolder.q.getInv())); viewHolder.q.getResponse().setRating(viewHolder.rt.getProgress());}
                        catch (NumberFormatException ex) {viewHolder.et.setText(Double.toString(viewHolder.rt.getProgress() * viewHolder.q.getInv() + viewHolder.q.getMin()));}
                    }

                    @Override public void afterTextChanged(Editable s) {}
                });
                viewHolder.rt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {if (fromUser) {viewHolder.et.setText(Double.toString(viewHolder.rt.getProgress() * viewHolder.q.getInv() + viewHolder.q.getMin())); viewHolder.q.getResponse().setRating(viewHolder.rt.getProgress());}}
                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });
                if (viewHolder.q.getResponse() == null) viewHolder.q.setResponse(viewHolder.q.new Response());
                else if (viewHolder.q.getResponse().validate()) {
                    viewHolder.rt.setProgress(viewHolder.q.getResponse().getRating());
                    viewHolder.et.setText(Double.toString(viewHolder.q.getResponse().getRating() * viewHolder.q.getInv() + viewHolder.q.getMin()));
                }
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_single")) {
                ral.put(viewHolder.q, new ArrayList<RadioButton>());
                LinearLayout tl = null;
                LinearLayout.LayoutParams lpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams par;
                lpr.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, rsa.getResources().getDisplayMetrics()), 0, 0);
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) {
                    if (j % 3 == 0) {
                        if (tl != null) viewHolder.lnr.addView(tl);
                        tl = new LinearLayout(rsa);
                        tl.setLayoutParams(lpr);
                    }
                    RadioButton rb = new RadioButton(rsa);
                    par = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, rsa.getResources().getDisplayMetrics()), LinearLayout.LayoutParams.WRAP_CONTENT);
                    par.setMargins(j % 3 > 0 ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, rsa.getResources().getDisplayMetrics()) : 0, 0, 0, 0);
                    rb.setLayoutParams(par);
                    rb.setText(viewHolder.q.getOptions().get(j));
                    rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    rb.setTextColor(Color.BLACK);
                    rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {for (int i = 0; i < ral.get(viewHolder.q).size(); i++) if (ral.get(viewHolder.q).get(i) == buttonView) viewHolder.q.getResponse().getSelection().set(i, 1); else ral.get(viewHolder.q).get(i).setChecked(false);}
                            else for (int i = 0; i < ral.get(viewHolder.q).size(); i++) if (ral.get(viewHolder.q).get(i) == buttonView) viewHolder.q.getResponse().getSelection().set(i, 0);
                        }
                    });
                    tl.addView(rb);
                    ral.get(viewHolder.q).add(rb);
                }
                if (tl != null) viewHolder.lnr.addView(tl);
                if (viewHolder.q.getResponse() == null) viewHolder.q.setResponse(viewHolder.q.new Response());
                else if (viewHolder.q.getResponse().validate()) for (int j = 0; j < viewHolder.q.getOptions().size(); j++) ral.get(viewHolder.q).get(j).setChecked(viewHolder.q.getResponse().getSelection().get(j) > 0);
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_multiple")) {
                bal.put(viewHolder.q, new ArrayList<CheckBox>());
                LinearLayout tl = null;
                LinearLayout.LayoutParams lpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams par;
                lpr.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, rsa.getResources().getDisplayMetrics()), 0, 0);
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) {
                    if (j % 3 == 0) {
                        if (tl != null) viewHolder.lnr.addView(tl);
                        tl = new LinearLayout(rsa);
                        tl.setLayoutParams(lpr);
                    }
                    CheckBox c = new CheckBox(rsa);
                    par = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, rsa.getResources().getDisplayMetrics()), LinearLayout.LayoutParams.WRAP_CONTENT);
                    par.setMargins(j % 3 > 0 ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, rsa.getResources().getDisplayMetrics()) : 0, 0, 0, 0);
                    c.setLayoutParams(par);
                    c.setText(viewHolder.q.getOptions().get(j));
                    c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    c.setTextColor(Color.BLACK);
                    c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {for (int i = 0; i < ral.get(viewHolder.q).size(); i++) if (ral.get(viewHolder.q).get(i) == buttonView) viewHolder.q.getResponse().getSelection().set(i, isChecked ? 1 : 0);}});
                    tl.addView(c);
                    bal.get(viewHolder.q).add(c);
                }
                if (tl != null) viewHolder.lnr.addView(tl);
                if (viewHolder.q.getResponse() == null) viewHolder.q.setResponse(viewHolder.q.new Response());
                else if (viewHolder.q.getResponse().validate()) for (int j = 0; j < viewHolder.q.getOptions().size(); j++) bal.get(viewHolder.q).get(j).setChecked(viewHolder.q.getResponse().getSelection().get(j) > 0);
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_ranking")) {
                /*eal.put(viewHolder.q, new ArrayList<Pair<Pair<TextView, EditText>, Integer>>());
                LinearLayout tl = null;
                LinearLayout.LayoutParams lpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams par;
                lpr.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, rsa.getResources().getDisplayMetrics()), 0, 0);
                for (int j = 0; j < viewHolder.q.getOptions().size(); j++) {
                    if (j % 3 == 0) {
                        if (tl != null) viewHolder.lnr.addView(tl);
                        tl = new LinearLayout(rsa);
                        tl.setLayoutParams(lpr);
                    }
                    final TextView tx = new TextView(rsa);
                    final EditText et = new EditText(rsa);
                    par = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, rsa.getResources().getDisplayMetrics()), LinearLayout.LayoutParams.WRAP_CONTENT);
                    par.setMargins(j % 3 > 0 ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, rsa.getResources().getDisplayMetrics()) : 0, 0, 0, 0);
                    tx.setLayoutParams(par);
                    tx.setText(viewHolder.q.getOptions().get(j));
                    tx.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tx.setTextColor(Color.BLACK);
                    par = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, rsa.getResources().getDisplayMetrics()), LinearLayout.LayoutParams.WRAP_CONTENT);
                    par.setMargins(j % 3 > 0 ? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, rsa.getResources().getDisplayMetrics()) : 0, 0, 0, 0);
                    et.setLayoutParams(par);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    et.setTextColor(Color.BLACK);
                    tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //error
                            int i, j, k;
                            for (i = 0; i < eal.get(viewHolder.q).size(); i++) if (eal.get(viewHolder.q).get(i).first.first == v) break;
                            k = eal.get(viewHolder.q).get(i).second;
                            if (k == 0) {
                                for (j = k = 0; j < eal.get(viewHolder.q).size(); j++) k = Math.max(k, eal.get(viewHolder.q).get(j).second);
                                eal.get(viewHolder.q).set(i, new Pair<>(eal.get(viewHolder.q).get(i).first, k + 1));
                            } else {
                                for (j = 0; j < eal.get(viewHolder.q).size(); j++) if (eal.get(viewHolder.q).get(j).second > k) eal.get(viewHolder.q).set(j, new Pair<>(eal.get(viewHolder.q).get(j).first, eal.get(viewHolder.q).get(j).second - 1));
                                eal.get(viewHolder.q).set(i, new Pair<>(eal.get(viewHolder.q).get(i).first, 0));
                            }
                        }
                    });
                    et.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i, j, k;
                            for (i = 0; i < eal.get(viewHolder.q).size(); i++) if (eal.get(viewHolder.q).get(i).first.second == v) break;
                            k = eal.get(viewHolder.q).get(i).second;
                            if (k == 0) {
                                for (j = k = 0; j < eal.get(viewHolder.q).size(); j++) k = Math.max(k, eal.get(viewHolder.q).get(j).second);
                                eal.get(viewHolder.q).set(i, new Pair<>(eal.get(viewHolder.q).get(i).first, k + 1));
                            } else {
                                for (j = 0; j < eal.get(viewHolder.q).size(); j++) if (eal.get(viewHolder.q).get(j).second > k) eal.get(viewHolder.q).set(j, new Pair<>(eal.get(viewHolder.q).get(j).first, eal.get(viewHolder.q).get(j).second - 1));
                                eal.get(viewHolder.q).set(i, new Pair<>(eal.get(viewHolder.q).get(i).first, 0));
                            }
                        }
                    });
                    tl.addView(tx);
                    tl.addView(et);
                    eal.get(viewHolder.q).add(new Pair<>(new Pair<>(tx, et), 0));
                }
                if (tl != null) viewHolder.lnr.addView(tl);
                if (viewHolder.q.getResponse() == null) viewHolder.q.setResponse(viewHolder.q.new Response());
                else if (viewHolder.q.getResponse().validate()) for (int j = 0; j < viewHolder.q.getOptions().size(); j++) eal.get(viewHolder.q).set(j, new Pair<>(eal.get(viewHolder.q).get(j).first, viewHolder.q.getResponse().getSelection().get(j)));
            */}
            if (Question.typeString(viewHolder.q.getType()).equals("open")) {
                viewHolder.et.addTextChangedListener(viewHolder.tw = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {viewHolder.q.getResponse().setAnswer(viewHolder.et.getText().toString());}

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                if (viewHolder.q.getResponse() == null) viewHolder.q.setResponse(viewHolder.q.new Response());
                else if (viewHolder.q.getResponse().validate()) viewHolder.et.setText(viewHolder.q.getResponse().getAnswer());
            }
        } else {
            viewHolder.sv.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rsa.complete(true);}});
            viewHolder.cn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rsa.complete(false);}});
            viewHolder.op.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rsa.modify();}});
            viewHolder.sv.setEnabled(ResActivity.isValid());
        }
    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder) {
        if (viewHolder.q != null) {
            if (Question.typeString(viewHolder.q.getType()).equals("rating")) {
                viewHolder.et.removeTextChangedListener(viewHolder.tw);
                viewHolder.rt.setOnSeekBarChangeListener(null);
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_single")) {
                if (ral.containsKey(viewHolder.q)) ral.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_multiple")) {
                if (bal.containsKey(viewHolder.q)) bal.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();
            }
            if (Question.typeString(viewHolder.q.getType()).equals("selection_ranking")) {
                /*viewHolder.q.setResponse(viewHolder.q.new Response());
                ArrayList<Integer> salt = new ArrayList<>();
                for (Pair<Pair<TextView, EditText>, Integer> p : eal.get(viewHolder.q)) {
                    salt.add(p.second);
                    p.first.first.setOnClickListener(null);
                    p.first.second.setOnClickListener(null);
                }
                viewHolder.q.getResponse().setSelection(salt);
                eal.remove(viewHolder.q);
                viewHolder.lnr.removeAllViews();*/
            }
            if (Question.typeString(viewHolder.q.getType()).equals("open")) viewHolder.et.removeTextChangedListener(viewHolder.tw);
        } else {
            viewHolder.sv.setOnClickListener(null);
            viewHolder.cn.setOnClickListener(null);
            viewHolder.op.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {return ResActivity.getResponseQuestionArray().size() + 1;}

}
