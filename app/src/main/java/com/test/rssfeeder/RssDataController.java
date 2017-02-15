package com.test.rssfeeder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nishant- on 16-11-2015.
 */
public class RssDataController extends AsyncTask<String,Void,ArrayList<RssDataModel>> {

    private String title = "title";
    private String link = "link";
    private String imgLink = "imgLink";
    private String description = "description";
    private String pubDate = "pubDate";
    private String urlString = "http://feeds.feedburner.com/AndroidPolice?format=xml";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    ArrayList<RssDataModel> dataModelList = new ArrayList<RssDataModel>();
    Context mContext;
    int listCount = 0;
    public MainActivity delegate=null;

    public RssDataController(Context context)
    {
        super();
        mContext = context;
        delegate = (MainActivity) context;

    }
    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList<RssDataModel> getDataModelList()
    {
        return dataModelList;
    }

    private String helperString(String str) {

        String img = "";
        //parse description for any image or video links
            if (str.contains("<img ")){
                img = str.substring(str.indexOf("<img "));
                String cleanUp = img.substring(0, img.indexOf(">") + 1);
                img = img.substring(img.indexOf("src=") + 5);
                int indexOf = img.indexOf("\"");
                if (indexOf==-1){
                    indexOf = img.indexOf("\"");
                }
                img = img.substring(0, indexOf);
            }
            return img;
    }



    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        RssDataModel rssDataModel = null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){

                    case XmlPullParser.START_TAG:
                        if(name.equals("item"))
                        {
                            rssDataModel = new RssDataModel();
                        }else if(rssDataModel != null)
                        {
                            if(name.equals("title"))
                            {
                                rssDataModel.itemTitle = myParser.nextText();
                            }else if(name.equals("description"))
                            {
                                rssDataModel.itemImage = helperString(myParser.nextText());
                            }else if(name.equals("link"))
                            {
                                rssDataModel.itemDescription = myParser.nextText();
                            }
                            else if(name.equals("pubDate"))
                            {
                                rssDataModel.pubDate = myParser.nextText();
                            }
                        }

                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();

                        break;

                    case XmlPullParser.END_TAG:
                        name=myParser.getName();
                        if(name.equals("item") && rssDataModel != null)
                        {
                            dataModelList.add(rssDataModel);
                        }
                        /*
                            if(name.equals("title")) {
                                title = text;
                                myParser.next();
                                if(link != ""){
                                    myParser.next();
                                    imgLink = helperString(myParser.nextText());
                                    rssDataModel = new RssDataModel();
                                    rssDataModel.itemTitle = title;
                                    rssDataModel.itemDescription = link;
                                    rssDataModel.itemImage = imgLink;
                                    dataModelList.add(rssDataModel);//Add the new data to the list
                                }
                                else
                                {
                                    title = null;
                                    link = null;
                                    imgLink = null;
                                }
                            }
                        if(name.equals("link"))
                        {
                            link = text;
                        }
*/
                        break;

                }

                event = myParser.next();
            }

            parsingComplete = false;

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<RssDataModel> result) {
        super.onPostExecute(result);
        //Create a list adapter
        //RssAdapter mAdapter = new RssAdapter(mContext,result);
        //delegate.setmAdapter(mAdapter);
        //Create a Card Adapter
        RssCardAdapter mCardAdapter = new RssCardAdapter(result);
        delegate.setmCardAdapter(mCardAdapter);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<RssDataModel> doInBackground(String... params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);
            Log.v("ADDING ITEM", "NAME ");
            parseXMLAndStoreIt(myparser);
            stream.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return dataModelList;
    }
}
