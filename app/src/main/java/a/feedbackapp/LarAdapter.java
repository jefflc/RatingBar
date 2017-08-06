package a.feedbackapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LarAdapter extends RecyclerView.Adapter<LarAdapter.ViewHolder> {

    private LarActivity lra;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ti, nm, rs;

        public ViewHolder(View itemView) {
            super(itemView);
            ti = (TextView) itemView.findViewById(R.id.lr_ti);
            nm = (TextView) itemView.findViewById(R.id.lr_nm);
            rs = (TextView) itemView.findViewById(R.id.lr_rs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos < LarActivity.getQuestionDataArray().size()) {
                        Intent in = new Intent(lra, DatActivity.class);
                        in.putExtra("qnr", LarActivity.getQuestionDataArray().get(pos).first);
                        lra.startActivity(in);
                    } else {
                        //Temporarily disabled
                        /*Intent in = new Intent(lra, EditActivity.class);
                        lra.startActivity(in);*/
                    }
                }
            });
        }
    }

    public void setActivity(LarActivity lra) {this.lra = lra;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_lar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i < LarActivity.getQuestionDataArray().size()) {
            viewHolder.ti.setText(LarActivity.getQuestionDataArray().get(i).first);
            viewHolder.nm.setText(LarActivity.getQuestionDataArray().get(i).second.first + " questions");
            viewHolder.rs.setText(LarActivity.getQuestionDataArray().get(i).second.second + " responses");
        } else {
            viewHolder.ti.setText("Add Question Set");
            viewHolder.nm.setText("");
            viewHolder.rs.setText("");
        }
    }

    @Override
    public int getItemCount() {return LarActivity.getQuestionDataArray().size() + 1;}
}
