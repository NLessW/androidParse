package com.cookandroid.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class Baseball extends AppCompatActivity {

    BbTask task;
    TextView sName, sBirth, sCompany, sTeam, sPos, searchName, batAvg, hrAvg, safeAvg, hpAvg, bat, homerun, safe, hitPoint, season,sbody;
    ImageView sProfile, playerTeam, teamL;
    String na;
    LinearLayout nameBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseball);
        setTitle("야구선수 검색 결과");

        Intent intent = getIntent();
        na = intent.getStringExtra("HumanSearch");

        sName = (TextView) findViewById(R.id.sName);
        searchName = (TextView) findViewById(R.id.searchName);
        sBirth = (TextView) findViewById(R.id.sBirth);
        sCompany = (TextView) findViewById(R.id.sCompany);
        sTeam = (TextView) findViewById(R.id.sTeam);
        sPos = (TextView) findViewById(R.id.sPos);
        batAvg = (TextView) findViewById(R.id.batAvg);
        hrAvg = (TextView) findViewById(R.id.hrAvg);
        safeAvg = (TextView) findViewById(R.id.safeAvg);
        hpAvg = (TextView) findViewById(R.id.hpAvg);
        bat = (TextView) findViewById(R.id.bat);
        homerun = (TextView) findViewById(R.id.homeRun);
        safe = (TextView) findViewById(R.id.safe);
        hitPoint = (TextView) findViewById(R.id.hitPoint);
        season = (TextView) findViewById(R.id.season);
        sbody = (TextView) findViewById(R.id.sBody);
        sProfile = (ImageView) findViewById(R.id.sProfile);
        playerTeam = (ImageView) findViewById(R.id.playerTeam);
        teamL = (ImageView) findViewById(R.id.teamL);
        nameBack = (LinearLayout) findViewById(R.id.nameBack);


        if (na.length() != 0) {
            task = new BbTask();
            task.execute(na);

        }

    }


    public void HumanSearch(View v) {

    }

    private class BbTask extends AsyncTask<String, String, String> {
        String[] results = new String[20];
        Bitmap bit = null;
        Bitmap bit1 = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=야구선수" + na)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .get();
                Elements section = doc.select("section._au_people_content_wrap");

                if(!section.isEmpty()){
                    Elements Info = doc.select("div.cm_info_box div.detail_info");
                    Elements logo = doc.select("div.cm_top_wrap");
                    bit = getBitmapFromURL(Info.get(0).select("a > img").attr("src"));
                    bit1 = getBitmapFromURL(logo.get(0).select("a>img").attr("src"));
                    Elements number = logo.select("span.state_end");
                    results[15] = number.get(0).text();
                    Elements jobInfo = doc.select("div.first_elss");
                    Elements txta = jobInfo.select("span.txt");
                    results[5] = txta.get(0).text();
                    results[4] = txta.get(1).text();
                    Elements dd = Info.select("div.info_group dd");
                    results[0] = dd.get(0).text();//생일
                    results[1] = dd.get(1).text();//신체
                    results[2] = dd.get(2).text();//소속사
                    results[3] = dd.get(5).text();//소속팀
                    Elements recordDiv = doc.select("div.record_info");
                    Elements sea = recordDiv.select("span.sub_text");
                    Elements recordUl = recordDiv.select("ul.list > li.item");
                    Elements rb = recordUl.select("span.sub_info");
                    Elements bat = recordUl.select("span.num_info");
                    results[6] = bat.get(0).text();//타율
                    results[7] = bat.get(1).text();//홈런
                    results[8] = bat.get(2).text();//안타
                    results[9] = bat.get(3).text();//타점
                    results[10] = rb.get(0).text(); //타율 or 평균자책
                    results[11] = rb.get(1).text(); //홈런 or 승수
                    results[12] = rb.get(2).text(); //안타 or 이닝
                    results[13] = rb.get(3).text(); //타점 or 삼진
                    results[14] = sea.get(0).text();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) { //테스크가 끝나고 할 작업
            super.onPostExecute(s);
            if(bit != null) {
                sProfile.setImageBitmap(bit);
            }
            if(bit1 != null){
                playerTeam.setImageBitmap(bit1);
            }
            if(results.length!=0){
                searchName.setText("야구선수 "+ na + "의 프로필 " + results[15]);
                sName.setText("이름 : " + na);
                sBirth.setText("출생 : " + results[0]);
                sbody.setText("신체 : " + results[1]);
                sCompany.setText("소속사 : " + results[2]);
                sTeam.setText("소속팀 : " + results[5]);
                sPos.setText("포지션 : " + results[4]);
                batAvg.setText(results[6]);
                bat.setText(results[10] + " : ");
                homerun.setText(results[11]+ " : ");
                safe.setText(results[12]+ " : ");
                hitPoint.setText(results[13]+ " : ");
                hrAvg.setText(results[7]);
                safeAvg.setText(results[8]);
                hpAvg.setText(results[9]);
                season.setText("("+results[14]+")");

                switch (results[5]){
                    case "SSG 랜더스":
                        teamL.setImageResource(R.drawable.ssglogo);
                        nameBack.setBackgroundColor(Color.parseColor("#C1253A"));
                        break;
                    case "키움 히어로즈":
                        teamL.setImageResource(R.drawable.kiwoomlogo);
                        nameBack.setBackgroundColor(Color.parseColor("#4F001F"));
                        break;
                    case "LG 트윈스":
                        teamL.setImageResource(R.drawable.lglogo);
                        break;
                    case "kt wiz":
                        teamL.setImageResource(R.drawable.ktlogo);
                        nameBack.setBackgroundColor(Color.parseColor("#ED1B23"));
                        break;
                    case "KIA 타이거즈":
                        teamL.setImageResource(R.drawable.kialogo);
                        break;
                    case "NC 다이노스":
                        teamL.setImageResource(R.drawable.nclogo);
                        nameBack.setBackgroundColor(Color.parseColor("#214778"));
                        break;
                    case "삼성 라이온즈":
                        teamL.setImageResource(R.drawable.samsunglogo);
                        nameBack.setBackgroundColor(Color.parseColor("#0066B3"));
                        break;
                    case "롯데 자이언츠":
                        teamL.setImageResource(R.drawable.lottelogo);
                        nameBack.setBackgroundColor(Color.parseColor("#0C2C55"));
                        break;
                    case "두산 베어스":
                        teamL.setImageResource(R.drawable.doosanlogo);
                        nameBack.setBackgroundColor(Color.parseColor("#0F0D2A"));
                        break;
                    case "한화 이글스":
                        teamL.setImageResource(R.drawable.hanhwalogo);
                        nameBack.setBackgroundColor(Color.parseColor("#F2501A"));
                        break;
                    default:
                        teamL.setImageResource(R.drawable.mlb);
                        break;

                }
            }
        }
    }
    public Bitmap getBitmapFromURL(String str) {
        Bitmap bit = null;
        try {
            URL url = new URL(str);
            bit = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return bit;

    }


}


