/**
 * 
 */
package sync;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import models.RssItem;
import models.RssLatestDate;
import models.SortedArrayList;
import adapters.RssItemAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.util.Log;
import database.RssItemsDataSource;

/**
 * @author Li Li
 * 
 */
public class RssAdapter extends AsyncTask<Void, Void, SortedArrayList<RssItem>> {

	protected String icon;
	protected String url;
	protected RssItemAdapter<RssItem> adapter;
	protected RssItemsDataSource rssItemsDataSource;
	protected SortedArrayList<RssItem> rssItems;
	protected Activity activity;
	protected RssLatestDate latestDate;
	protected boolean ableToRemoveSplashScreen;
	
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setRssItems(SortedArrayList<RssItem> rssItems) {
		this.rssItems = rssItems;
	}

	public void setListAdapter(RssItemAdapter<RssItem> adapter) {
		this.adapter = adapter;
	}

	public void setDataSource(RssItemsDataSource rssItemsDataSource) {
		this.rssItemsDataSource = rssItemsDataSource;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setAbleToRemoveSplashScreen(){
		this.ableToRemoveSplashScreen = true;
	}

	@Override
	protected SortedArrayList<RssItem> doInBackground(Void... params) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			RssHandler handler = new RssHandler();
			handler.setIcon(icon);
			saxParser.parse(url, handler);
			return handler.getItems();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(SortedArrayList<RssItem> result) {
		if (result != null) {
			if (this.rssItemsDataSource != null)
				this.rssItemsDataSource.addRssItems(result);
			for (RssItem rssItem: result){
				this.rssItems.insertSorted(rssItem);
			}
			adapter.notifyDataSetChanged();
		}

		updateLatestDate();
		
		if (activity != null && ableToRemoveSplashScreen == true) {
			removeSplashScreen();
		}
	}

	protected void updateLatestDate() {
		if (this.rssItems != null){
			latestDate = RssLatestDate.getInstance(activity);
			latestDate.update(this.rssItems.get(0).getPubDateObject());
		}
	}

	private void removeSplashScreen() {
		FragmentManager fm = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		Fragment splashScreen = fm.findFragmentByTag("SplashScreen");
		if (splashScreen == null) {
			Log.e("Cannot find spashscreen by tag",
					"Cannot find spashscreen by tag");
		} else {
			fragmentTransaction.remove(splashScreen);
		}
		fragmentTransaction.commit();
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
