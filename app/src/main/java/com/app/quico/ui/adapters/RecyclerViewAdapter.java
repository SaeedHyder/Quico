package com.app.quico.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.app.quico.interfaces.LoadMoreChatInterface;
import com.app.quico.interfaces.LoadMoreListener;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/10/2017.
 */

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewBinder.BaseViewHolder> {
    private List<T> collections = new ArrayList<>();
    private RecyclerViewBinder<T> viewBinder;
    private Context mContext;
    private LoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(LoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public RecyclerViewAdapter(List<T> collections, RecyclerViewBinder<T> viewBinder, Context context) {
        this.collections = collections;
        this.viewBinder = viewBinder;
        this.mContext = context;

    }

    @Override
    public RecyclerViewBinder.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return (RecyclerViewBinder.BaseViewHolder) this.viewBinder.createViewHolder(this.viewBinder.createView(this.mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerViewBinder.BaseViewHolder holder, int position) {
        T entity = (T) this.collections.get(position);
        this.viewBinder.bindView(entity, position, holder, this.mContext);
        if (collections.size() > 8) {

            if (position == collections.size() - 1) {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMoreItem(position);
                }
            }

        }

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.collections.size();
    }

    public T getItemFromList(int index) {
        return collections.get(index);
    }

    public List<T> getList() {
        return collections;
    }

    /**
     * Clears the internal list
     */
    public void clearList() {
        collections.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a entity to the list and calls {@link #notifyDataSetChanged()}.
     * Should not be used if lots of NotificationDummy are added.
     *
     * @see #addAll(List)
     */
    public void add(T entity) {
        collections.add(entity);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        collections.remove(position);
        notifyDataSetChanged();
    }



    /**
     * Adds a NotificationDummy to the list and calls
     * {@link #notifyDataSetChanged()}. Can be used {
     * {@link List#subList(int, int)}.
     *
     * @see #addAll(List)
     */
    public void addAll(List<T> entityList) {
        collections.addAll(entityList);
        notifyDataSetChanged();
    }


    public void addAllStart(List<T> entityList) {
        collections.addAll(entityList);
        // notifyDataSetChanged();
    }

    public void addStart(T entityList) {
        collections.add(0, entityList);
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }


}
