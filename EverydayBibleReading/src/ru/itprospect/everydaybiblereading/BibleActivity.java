package ru.itprospect.everydaybiblereading;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BibleActivity extends ActionBarActivity {
	
	private PrefManager prefManager;
	
	
	private static final int SHOW_PREFERENCES = 1;
	private static final int SELECT_BOOK = 2;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Log.v("EBR", "BibleActivity: onCreate");
        
        setContentView(R.layout.bible_activity_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextFragment textFragment = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);

        Uri uri = getIntent().getData();
        if (uri != null && !textFragment.uriAlreadyGet) { 
        	String book = uri.getQueryParameter("book");
        	String chapter = uri.getQueryParameter("chapter");
        	textFragment.uriAlreadyGet = true;
        	
        	MainApp app = ((MainApp) getApplicationContext());
        	app.setBook(book);
        	app.setChapter(chapter);
        	
        	textFragment.UpdateText();
        }
        updateActFromPreferences();
        
    }
    
    public void SelectBook(String book, String type) {
    	Intent i = new Intent(this, SelectBookActivity.class);
    	i.putExtra("BOOK", book);
    	i.putExtra("TYPE", type);
    	startActivityForResult(i, SELECT_BOOK);
    }
    
    public void ShowPreferences() {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivityForResult(i, SHOW_PREFERENCES);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.e("EBR", "BibleActivity: onActivityResult " + requestCode);
		
		if (requestCode == SHOW_PREFERENCES) {
			updateActFromPreferences();
			TextFragment textFragment = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);
			textFragment.UpdateText();
			textFragment.updateFromPreferences();
		}
		else if (requestCode == SELECT_BOOK) {
			if (resultCode==RESULT_OK && data != null) {
				
				String book = data.getStringExtra("book");
				String chapter = data.getStringExtra("chapter");
				MainApp app = ((MainApp) getApplicationContext());
				app.setBook(book);
				app.setChapter(chapter);

				TextFragment textFragment = (TextFragment) getSupportFragmentManager().findFragmentById(R.id.text_fragment);
				textFragment.UpdateText();
				textFragment.SetPositionForTextElement(0,0);
			}
		}
	}
    
	private void updateActFromPreferences() {
		if (prefManager==null) {
			prefManager = new PrefManager(getApplicationContext());
		}
		
		int actBarBacId = prefManager.getColorSchemeActionBarId();
		Drawable d=getResources().getDrawable(actBarBacId);  
		getSupportActionBar().setBackgroundDrawable(d);
		
		
	}
	
    
}

