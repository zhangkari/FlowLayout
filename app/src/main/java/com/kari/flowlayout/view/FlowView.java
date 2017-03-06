package com.kari.flowlayout.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class FlowView extends ViewGroup {

    private FlowAdapter mAdapter;
    private FlowAdapterObserver mAdapterObserver;

    public FlowView(Context context) {
        super(context);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        MarginLayoutParams params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());
        params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());
        params.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());
        params.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());
        return params;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed || mAdapter == null) {
            return;
        }

        int count = mAdapter.getCount();
        final int WIDTH = getMeasuredWidth();
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            if (lineWidth + child.getMeasuredWidth() + params.leftMargin + params.rightMargin <= WIDTH) {
                child.layout(width + params.leftMargin,
                        height + params.topMargin,
                        width + params.leftMargin + child.getMeasuredWidth(),
                        height + params.topMargin + child.getMeasuredHeight());

                lineWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                width = Math.max(width, lineWidth);
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);
            } else {
                width = 0;
                height += lineHeight;
                child.layout(width + params.leftMargin,
                        height + params.topMargin,
                        width + params.leftMargin + child.getMeasuredWidth(),
                        height + params.topMargin + child.getMeasuredHeight());

                lineWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                width = Math.max(width, lineWidth);
                lineHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mAdapter == null) {
            return;
        }
        int count = mAdapter.getCount();
        final int WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        final int HEIGHT = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            if (lineWidth + child.getMeasuredWidth() + params.leftMargin + params.rightMargin <= WIDTH) {
                lineWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                width = Math.max(width, lineWidth);
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);
            } else {
                height += lineHeight;
                lineWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                width = Math.max(width, lineWidth);
                lineHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }

            if (i == count - 1) {
                height += lineHeight;
            }
        }

        setMeasuredDimension(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? WIDTH : width,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ? HEIGHT : height);
    }

    public void setAdapter(FlowAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter must be null !");
        }
        mAdapter = adapter;
        if (mAdapterObserver == null) {
            mAdapterObserver = new FlowAdapterObserver();
        }
        adapter.registerDataSetObserver(mAdapterObserver);
        takeAdapterEffect();
    }

    private void takeAdapterEffect() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            addView(mAdapter.getView(i, null, this));
        }
        requestLayout();
    }

    class FlowAdapterObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            takeAdapterEffect();
        }

        @Override
        public void onInvalidated() {
            takeAdapterEffect();
        }
    }
}
