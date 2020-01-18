package com.acxca.ava.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acxca.ava.presentation.R;

/**
 * Created by lion on 18-2-28.
 */

public class TabView extends FrameLayout {
    private static final String TAG = "====_TabView";
    private TextView mTvTitle;
    private ImageView mIvIcon;

    public TabView(Context context) {
        this(context, null, 0);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        String title = ta.getString(R.styleable.TabView_tabTitle);
        int drawable = ta.getResourceId(R.styleable.TabView_tabDrawable, 0);
        ta.recycle();

        if (TextUtils.isEmpty(title)) {
            Log.e(TAG, "TabView.refresh()" + "title is empty");
        }

        if (drawable == 0) {
            Log.e(TAG, "TabView.refresh()" + "drawable is empty");
        }


        View view = LayoutInflater.from(context).inflate(R.layout.view_tab, this, false);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mIvIcon = (ImageView) view.findViewById(R.id.iv_icon);

        mTvTitle.setText(title);
        mIvIcon.setImageResource(drawable);

        addView(view);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mTvTitle.setSelected(selected);
        mIvIcon.setSelected(selected);
    }
}
