package com.asijack.tabhostdemo;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentFactory extends Fragment{
	private String content;
	private String defaultContent="default content";
	
	static FragmentFactory newInstance(String s){
		FragmentFactory newFragment=new FragmentFactory();
		Bundle bundle=new Bundle();
		bundle.putString("content", s);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Bundle args=getArguments();
		content=args!=null?args.getString("content"):defaultContent;		
		View view= inflater.inflate(R.layout.guide_2, container,false);
		TextView tv = (TextView) view.findViewById(R.id.tv);
		tv.setText(content);
		return view;
	}
		
}
