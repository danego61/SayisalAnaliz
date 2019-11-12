package com.danego.sayisalanaliz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_metod.*
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.*

class HesaplaActivity : AppCompatActivity() {

    private lateinit var type: btn
    private val texts = listOf(
        "P0:P1:ε:Digit Yuvarlama",
        "P0:ε:Digit Yuvarlama",
        "P0:ε:Digit Yuvarlama",
        "A1:A2:ε:Digit Yuvarlama"
    )
    private val fsecont = listOf("x^3+cosx=0", "cosx+2sinx+x^2)=0", "cosx=x")
    private val fnewton = listOf("e^x-5sin(π*x/2=0", "x^2-sinx-1=0", "cosx-x=0")
    private val fsabit =
        listOf("(x^2-1)/3", "ln(1+2x)", "e^x-1-2x", "√(x+2)", "(3x+20)^(1/3)", "x^4-3x^2-3")
    private val fiki = listOf(
        "x^3+4x^2-10",
        "x^2-3",
        "e^-x-sinx",
        "√x-cosx",
        "x^3-7x^2+14x-6",
        "x^4-2x^3-4x^2+4x+4",
        "x-2^-x",
        "e^x-x^2+3x+2",
        "2xcos(2x)-(x+1)^2",
        "x^3+4x^2-10"
    )
    private lateinit var function: (x: Double) -> Double
    private lateinit var functiont: (x: Double) -> Double
    private val max = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metod)
        type = intent.getSerializableExtra("btn") as btn
        when (type) {
            btn.secont -> {
                title="Secont (Kirişler) Yöntemi"
                txt_islem.hint = texts[0]
                functions.adapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fsecont)
            }
            btn.newton -> {
                title="Newton-Raphson Metodu"
                txt_islem.hint = texts[1]
                functions.adapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fnewton)
            }
            btn.sabit -> {
                title="Sabit Nokta İterasyonu"
                txt_islem.hint = texts[2]
                functions.adapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fsabit)
            }
            btn.iki -> {
                title="İkiye Bölme Yöntemi"
                txt_islem.hint = texts[3]
                functions.adapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fiki)
            }
        }

        btn_hesapla.setOnClickListener {
            tablo.removeAllViews()
            when (type) {
                btn.secont -> {
                    when (functions.selectedItemPosition) {
                        0 -> {
                            function = ::fs1
                        }
                        1 -> {
                            function = ::fs2
                        }
                        2 -> {
                            function = ::fs3
                        }
                    }
                    val kes = txt_islem.text.split(':')
                    try {
                        SecontDongu(
                            kes[0].toDouble(),
                            kes[1].toDouble(),
                            kes[2].toDouble(),
                            kes[3].toInt()
                        )
                    } catch (e: Exception) {
                        Alert()
                    }
                }
                btn.newton -> {
                    when (functions.selectedItemPosition) {
                        0 -> {
                            function = ::fn1
                            functiont = ::fnt1
                        }
                        1 -> {
                            function = ::fn2
                            functiont = ::fnt2
                        }
                        2 -> {
                            function = ::fs3
                            functiont = ::fnt3
                        }
                    }
                    val kes = txt_islem.text.split(':')
                    try {
                        newtondongu(kes[0].toDouble(), kes[1].toDouble(), kes[2].toInt())
                    } catch (e: Exception) {
                        Alert()
                    }
                }
                btn.sabit -> {
                    when (functions.selectedItemPosition) {
                        0 -> {
                            function = ::fsa1
                        }
                        1 -> {
                            function = ::fsa2
                        }
                        2 -> {
                            function = ::fsa3
                        }
                        3 -> {
                            function = ::fsa4
                        }
                        4 -> {
                            function = ::fsa5
                        }
                        5 -> {
                            function = ::fsa6
                        }
                    }
                    val kes = txt_islem.text.split(':')
                    try {
                        sabitDongu(kes[0].toDouble(), kes[1].toDouble(), kes[2].toInt())
                    } catch (e: Exception) {
                        Alert()
                    }
                }
                btn.iki -> {
                    when (functions.selectedItemPosition) {
                        0 -> {
                            function = ::fi1
                        }
                        1 -> {
                            function = ::fi2
                        }
                        2 -> {
                            function = ::fi3
                        }
                        3 -> {
                            function = ::fi4
                        }
                        4 -> {
                            function = ::fi5
                        }
                        5 -> {
                            function = ::fi6
                        }
                        6 -> {
                            function = ::fi7
                        }
                        7 -> {
                            function = ::fi8
                        }
                        8 -> {
                            function = ::fi9
                        }
                        9 -> {
                            function = ::fi10
                        }
                    }
                    val kes = txt_islem.text.split(':')
                    try {
                        ikiDongu(
                            kes[0].toDouble(),
                            kes[1].toDouble(),
                            kes[2].toDouble(),
                            kes[3].toInt()
                        )
                    } catch (e: Exception) {
                        Alert()
                    }
                }
            }
        }
    }

    private fun SecontDongu(p00: Double, p11: Double, kesme: Double, yuvarlama: Int) {
        satirekle(listOf("n", "pn", "|pn-pn-1|"))
        var pne = 0.0
        var p0 = p00
        var p1 = p11
        for (k in 1..max) {
            var pn = SecontFun(p0, p1)
            pn = roundOffDecimal(pn, yuvarlama)
            val hata = roundOffDecimal(abs(pn - pne), yuvarlama)
            satirekle(listOf((k + 1).toString(), pn.toString(), hata.toString()))
            if (hata <= kesme)
                break
            pne = pn
            p0 = p1
            p1 = pn
        }
    }

    fun roundOffDecimal(number: Double, yuvarlama: Int): Double {
        var yvr = "#."
        for (k in 1..yuvarlama)
            yvr += "#"
        val df = DecimalFormat(yvr)
        df.roundingMode = RoundingMode.FLOOR
        var format = df.format(number)
        format = format.replace(',', '.')
        return format.toDouble()
    }

    private fun SecontFun(p0: Double, p1: Double): Double {
        var geri = p1
        geri -= (function(p1) * (p1 - p0)) / (function(p1) - function(p0))
        return geri
    }

    private fun satirekle(veri: List<String>) {
        val table = TableRow(this)
        table.dividerDrawable = getDrawable(R.drawable.divider)
        table.showDividers = TableRow.SHOW_DIVIDER_MIDDLE
        veri.forEach {
            val t = TextView(this)
            t.text = it
            t.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            t.textSize = 19.0F
            table.addView(t)
        }
        tablo.addView(table)
    }

    private fun Alert() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Hata")
        alert.setMessage("Yazım Hatası")
        alert.setIcon(android.R.drawable.ic_dialog_alert)
        alert.setPositiveButton("Tamam", null)
        alert.show()
    }

    private fun newtondongu(p00: Double, kesme: Double, yuvarlama: Int) {
        satirekle(listOf("n", "pn", "|pn-pn-1|"))
        var pne = 0.0
        var p0 = p00
        for (k in 1..max) {
            var pn = newtonfun(p0)
            pn = roundOffDecimal(pn, yuvarlama)
            val hata = roundOffDecimal(abs(pn - pne), yuvarlama)
            satirekle(listOf(k.toString(), pn.toString(), hata.toString()))
            if (hata <= kesme)
                break
            pne = pn
            p0 = pn
        }
    }

    private fun newtonfun(p0: Double): Double {
        var geri = p0
        geri -= function(p0) / functiont(p0)
        return geri
    }

    private fun sabitDongu(a1: Double, kesme: Double, yuvarlama: Int) {
        satirekle(listOf("pn", "f(pn)", "|pn-pn-1|"))
        var pe = 0.0
        var pn = a1
        for (k in 1..max) {
            var fpn = function(pn)
            fpn = roundOffDecimal(fpn, yuvarlama)
            var hata = abs(fpn - pe)
            hata = roundOffDecimal(hata, yuvarlama)
            satirekle(listOf(pn.toString(), fpn.toString(), hata.toString()))
            if (hata < kesme)
                break
            pn = fpn
            pe = fpn
        }
    }

    private fun ikiDongu(a1: Double, a2: Double, kesme: Double, yuvarlama: Int) {
        satirekle(listOf("an", "ap", "pn", "f(pn)"))
        var an: Double
        var ap: Double
        var pn: Double
        if (function(a1) < 0 && function(a2) > 0) {
            an = a1
            ap = a2
        } else if (function(a1) > 0 && function(a2) < 0) {
            an = a2
            ap = a1
        } else {
            Toast.makeText(this, "ADT Hatası!", Toast.LENGTH_SHORT).show()
            return
        }
        for (k in 1..max) {
            pn = (an + ap) / 2
            pn = roundOffDecimal(pn, yuvarlama)
            var fpn = function(pn)
            fpn = roundOffDecimal(fpn, yuvarlama)
            satirekle(listOf(an.toString(), ap.toString(), pn.toString(), fpn.toString()))
            if (abs(fpn) < kesme)
                break
            if (fpn < 0)
                an = pn
            else
                ap = pn
        }
    }

    private fun fs1(x: Double): Double {
        var geri = 0.0
        geri += x.pow(3)
        geri += cos(x)
        return geri
    }

    private fun fs2(x: Double): Double {
        var geri = 0.0
        geri += cos(x)
        geri += 2 * sin(x)
        geri += x.pow(2)
        return geri
    }

    private fun fs3(x: Double): Double {
        var geri = 0.0
        geri += cos(x)
        geri -= x
        return geri
    }

    private fun fn1(x: Double): Double {
        var geri = 0.0
        geri += Math.E.pow(x)
        geri -= 5 * sin(Math.PI * x / 2)
        return geri
    }

    private fun fn2(x: Double): Double {
        var geri = 0.0
        geri += x.pow(2)
        geri -= sin(x)
        geri -= 1
        return geri
    }

    private fun fnt1(x: Double): Double {
        var geri = 0.0
        geri += Math.E.pow(x)
        geri -= 5 * Math.PI / 2 * cos(Math.PI * x / 2)
        return geri
    }

    private fun fnt2(x: Double): Double {
        var geri = 0.0
        geri += 2 * x
        geri -= cos(x)
        return geri
    }

    private fun fnt3(x: Double): Double {
        var geri = 0.0
        geri -= sin(x)
        geri -= 1
        return geri
    }

    private fun fi10(x: Double): Double {
        var deger = 0.0
        deger += x.pow(3)
        deger += 4 * x.pow(2)
        deger -= 10
        return deger
    }

    private fun fi9(x: Double): Double {
        var deger = 0.0
        deger += 2 * x * cos(2 * x)
        deger -= (x + 1).pow(2)
        return deger
    }

    private fun fi8(x: Double): Double {
        var deger = 0.0
        deger += Math.E.pow(x)
        deger -= x.pow(2)
        deger += 3 * x
        deger += 2
        return deger
    }

    private fun fi7(x: Double): Double {
        var deger = x
        deger -= 2.0.pow(-x)
        return deger
    }

    private fun fi6(x: Double): Double {
        var deger = 0.0
        deger += x.pow(4)
        deger -= 2 * x.pow(3)
        deger -= 4 * x.pow(2)
        deger += 4 * x
        deger += 4
        return deger
    }

    private fun fi5(x: Double): Double {
        var deger = 0.0
        deger += x.pow(3)
        deger -= 7 * x.pow(2)
        deger += 14 * x
        deger -= 6
        return deger
    }

    private fun fi4(x: Double): Double {
        var deger = 0.0
        deger += x.pow(1.0 / 2)
        deger -= cos(x)
        return deger
    }

    private fun fi3(x: Double): Double {
        var deger = 0.0
        deger += Math.E.pow(-x)
        deger -= sin(x)
        return deger
    }

    private fun fi2(x: Double): Double {
        var deger = 0.0
        deger += x.pow(2)
        deger -= 3
        return deger
    }

    private fun fi1(x: Double): Double {
        var deger = 0.0
        deger += x.pow(3)
        deger += 4 * x.pow(2)
        deger -= 10
        return deger
    }

    private fun fsa6(x: Double): Double {
        var deger = 0.0
        deger += x.pow(4)
        deger -= 3 * x.pow(2)
        deger -= 3
        return deger
    }

    private fun fsa5(x: Double): Double {
        var deger = 0.0
        deger += ((3 * x) + 20).pow(1.0 / 3)
        return deger
    }

    private fun fsa4(x: Double): Double {
        var deger = 0.0
        deger += (x + 2).pow(1.0 / 2)
        return deger
    }

    private fun fsa3(x: Double): Double {
        var deger = 0.0
        deger += Math.E.pow(x)
        deger -= 1
        deger -= 2 * x
        return deger
    }

    private fun fsa2(x: Double): Double {
        var deger = 0.0
        deger += ln(1 + 2 * x)
        return deger
    }

    private fun fsa1(x: Double): Double {
        var deger = 0.0
        deger += (x.pow(2) - 1) / 3
        return deger
    }
}
