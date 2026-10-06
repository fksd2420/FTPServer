package com.example.mylibrary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class VPCustomAdapter extends FragmentStateAdapter {
    private Context context = null;
    public ArrayList<Fragment> Fragments = new ArrayList<>();

    public VPCustomAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @Override
    public int getItemCount() {
        return Fragments.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return Fragments.get(position);
    }

}
//public class ViewPagerCustomAdapter extends RecyclerView.Adapter<ViewPagerCustomAdapter.ViewHolder> {
//
//    private Context context = null;
//    FragmentManager fragmentManager;
//
//    String fragmentTagPrefix = "viewpager";
//
//    public ViewPagerCustomAdapter(Context context) {
//        this.context = context;
//        fragmentManager = ((MainActivity)context).getSupportFragmentManager();
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_template, parent, false);
//        view.setId(View.generateViewId());
//
//        var rl = new RelativeLayout(context);
//        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        rl.setLayoutParams(lp);
//        rl.setId(View.generateViewId());
//        return new ViewHolder(rl);
//    }
//
//    public ArrayList<Fragment> Fragments = new ArrayList<>();
//    // This method binds the screen with the view
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.fragment = Fragments.get(position);
//    }
//
//    @Override
//    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        if (fragmentManager.findFragmentById(holder.root.getId()) == null) {
//            fragmentManager.beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(holder.root.getId(), holder.fragment)
//                    .commit();
//            fragmentManager.executePendingTransactions();
//        }
//    }
//    // This Method returns the size of the Array
//    @Override
//    public int getItemCount() {
//        return Fragments.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        RelativeLayout root;
//        Fragment fragment;
//        int position=-1;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            root =itemView.findViewById(R.id.viewpager_template_root);
//            root= (RelativeLayout)itemView;
//            android.util.Log.i("abcc" , " " + root.getId());
//        }
//    }
//}
