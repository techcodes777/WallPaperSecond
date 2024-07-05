package com.hdlight.wallpaperapps.fragment;

import static com.hdlight.wallpaperapps.pagination.PaginationListener.PAGE_START;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.SearchAdapter;
import com.hdlight.wallpaperapps.adapter.SearchOtherAdapter;
import com.hdlight.wallpaperapps.adapter.WallPaperAdapter;
import com.hdlight.wallpaperapps.utils.Constant;

import java.util.ArrayList;
import java.util.Locale;


public class TrendingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SharedPreferences sharedPreferences;
    Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private WallPaperAdapter wallPaperAdapter;
    private ArrayList<String> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    String mobileNumber;
    int page = 0, limit = 2;
    NestedScrollView nestedScroll;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    int itemCount = 0;
    GridLayoutManager layoutManager;
    boolean loading = false;
    String last_key = "", last_node = "";
    boolean isMaxData = false, isScrolling = false;
    int ITEM_LOAD_COUNT = 10;
    int currentitems, tottalitems, scrolledoutitems;
    public static int pageNum = 1;
    public static String totalPage = "1";
    ExtendedFloatingActionButton fabButton;
    SearchView searchView;
    RecyclerView recyclerViewSV;
    GridLayoutManager gridLayoutManagerSV,gridLayoutManagerSVOth;
    NestedScrollView scrollView;
    ConstraintLayout fragment;
    boolean close = false;
    Bundle save;
    SearchAdapter searchAdapter;
    boolean back = true;
    SearchOtherAdapter searchOtherAdapter;
    RecyclerView recyclerViewSVOth;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("params");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        View view = null;
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("search");
            if (message.equals("1")) {
                view = inflater.inflate(R.layout.search_layout, container, false);
                searchView = view.findViewById(R.id.searchView);
//                Toast.makeText(getContext(), "success"  + message, Toast.LENGTH_SHORT).show();
//                if (!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                    searchView.onActionViewCollapsed();
//                } else {
//                }
                recyclerViewSV = view.findViewById(R.id.recyclerViewSV);
                scrollView = view.findViewById(R.id.nestedScroll);
                recyclerViewSVOth = view.findViewById(R.id.recyclerViewSVOther);

                gridLayoutManagerSVOth = new GridLayoutManager(context,3);

                gridLayoutManagerSV = new GridLayoutManager(context, 3);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        String q1 = query.replace(".", "");
//                        q1.trim();
                        processSearch(q1);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String nt = newText.replace(".", "");
//                        nt.trim();
                        processSearch(nt);
//                        fragment.setVisibility(View.VISIBLE);
                        saveState();
                        Constant.BACK_REMOVE = false;

                        return false;
                    }
                });

                Constant.BACK = true;

            } else {
                View view1 = inflater.inflate(R.layout.search_layout, container, false);
                SearchView searchView = view1.findViewById(R.id.searchView);
                searchView.setVisibility(View.GONE);
//                Toast.makeText(context, "Gone", Toast.LENGTH_SHORT).show();

                fragment = getActivity().findViewById(R.id.trendingFragment);
                fragment.setVisibility(View.VISIBLE);

                RecyclerView recyclerView1 = getActivity().findViewById(R.id.recyclerView);
                recyclerView1.setVisibility(View.VISIBLE);

                Constant.BACK = false;
                return view1;
            }
        } else {

            view = inflater.inflate(R.layout.fragment_trending, container, false);

            SharedPreferences sp = context.getSharedPreferences("search", Context.MODE_PRIVATE);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
            this.sharedPreferences = defaultSharedPreferences;
            reference = FirebaseDatabase.getInstance().getReference().child("images");
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            nestedScroll = (NestedScrollView) view.findViewById(R.id.nestedScroll);
            layoutManager = new GridLayoutManager(context, 3);
//            fragment = view.findViewById(R.id.trendingFragment);

            getData();
            fabButton = (ExtendedFloatingActionButton) view.findViewById(R.id.extFloatingActionButton);
            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // the delay of the extension of the FAB is set for 12 items

                    if (scrollY < oldScrollY){
//                        Log.e("scroll", "scroll up: "+ scrollX );
                        fabButton.show();
                        fabButton.setVisibility(View.VISIBLE);
                    }else {
                        fabButton.hide();
                        fabButton.setVisibility(View.GONE);
                    }

                    if (scrollY == 0) {
                        fabButton.hide();
                        fabButton.setVisibility(View.GONE);
//                        Toast.makeText(context, "sxroll" + scrollX, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.smoothScrollToPosition(0);
                    nestedScroll.fullScroll(NestedScrollView.FOCUS_UP);
                }
            });

            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setOnRefreshListener(this);

//            Toast.makeText(getContext(), "success"  , Toast.LENGTH_SHORT).show();
            return view;
        }
        return view;
    }

    private void processSearch(String query) {
//        startAt(query).endAt(query+"\uf8ff").
        Log.e("TrendFrag", "processSearch: " + query);
        String car = query.trim();
//        Toast.makeText(context, "j" + car, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("allimage").child(car).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> list1 = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    String data = shot.getValue().toString();
                    list1.add(data);
                    Log.e("TrendingFrag", "onDataChange: " + data);
                }
                scrollView.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(car)) {
//                    fragment.setVisibility(View.VISIBLE);
                    recyclerViewSV.setVisibility(View.GONE);

//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    TrendingFragment trendingFragment = new TrendingFragment();
//                    ft.replace(R.id.searchLL,trendingFragment);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                    Toast.makeText(context, "c" + car, Toast.LENGTH_SHORT).show();
//                    close = false;

                    fragment = getActivity().findViewById(R.id.trendingFragment);
                    fragment.setVisibility(View.VISIBLE);
                    RecyclerView recyclerView1 = getActivity().findViewById(R.id.recyclerView);
                    recyclerView1.setVisibility(View.VISIBLE);

                } else {
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    TrendingFragment bdf = new TrendingFragment();
//                    ft.replace(R.id.trendingFragment, bdf);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    fragment = getActivity().findViewById(R.id.trendingFragment);
                    fragment.setVisibility(View.GONE);
                    RecyclerView recyclerViewT = getActivity().findViewById(R.id.recyclerView);
                    recyclerViewT.setVisibility(View.GONE);
                    recyclerViewSV.setVisibility(View.VISIBLE);



                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.child(car);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.e("car", "onDataChange: " + snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }

                if (list1.isEmpty()){
//                    Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
                    recyclerViewSV.setVisibility(View.GONE);
                    recyclerViewSVOth.setVisibility(View.VISIBLE);
                }else {
                    scrollView.setVisibility(View.VISIBLE);
                    recyclerViewSVOth.setVisibility(View.GONE);
                }

                recyclerViewSV.setLayoutManager(gridLayoutManagerSV);
                searchAdapter = new SearchAdapter(list1, context);
                recyclerViewSV.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();

                FirebaseDatabase.getInstance().getReference().child("searchimg").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> list2 = new ArrayList<>();

                        for (DataSnapshot shot : snapshot.getChildren()) {
                            String data = shot.getValue().toString();
                            list2.add(data);
                            Log.e("TrendingFrag", "search other: " + data);
                        }

                        recyclerViewSVOth.setLayoutManager(gridLayoutManagerSVOth);
                        searchOtherAdapter = new SearchOtherAdapter(list2,context);
                        recyclerViewSVOth.setAdapter(searchOtherAdapter);
                        searchOtherAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "ff" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void loadData() {
    }

    private void getData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    String data = shot.getValue().toString();
                    list.add(data);
                }
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setHasFixedSize(false);
                wallPaperAdapter = new WallPaperAdapter(list, context);
                recyclerView.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(wallPaperAdapter);
                        wallPaperAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                Toast.makeText(context, "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        if (wallPaperAdapter != null){
            Toast.makeText(context, "not null", Toast.LENGTH_SHORT).show();
        }
        wallPaperAdapter.clear();
        getData();
    }

    private Bundle saveState() {
        TrendingFragment fragment = new TrendingFragment();
        Bundle state = new Bundle();
        state.putSerializable("search", 0);
        fragment.setArguments(state);
        return state;
    }


}