package com.gonzalezjesus_173151.myfeelings

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import utilities.CustomBarDrawable
import utilities.CustomCircleDrawable
import utilities.Emociones
import utilities.JSONFile

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile?= null
    var veryHappy = 0.0F
    var happy = 0.0F
    var normal = 0.0F
    var sad = 0.0F
    var verySad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    jsonFile = JSONFile()
        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz",0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz",0.0F, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Normal", 0.0F, R.color.greenie,normal))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue,sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F,R.color.deepBlue, verySad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener{
            guardar()
        }

        veryHappyButton.setOnClickListener{
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener{
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener{
            normal++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener{
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener{
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }


    }

    fun fetchingData(){
        try{
        var json: String = jsonFile?.getData(this)?: ""

        if (json != ""){
            this.data = true
            var jsonArray: JSONArray = JSONArray(json)

            this.lista = parseJson(jsonArray)

            for (i in lista){
                when(i.nombre){
                    "Muy feliz" -> veryHappy = i.total
                    "Feliz" -> happy = i.total
                    "Normal" -> normal = i.total
                    "Triste" -> sad = i.total
                    "Muy triste" ->  verySad = i.total
                }
            }
        }else{
            this.data = false
        }
        }catch (excep: JSONException){
            excep.printStackTrace()
        }
    }


    fun iconoMayoria(){

        if(happy>veryHappy && happy>normal && happy>sad && happy>verySad){
          //  icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_veryhappy, null))
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(veryHappy>happy && veryHappy>normal && veryHappy>sad && veryHappy>verySad){
           // icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_happy, null))
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }
        if(normal>veryHappy && normal>happy && normal>sad && normal>verySad){
           // icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_neutral, null))
           icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if(sad>happy && sad>normal && sad>veryHappy && sad>verySad){
           // icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sad, null))
          icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if(verySad>happy && verySad>normal && verySad>sad && veryHappy<verySad){
            //icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_varysad, null))
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }

    }

    fun actualizarGrafica(){
        val total = veryHappy+happy+normal+verySad+sad

        var pVH: Float = (veryHappy * 100 / total).toFloat()
        var pH: Float = (happy * 100 / total).toFloat()
        var pN: Float = (normal * 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verySad * 100 / total).toFloat()

        Log.d("porcentajes", "very happy"+ pVH)
        Log.d("porcentajes","happy"+ pH)
        Log.d("porcentajes","normal" + pN)
        Log.d("porcentajes","sad" + pS)
        Log.d("porcentajes","very sad" + pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Normal", pN, R.color.greenie,normal))
        lista.add(Emociones("Triste", pS, R.color.blue,sad))
        lista.add(Emociones("Muy triste", pVS,R.color.deepBlue, verySad))

        val fondo = CustomCircleDrawable(this, lista)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz",pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz",pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Normal", pN, R.color.greenie,normal))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue,sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS,R.color.deepBlue, verySad))

        graph.background = fondo
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()
        for (i in 0..jsonArray.length()){
            try {
                val nombre= jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje= jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color= jsonArray.getJSONObject(i).getInt("color")
                val total= jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            }catch (excep: JSONException){
                excep.printStackTrace()
            }
        }
        return lista
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o : Int = 0

        for ( i in lista){
            Log.d("objetos", i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, i)
            o++

        }
        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this,"Datos guardados", Toast.LENGTH_SHORT).show()
    }

}