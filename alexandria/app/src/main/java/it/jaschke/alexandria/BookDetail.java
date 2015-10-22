package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.data.BookDetailModel;
import it.jaschke.alexandria.services.BookService;


public class BookDetail extends FragmentBase implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EAN_KEY = "EAN";
    private final int LOADER_ID = 10;
    private View rootView;
    private String ean;
    private String bookTitle;
    private ShareActionProvider shareActionProvider;
    private BookDetailModel mBookDetailModel;

    public BookDetail(){
        mBookDetailModel = new BookDetailModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            ean = arguments.getString(BookDetail.EAN_KEY);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean);
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);

                Snackbar.make(rootView, R.string.snack_book_deleted, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snack_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), BookService.class);
                                intent.putExtra(BookService.BOOK_DETAIL_MODELE, mBookDetailModel);
                                intent.setAction(BookService.UNDO_DELETE_BOOK);
                                getActivity().startService(intent);
                            }
                        })
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                if (event != DISMISS_EVENT_ACTION) {
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }
                        })
                .show();
            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(ean)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        ((TextView) rootView.findViewById(R.id.fullBookTitle)).setText(bookTitle);

        mBookDetailModel.setTitleBook(bookTitle);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + bookTitle);
        shareActionProvider.setShareIntent(shareIntent);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        ((TextView) rootView.findViewById(R.id.fullBookSubTitle)).setText(bookSubTitle);

        mBookDetailModel.setSubTitle(bookSubTitle);

        String desc = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        ((TextView) rootView.findViewById(R.id.fullBookDesc)).setText(desc);

        mBookDetailModel.setDesc(desc);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        mBookDetailModel.setAuthors(authors);
        // Do operation only if authors exists
        if (authors != null) {
            String[] authorsArr = authors.split(",");
            ((TextView) rootView.findViewById(R.id.authors)).setLines(authorsArr.length);
            ((TextView) rootView.findViewById(R.id.authors)).setText(authors.replace(",", "\n"));
        }

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        mBookDetailModel.setImgUrl(imgUrl);
        if(Patterns.WEB_URL.matcher(imgUrl).matches()){
            Glide.with(getActivity())
                    .load(imgUrl)
                    .into((ImageView) rootView.findViewById(R.id.fullBookCover));
            rootView.findViewById(R.id.fullBookCover).setVisibility(View.VISIBLE);
        }

        String pubishedOn = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.PUBLISHED_DATE));

        mBookDetailModel.setPublishedOn(pubishedOn);

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = simpleDateFormat.parse(pubishedOn);
            simpleDateFormat.applyPattern("dd MMMM yyyy");
            ((TextView) rootView.findViewById(R.id.publishedOn)).setText(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String publisher = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.PUBLISHER));
        mBookDetailModel.setPublisher(publisher);
        if (publisher != null && !publisher.isEmpty()) {
            rootView.findViewById(R.id.publisher).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.publisherTitle).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.publisher)).setText(publisher);
        } else {
            rootView.findViewById(R.id.publisher).setVisibility(View.GONE);
            rootView.findViewById(R.id.publisherTitle).setVisibility(View.GONE);
        }

        String isbn = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry._ID));
        ((TextView) rootView.findViewById(R.id.isbn)).setText(isbn);

        mBookDetailModel.setIsbn(isbn);

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        mBookDetailModel.setCategories(categories);
        if (categories != null && !categories.isEmpty()) {
            rootView.findViewById(R.id.categories).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.categoriesTitle).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.categories)).setText(categories);
        } else {
            rootView.findViewById(R.id.categories).setVisibility(View.GONE);
            rootView.findViewById(R.id.categoriesTitle).setVisibility(View.GONE);
        }

        int pageCount = data.getInt(data.getColumnIndex(AlexandriaContract.BookEntry.PAGE_COUNT));
        mBookDetailModel.setPages(pageCount);
        if (pageCount != 0) {
            rootView.findViewById(R.id.pageCount).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.pageTitle).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.pageCount)).setText(String.valueOf(pageCount));
        } else {
            rootView.findViewById(R.id.pageCount).setVisibility(View.GONE);
            rootView.findViewById(R.id.pageTitle).setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentBaseListener = (FragmentBaseListener) activity;
        mFragmentBaseListener.setToolBar(activity.getString(R.string.book_detail_name));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentBaseListener = null;
    }
}