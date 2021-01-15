package com.example.alarmclock.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alarmclock.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsFragment extends Fragment {
    ListView lvTitle;
    ArrayList<String> arrayTitle,arrayLink,arrayImg;
    NewsAdapter adapter;
    TextView tvDateNews;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_news,container,false);




        tvDateNews = view.findViewById(R.id.tvDateNews);
        lvTitle = view.findViewById(R.id.lvTitle);
        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        arrayImg = new ArrayList<>();

        adapter = new NewsAdapter(getContext(),arrayTitle,arrayLink,arrayImg);

        lvTitle.setAdapter(adapter);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        tvDateNews.setText(formatter.format(date));

        new RSS().execute("https://vnexpress.net/rss/so-hoa.rss");

        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                intent.putExtra("title",arrayTitle.get(position));
                intent.putExtra("link",arrayLink.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
    private class RSS extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine())!=null){
                    content.append(line);
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();

            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeList1 = document.getElementsByTagName("description");

            for (int i =0;i<nodeList.getLength();i++){
                String cdata = nodeList1.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if(matcher.find()){
                    arrayImg.add(matcher.group(1));
                }
                Element element = (Element) nodeList.item(i);
                arrayTitle.add(parser.getValue(element,"title"));
                arrayLink.add(parser.getValue(element,"link"));
            }

            adapter.notifyDataSetChanged();

        }
    }
}
