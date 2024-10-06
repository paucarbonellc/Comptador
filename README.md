
# 1. Anàlisi de l'estructura del projecte
El proyecto cuenta de tres paquetes principales. El primero es `manifests` que contiene el archivo `AndroidManifest.xml` . Este archivo contiene información sobre la aplicación, así como componentes que lo integran, permisos que debe tener la aplicación y las diferentes funcionalidades. Después está el paquete `kotlin+java`, este integra la lógica de la aplicación, está dividido en tres paquetes más, dos de ellos son para hacer tests unitarios y el restante es el que contiene la aplicación. Por último pasamos a el paquete `res`, este contiene los recursos necesarios para correr la aplicación, como por ejemplo las vistas de las diferentes pantallas, recursos en formato multimedia, etc.

Además también tenemos los scripts de Gradle. Tenemos dos scripts para compilar nuestro proyecto (es decir, dos scripts llamados `build.gradle.kts`), uno que es para compilar el proyecto entero, donde podríamos agregar dependencias comunes y otro que es para compilar el módulo `:app`. En este último se define la configuración de Android, versión de Java, dependencias que tiene y también los tests.

<img src="https://i.imgur.com/I1n6Bh9.png">

En general todos los archivos son importantes, pero si debemos descartar algunos de este proyecto yo descartaría los tests, ya que en este caso, hacen comprobaciones innecesarias.

En respuesta a la pregunta '_Si volguera afegir una nova activitat, sería suficient crear el fitxer de _layout_ i el fitxer Kotlin amb la classe?_', la respuesta es no, también habría que insertarla en el `AndroidManifest.xml`, tal y como está integrada la actividad `MostraComptadorActivity`

# 2. Análisi del clicle de vida i el problema de la pèrdua d'estat
Para analizar el ciclo de vida de la aplicación he hecho lo siguiente en la clase MainActivity:

<img src="https://i.imgur.com/HekcV5N.png">

Si en nuestro IDE abrimos la sección `Logcat` podemos ver lo siguiente:

```
2024-10-06 17:54:12.581  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onPause
2024-10-06 17:54:12.655  3065-3148  EGL_emulation           com.pmdm.ieseljust.comptador         D  app_time_stats: avg=8813.36ms min=9.46ms max=44011.59ms count=5
2024-10-06 17:54:13.712  3065-3065  VRI[MainActivity]       com.pmdm.ieseljust.comptador         D  visibilityChanged oldVisibility=true newVisibility=false
2024-10-06 17:54:13.866  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onStop
2024-10-06 17:54:20.501  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onRestart
2024-10-06 17:54:20.504  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onStart
2024-10-06 17:54:20.507  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onResume
2024-10-06 17:54:27.108  3065-3148  EGL_emulation           com.pmdm.ieseljust.comptador         D  app_time_stats: avg=1041.40ms min=13.54ms max=5637.76ms count=6
2024-10-06 17:54:30.055  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onPause
2024-10-06 17:54:30.134  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onStop
2024-10-06 17:54:30.162  3065-3065  DEBUG                   com.pmdm.ieseljust.comptador         D  Al mÃ¨tode onDestroy
```
Mientras actuamos con la aplicación cerrándola, abriéndola, minimizándola, matándola y demás acciones, van saltando los eventos y llamando a la función que le hemos puesto

# 3. Solució a la pèrdua d'estat
He analizado el problema y este se encuentra en que cuando rotamos la pantalla, hacemos un cambio de estado que refresca la pantalla entera, haciendo perder el progreso que llevábamos en contador. Mi solución ha sido implementar estos dos métodos en la clase `MainActivity`:

```
override fun onSaveInstanceState(outState: Bundle) {  
    super.onSaveInstanceState(outState)  
  
    outState.putInt("contador", comptador)  
}  
  
override fun onRestoreInstanceState(savedInstanceState: Bundle) {  
    super.onRestoreInstanceState(savedInstanceState)  
    comptador = savedInstanceState.getInt("contador")  
  
    val textViewContador=findViewById<TextView>(R.id.textViewComptador)  
    textViewContador.text=comptador.toString()  
}
```

Aplicando estos dos métodos en mi clase principal el problema se ha conseguido solucionar.

# 4. Ampliant la funcionalitat amb decrements i Reset
He copiado y pegado el código del nuevo layout de aules a mi proyecto y me he encontrado con dos errores. El primero de ellos: La etiqueta padre no tenía un ID asignado y fallaba en la compilación ya que en la MainActivity se intenta acceder a él. El otro era que un botón estaba intentando asignarse un valor `layout_constraintStart_toEndtOf` cuando este no existe.

Por último para implementar las funcionalidades nuevas he modificado el final del método `onCreate` para añadir esto:
```
// Referencia al botón  
val btAdd=findViewById<Button>(R.id.btAdd)  
val btMenos = findViewById<Button>(R.id.btResta)  
val btReset = findViewById<Button>(R.id.btReset)  
  
// Asociaciamos una expresióin lambda como  
// respuesta (callback) al evento Clic sobre  
// el botón  
btAdd.setOnClickListener {  
  comptador++  
    textViewContador.text=comptador.toString()  
}  
  
btMenos.setOnClickListener {  
  comptador--  
    textViewContador.text=comptador.toString()  
}  
  
btReset.setOnClickListener {  
  comptador = 0  
  textViewContador.text=comptador.toString()  
}
```


# 5. Canvis per implementar el View Binding

En https://joamuran.net/curs24_25/pmdm/u1 no he encontrado como implementar View Binding.