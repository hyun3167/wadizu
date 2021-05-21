package com.example.wadizu

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.settings.javaScriptEnabled = true //자바 스크립트 허용
        /*웹뷰테서 새 창이 뜨지않도록 방지하는 구문*/
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        /*웹뷰테서 새 창이 뜨지않도록 방지하는 구문*/
        webView.loadUrl("https://www.naver.com")
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager //위치관리자 정보

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListner())

    }
    internal inner class locationListner : LocationListener {
        override fun onLocationChanged(p0: Location) {
            val geocoder : Geocoder = Geocoder(this@MainActivity) //위치 -> 주소
            var list = geocoder.getFromLocation(p0.latitude, p0.longitude, 1) as List<Address> // 주소배열 -> list 최대
            text1.text = "위도 : "+p0.latitude+"\n경도 : "+ p0.longitude+"\n 주소 : "+ list[0].getAddressLine(0)
            try {
                var output = openFileOutput("myFile.txt", Context.MODE_PRIVATE)
                var dos = DataOutputStream(output)

                dos.writeUTF("위도 : "+p0.latitude+"\n경도 : "+ p0.longitude+"\n 주소 : "+ list[0].getAddressLine(0))
                dos.flush()
                dos.close()

            }catch (e : Exception){
                e.printStackTrace()
            }
            button2.setOnClickListener{ view ->
                try {
                    var  input = openFileInput("myFile.txt")
                    var dis = DataInputStream(input)

                    var value1 = dis.readUTF()
                    dis.close()

                    text1.append("value3 : ${value1}\n")


                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}