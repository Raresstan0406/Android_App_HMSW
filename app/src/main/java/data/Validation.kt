package data

fun validation(Temp:Float, Pulse:Int, SpO2:Int):List<Int>{
    val tempValid = if (Temp in 34.0..37.0) 1 else 0
    val pulseValid = if (Pulse in 60..100) 1 else 0
    val spo2Valid = if (SpO2 in 93..100) 1 else 0
    return listOf(tempValid, pulseValid, spo2Valid)
}

fun parseVitals(message: String): Triple<Float, Int, Int> {
    val tempMatch = Regex("""T:([0-9.]+)""").find(message)?.groupValues?.get(1)
    val pulseMatch = Regex("""P:(\d+)""").find(message)?.groupValues?.get(1)
    val spo2Match = Regex("""S:(\d+)""").find(message)?.groupValues?.get(1)

    val temp = tempMatch?.toFloat() ?: 0f
    val pulse = pulseMatch?.toInt() ?: 0
    val spo2 = spo2Match?.toInt() ?: 0

    return Triple(temp, pulse, spo2)
}