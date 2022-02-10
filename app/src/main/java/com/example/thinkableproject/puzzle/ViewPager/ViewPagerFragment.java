package com.example.thinkableproject.puzzle.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.thinkableproject.R;
import com.example.thinkableproject.puzzle.Class.DataBase;

public class ViewPagerFragment extends Fragment {

    View view;
    String msg;
    int pos;

    TextView[] scoreTV =new TextView[10];
    TextView[] nameTV= new TextView[10];

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int TEXT_NAME[] = {R.id.textViewName1, R.id.textViewName2, R.id.textViewName3,R.id.textViewName4,R.id.textViewName5,
            R.id.textViewName6,R.id.textViewName7,R.id.textViewName8,R.id.textViewName9,R.id.textViewName10};
    private int TEXT_SCORE[] = {R.id.textViewScore1, R.id.textViewScore2, R.id.textViewScore3,R.id.textViewScore4,R.id.textViewScore5,
            R.id.textViewScore6,R.id.textViewScore7,R.id.textViewScore8,R.id.textViewScore9,R.id.textViewScore10};

    public ViewPagerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        pos = getArguments().getInt("page");
        msg = getArguments().getString("massage");

        TextView sizeBoad = view.findViewById(R.id.SizeOfBoard);
        sizeBoad.setText(msg);

        position(pos);

        return view;
    }

    private  void position(int pos) {
        int index=0;
        DataBase dataBase = new DataBase(this.getActivity());
        if (pos == 1) {
            dataBase.setPrefRef("PRESNAME9", "PRESSCORE9");
            index = dataBase.preferencesCounter.getInt("game9counter",0);
        } else if (pos == 2) {
            dataBase.setPrefRef("PRESNAME15", "PRESSCORE15");
            index = dataBase.preferencesCounter.getInt("game15counter",0);
        } else if (pos == 3) {
            dataBase.setPrefRef("PRESNAME24", "PRESSCORE24");
            index = dataBase.preferencesCounter.getInt("game24counter",0);
        }
        int i=0;
        int[] valueArr = new int[index];
        int[] keyArr = new int[index];

        while (i < index) {
            valueArr[i] = dataBase.preferencesScore.getInt("" + i, 0);
            keyArr[i] = i;
            i++;
        }
        for (i = 0; i < valueArr.length; i++) {
            for (int j = i + 1; j < valueArr.length; j++) {
                int tmp = 0;
                if (valueArr[i] <= valueArr[j]) {
                    tmp = valueArr[i];
                    valueArr[i] = valueArr[j];
                    valueArr[j] = tmp;
                    tmp = keyArr[i];
                    keyArr[i] = keyArr[j];
                    keyArr[j] = tmp;
                }
            }
        }
        i=0;
        int anotherIndex=index;
        while (i < index) {
            nameTV[i] = view.findViewById(TEXT_NAME[i]);
            nameTV[i].setText(dataBase.preferencesName.getString("" + keyArr[anotherIndex-1], "empty"));
            scoreTV[i] = view.findViewById(TEXT_SCORE[i]);
            scoreTV[i].setText("" + dataBase.preferencesScore.getInt("" + keyArr[anotherIndex-1], 0));
            i++;
            anotherIndex--;
        }

    }
}
