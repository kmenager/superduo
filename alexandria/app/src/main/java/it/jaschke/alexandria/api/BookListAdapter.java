package it.jaschke.alexandria.api;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;

/**
 * Created by saj on 11/01/15.
 */
public class BookListAdapter extends CursorAdapter {


    public static class ViewHolder {
        public final ImageView bookCover;
        public final TextView bookTitle;
        public final TextView authors;

        public ViewHolder(View view) {
            bookCover = (ImageView) view.findViewById(R.id.fullBookCover);
            bookTitle = (TextView) view.findViewById(R.id.listBookTitle);
            authors = (TextView) view.findViewById(R.id.authors);
        }
    }

    public BookListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String imgUrl = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        Glide.with(context)
                .load(imgUrl)
                .into(viewHolder.bookCover);

        String bookTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        viewHolder.bookTitle.setText(bookTitle);

        Cursor authorEntry = context.getContentResolver().query(
                AlexandriaContract.AuthorEntry.buildAuthorUri(cursor.getLong(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID))),
                null,
                null,
                null,
                null);
        if(authorEntry != null && !authorEntry.moveToFirst()){
            authorEntry.close();
        } else {
            String authors = authorEntry.getString(authorEntry.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
            // Do operation only if authors exists
            if (authors != null) {
                String[] authorsArr = authors.split(",");
                viewHolder.authors.setLines(authorsArr.length);
                viewHolder.authors.setText(authors.replace(",", "\n"));
                authorEntry.close();
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }
}
