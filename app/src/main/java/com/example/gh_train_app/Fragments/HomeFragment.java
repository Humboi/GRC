package com.example.gh_train_app.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.gh_train_app.Adapters.NewsAdapter;
import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Image;
import com.example.gh_train_app.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NewsAdapter.OnItemClickListener{
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();



    private static int[] array_image_place = {
            R.drawable.president,
            R.drawable.railways1,
            R.drawable.railwayteam,
            R.drawable.development,
            R.drawable.kotoka,
            R.drawable.kojo,
            R.drawable.stateaddress,
            R.drawable.mahama,
    };

    private static String[] array_title_place = {
            "Pres Nana Addo sworn in as President of the nation",
            "Ghana Railway Authority Recieves new trains",
            "Board of GRA meet to discuss expansion of Railway systems in Ghana",
            "Gov. constructs road at Ofori-Krom",
            "Expansion and redesign of Kotoko Airport",
            "Hon. Kojo A.Nkrumah: Know your Minisiter Ghana",
            "Pres Nana Addo Address the Nation",
            "Be open about your past mistakes and tell Ghanaians how you intend to correct them – Asantehene advises Mahama",
    };

    private static String[] array_brief_place = {
            "State Initiation",
            "GRA Ghana",
            "GRA Board Secretariat",
            "Road Construction and Expansion",
            "Airport",
            "Ministries",
            "Jubilee House",
            "Kumasi",
    };

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ProgressDialog progressDialog;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<News> mNes;


    private void openDetailActivity(String[] data){
        OpenDetailsFragment fragment = new OpenDetailsFragment();
        final Bundle args = new Bundle();
        args.putString("news_category",data[0]);
        args.putString("news_title",data[1]);
        args.putString("new_description",data[2]);
        args.putString("news_date",data[3]);
        args.putString("news_image",data[4]);
        fragment.setArguments(args);

        FragmentTransaction transaction =((FragmentActivity)getContext()).getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        layout_dots = root. findViewById(R.id.layout_dots);
        viewPager = root. findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(getActivity(), new ArrayList<Image>());
        final List<Image> items = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            Image obj = new Image();
            obj.image = array_image_place[i];
            obj.imageDrw = getResources().getDrawable(obj.image);
            obj.name = array_title_place[i];
            obj.brief = array_brief_place[i];
            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView)root.findViewById(R.id.title)).setText(items.get(0).name);
        ((TextView)root.findViewById(R.id.brief)).setText(items.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) root.findViewById(R.id.title)).setText(items.get(pos).name);
                ((TextView) root.findViewById(R.id.brief)).setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());



        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getActivity());
        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Data...");
        // Showing progress dialog.
        progressDialog.show();

        mNes = new ArrayList<> ();
        mAdapter = new NewsAdapter (getActivity(), mNes);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(HomeFragment.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("News");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                mNes.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    News upload = teacherSnapshot.getValue(News.class);
//                    upload.setNewsId(teacherSnapshot.getKey());
                    mNes.add(upload);
                }
                mAdapter.notifyDataSetChanged();
               progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        return  root;
    }

    public void onItemClick(int position) {
        News clickedNews=mNes.get(position);
        String[] teacherData={clickedNews.getNews_category(),clickedNews.getNews_title(),clickedNews.getNews_description(),clickedNews.getNews_date(),clickedNews.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        News clickedNews=mNes.get(position);
        String[] teacherData={clickedNews.getNews_category(),clickedNews.getNews_title(),clickedNews.getNews_description(),clickedNews.getNews_date(),clickedNews.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onDeleteItemClick(int position) {

    }


    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.image);
            lyt_parent.setOnClickListener(v1 -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v1, o);
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_dashboard );
    }

}
