package com.example.gabriele.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gabriele on 01/07/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * ViewHolder for fields of the News.
     */
    static class ViewHolder {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView dateTextView;
    }

    /**
     * @param context The current context. Used to inflate the layout file.
     * @param news   A List of News objects to display in a list.
     */

    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        final News currentNews = getItem(position);
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_activity, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
            holder.sectionTextView = (TextView) listItemView.findViewById(R.id.news_section);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
            listItemView.setTag(holder);

        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        holder.titleTextView.setText(currentNews.getTitle());
        holder.sectionTextView.setText(currentNews.getSection());
        holder.dateTextView.setText(currentNews.getDate());

        return listItemView;
    }
}
