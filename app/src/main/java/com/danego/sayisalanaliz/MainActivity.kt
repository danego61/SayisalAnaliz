package com.danego.sayisalanaliz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

enum class btn{
    iki,sabit,newton,secont
}

class MainActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        title="Sayısal Analiz"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_newton.setOnClickListener(this)
        btn_iki.setOnClickListener(this)
        btn_sabit.setOnClickListener(this)
        btn_secont.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val btn = p0 as Button
        val intent = Intent(this, HesaplaActivity::class.java)
        when (btn.text) {
            "İkiye Bölünme" -> {
                intent.putExtra("btn", com.danego.sayisalanaliz.btn.iki)
            }
            "Sabit Nokta" -> {
                intent.putExtra("btn", com.danego.sayisalanaliz.btn.sabit)
            }
            "Newton-Raphson" -> {
                intent.putExtra("btn", com.danego.sayisalanaliz.btn.newton)
            }
            "Secont" -> {
                intent.putExtra("btn", com.danego.sayisalanaliz.btn.secont)
            }
        }
        startActivity(intent)
    }
}
