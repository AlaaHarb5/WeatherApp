package com.example.weatherapp.model

class OpenWeatherMap
{
    var weather: List<Weather>? = null
    var base : String? = null
    var rain:Rain? = null
    var main: Main? = null
    var clouds: Clouds? = null
    var sys: Sys? = null
    var wind :Wind? = null
    var id:Int = 0
    var name:String? = null
    var cod :Int = 0
    var dt :Int = 0
    constructor(){}


    constructor(
        weather: List<Weather>,
        base: String,
        rain: Rain,
        main: Main,
        clouds: Clouds,
        sys: Sys,
        wind: Wind,
        id: Int,
        name: String,
        cod: Int,
        dt: Int
    ) {
        this.weather = weather
        this.base = base
        this.rain = rain
        this.main = main
        this.clouds = clouds
        this.sys = sys
        this.wind = wind
        this.id = id
        this.name = name
        this.cod = cod
        this.dt = dt
    }
}