package com.yoon.memoria.Main.Fragment.Place;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yoon.memoria.Quiz.QuizActivity;
import com.yoon.memoria.R;


public class PlaceFragment extends Fragment implements PlaceContract.View {

    private PlacePresenter presenter;

    private Button btn_quiz;

    public PlaceFragment() {
        presenter = new PlacePresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    public void init(){
        btn_quiz = getView().findViewById(R.id.btn_quiz);
        btn_quiz.setOnClickListener(
                view -> getActivity().startActivity(new Intent(getActivity(), QuizActivity.class))
        );
    }

    @Override
    public PlaceContract.Presenter getPresenter() {
        return presenter;
    }
}
