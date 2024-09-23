package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ScoreViewHolder> {

    private final List<UserScoreModel> scoreList;

    public HistoryAdapter(List<UserScoreModel> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historyexam_custom, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        UserScoreModel score = scoreList.get(position);
        holder.title.setText(score.getTitle());
        holder.score.setText("Tổng điểm: " + score.getScore());
        holder.scoreListening.setText("Tổng điểm bài nghe: " + score.getTotalListening());
        holder.scoreReading.setText("Tổng điểm bài đọc: " + score.getTotalReading());
        holder.dateFinish.setText("Ngày làm bài: " + score.getDateFinish());
        holder.timeFinish.setText("Thời gian hoàn thành: " + score.getTimeFinish());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView title, score, scoreListening, scoreReading, dateFinish, timeFinish;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            score = itemView.findViewById(R.id.score);
            scoreListening = itemView.findViewById(R.id.scoreListening);
            scoreReading = itemView.findViewById(R.id.scoreReading);
            dateFinish = itemView.findViewById(R.id.dateFinish);
            timeFinish = itemView.findViewById(R.id.timeFinish);
        }
    }
}

