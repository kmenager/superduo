package it.jaschke.alexandria.api;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;

/**
 * Created by kevin.menager on 25/09/2015.
 */
public class BookAdapter extends CursorRecyclerViewAdapter<BookAdapter.ViewHolder> {

    private final Context mContext;

    public BookAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);
        viewHolder.bindView(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mBookCover;
        public final TextView mBookTitle;
        public final TextView mAuthors;

        public ViewHolder(View itemView) {
            super(itemView);
            mBookCover = (ImageView) itemView.findViewById(R.id.fullBookCover);
            mBookTitle = (TextView) itemView.findViewById(R.id.listBookTitle);
            mAuthors = (TextView) itemView.findViewById(R.id.authors);
            itemView.setOnClickListener(this);
        }

        public void bindView(Cursor cursor) {
            String imgUrl = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(mBookCover);

            String bookTitle = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
            mBookTitle.setText(bookTitle);

            Cursor authorEntry = mContext.getContentResolver().query(
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
                    mAuthors.setLines(authorsArr.length);
                    mAuthors.setText(authors.replace(",", "\n"));
                    authorEntry.close();
                }
            }
        }

        @Override
        public void onClick(View v) {
            Cursor cursor = getCursor();
            if (cursor != null && cursor.moveToPosition(getAdapterPosition())) {
                ((Callback) mContext)
                        .onItemSelected(cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID)));
            }
        }
    }
}
