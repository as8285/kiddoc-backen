	package com.buddy.kiddoc.util;

	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.Reader;
	import java.net.HttpURLConnection;
	import java.net.MalformedURLException;
	import java.net.URL;
	import java.text.SimpleDateFormat;

	import org.apache.ibatis.io.Resources;
	import org.apache.ibatis.session.SqlSession;
	import org.apache.ibatis.session.SqlSessionFactory;
	import org.apache.ibatis.session.SqlSessionFactoryBuilder;
	import org.json.JSONArray;
	import org.json.JSONObject;

import com.buddy.kiddoc.model.vo.Hospital;

	public class JSONHospital {

		public static void main(String[] args) {
			
			String url = "https://openapi.gg.go.kr/TbChildnatnPrvntncltnmdnstM?KEY=7eeb0d5d882e405db94759ca79a24c07&Type=json&pIndex=1&pSize=100";
			
			try {
				
				Reader r = Resources.getResourceAsReader("mybatis-config.xml");
				SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(r);
				SqlSession session = factory.openSession();
				
				URL requestUrl = new URL(url);
				HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
				urlConnection.setRequestMethod("GET");
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String line = null;
				
				StringBuffer responseBuffer = new StringBuffer();
				
				while((line=br.readLine())!=null) {
					responseBuffer.append(line);
				}
				br.close();
				urlConnection.disconnect();
				
				// JSON 파싱 시작
				String responseData = responseBuffer.toString();
				JSONObject jsonResponse = new JSONObject(responseData);
				
				JSONObject TbChildnatnPrvntncltnmdnstM = jsonResponse.getJSONObject("TbChildnatnPrvntncltnmdnstM");
				JSONArray row = TbChildnatnPrvntncltnmdnstM.getJSONArray("row");
				
				for(int i=0; i<row.length(); i++) {
					JSONObject result = row.getJSONObject(i);
					String  sigunNm = result.getString("SIGUN_NM");
					String facltNm = result.getString("FACLT_NM");
					String telNo = result.getString("TELNO");
					System.out.println("SIGUN_NM : " + sigunNm);
					System.out.println("FACLT_NM : " + facltNm);
					System.out.println("TELNO : " + telNo);
					System.out.println();	
					Hospital hospital = new Hospital();
					hospital.setSigunNm(sigunNm);
					hospital.setFacltNm(facltNm);
					hospital.setTelno(telNo);	
					session.insert("hospitalMapper.insertHospital", hospital);
					session.commit();
					
					
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

