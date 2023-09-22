package com.accenture.accpenture.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accenture.accpenture.FragmentDataModel;
import com.accenture.accpenture.MyAdapter;
import com.accenture.accpenture.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    private ArrayList<FragmentDataModel> dataHolder;
    private MaterialCardView cardView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView1 = view.findViewById(R.id.recycler_view1);
        recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView3 = view.findViewById(R.id.recycler_view3);
        recyclerView4 = view.findViewById(R.id.recycler_view4);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView4.setLayoutManager(layoutManager4);

        dataHolder = new ArrayList<>();

        dataHolder.add(new FragmentDataModel("1"));
        dataHolder.add(new FragmentDataModel("2"));
        dataHolder.add(new FragmentDataModel("3"));
        dataHolder.add(new FragmentDataModel("4"));

        MyAdapter myAdapter1 = new MyAdapter(dataHolder);
        MyAdapter myAdapter2 = new MyAdapter(dataHolder);
        MyAdapter myAdapter3 = new MyAdapter(dataHolder);
        MyAdapter myAdapter4 = new MyAdapter(dataHolder);

        recyclerView1.setAdapter(myAdapter1);
        recyclerView2.setAdapter(myAdapter2);
        recyclerView3.setAdapter(myAdapter3);
        recyclerView4.setAdapter(myAdapter4);

        return view;
    }
}