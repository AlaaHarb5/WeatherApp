package com.example.weatherapp.Common

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Common
{
    val API_KEY = " 45bf6a7e9f13c39e38db3580f469713f"
    val API_LINK= "http://api.openweathermap.org/data/2.5/weather "
    fun apiRequest (lat:String , lang : String) :String
    {
        var sb = StringBuilder(API_LINK)
        sb.append("?lat=${lat}&lon=${lang}&APPID=${API_KEY}&units=matric")
        return sb.toString()
    }
    fun unixTimeStampToDateTime(unixStamp:Double):String
    {
        val dateFormate = SimpleDateFormat("HH:mm:ss")
        val date = Date()
        date.time=unixStamp.toLong()*1000
        return dateFormate.format(date)
    }
    fun getImage(icon:String):String
        {
        return "http://operweathermap.org/img/w//${icon}.png"
    }
    val dateNow:String

        get()
        {
            val dateFormat=SimpleDateFormat("dd MM yyyy HH:mm")
            val date=Date()
            return dateFormat.format(date)
        }
    }
