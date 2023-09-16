package com.sergio.evafinal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sergio.evafinal.DB.DBHelper
import com.sergio.evafinal.DB.Lugar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel


enum class Pantalla{
    PANTALLAPRINCIPAL,
    FORMNEW,
    FORMEDIT,
    CAMARA
}

class AppVM : ViewModel() {
    val pantallaActual = mutableStateOf(Pantalla.PANTALLAPRINCIPAL)
    val lugarSeleccionado = mutableStateOf(Lugar(
        id = 0,
        lugarNombre = "",
        imagenUrl = "",
        latitud = 0.0,
        longitud = 0.0,
        orden = 0,
        costoAlojamiento = 0.0,
        costoTraslados = 0.0,
        comentarios = ""
    ))
}


class MainActivity : ComponentActivity() {
    val appVM:AppVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUI()
        }
    }
}

@Composable
fun AppUI(
){
    val appVM:AppVM = viewModel()

    when(appVM.pantallaActual.value){
        Pantalla.PANTALLAPRINCIPAL -> {
            AppLugaresUI()
        }
        Pantalla.FORMNEW -> {
            PantallaNuevoLugar()
        }
        Pantalla.FORMEDIT -> {
            PantallaEditUI(appVM.lugarSeleccionado.value)
        }
        Pantalla.CAMARA -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNuevoLugar() {
    var lugarNombre by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var orden by remember { mutableStateOf(0) }
    var costoAlojamiento by remember { mutableStateOf(0.0) } // Cambiado a Double
    var costoTraslados by remember { mutableStateOf(0.0) }
    var comentarios by remember { mutableStateOf("") }
    val alcanceCorrutina = rememberCoroutineScope()
    val contexto = LocalContext.current as ComponentActivity
    val appVM:AppVM = viewModel()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row() {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Ir hacia atrás",
                modifier = Modifier
                    .clickable {
                        appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL
                    }
                    .size(36.dp),
                tint = Color.Black,
            )
        }
        Text("Agregar Nuevo Lugar", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier
            .padding(16.dp)
            .align(Alignment.CenterHorizontally))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = lugarNombre,
            onValueChange = { lugarNombre = it },
            label = { Text("Nombre Lugar") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("Url Imagen Referencial") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = latitud,
            onValueChange = { latitud = it },
            label = { Text("Latitud") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = longitud,
            onValueChange = { longitud = it },
            label = { Text("Longitud") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = orden.toString(), // Convierte el valor de Int a String para mostrarlo en el campo
            onValueChange = {
                // Comprueba si el valor ingresado es un número entero válido antes de actualizar la variable
                if (it.toIntOrNull() != null) {
                    orden = it.toInt()
                }
            },
            label = { Text("Orden") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = costoAlojamiento.toString(),
            onValueChange =  {
                val parsedValue = it.toDoubleOrNull()
                if (parsedValue != null) {
                    costoAlojamiento = parsedValue
                }
            },
            label = { Text("Costo Alojamiento") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
//            textStyle = TextStyle(textAlign = TextAlign.End)
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = costoTraslados.toString(),
            onValueChange = {
                val parsedValue = it.toDoubleOrNull()
                if (parsedValue != null) {
                    costoTraslados = parsedValue
                }
            },
            label = { Text("Costo Traslados") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            value = comentarios,
            onValueChange = { comentarios = it },
            label = { Text("Comentarios") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                alcanceCorrutina.launch(Dispatchers.IO){
                    val lugarDao = DBHelper.getInstance(contexto).lugarDao()
                    // Crea un objeto Lugar con los datos ingresados por el usuario
                    val nuevoLugar = Lugar(
                        id = 0,
                        lugarNombre = lugarNombre,
                        imagenUrl = imagenUrl,
                        latitud = latitud.toDoubleOrNull() ?: 0.0, // Convierte a Double o usa 0.0 si no se puede convertir
                        longitud = longitud.toDoubleOrNull() ?: 0.0,
                        orden = orden,
                        costoAlojamiento = costoAlojamiento,
                        costoTraslados = costoTraslados,
                        comentarios = comentarios
                    )

                    //Inserta el objeto Lugar en la base de datos
                    val idLugarInsertado = lugarDao.insertLugar(nuevoLugar)

                    if (idLugarInsertado > 0) {

                        // La inserción fue exitosa, guardo en el log
                        Log.d("MiApp", "Inserción exitosa. ID del nuevo lugar: $idLugarInsertado")

                    } else {
                        // Hubo un error en la inserción, guardo en el log
                        Log.e("MiApp", "Error durante la inserción en la base de datos")

                    }
                    appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL

                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar")
        }
    }
}

@Composable
fun AppLugaresUI() {
    val (lugares, setLugares) = remember { mutableStateOf(emptyList<Lugar>()) }
    val alcanceCorrutina = rememberCoroutineScope()
    val contexto = LocalContext.current as ComponentActivity
    val appVM:AppVM = viewModel()

    LaunchedEffect(lugares) {
        alcanceCorrutina.launch(Dispatchers.IO) {
            // Obtener la lista de lugares desde la base de datos
            val lugarDao = DBHelper.getInstance(contexto).lugarDao()
            val lugaresList = lugarDao.findAll()
            setLugares(lugaresList)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){

        Button(
            onClick = {

                appVM.pantallaActual.value = Pantalla.FORMNEW
            },
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.End)
        ) {
            Text("Agregar Nuevo Lugar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (lugares.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(lugares) { lugar ->
                    LugarItemUI(lugar)
                    {
                        setLugares(emptyList<Lugar>())

                    }
                }
            }
        } else {
            Text("No hay lugares para mostrar.")
        }





    }
}


@Composable
fun LugarItemUI(lugar:Lugar,onSave:() -> Unit = {}){
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()
    val appVM:AppVM = viewModel()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ){
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.widthIn(min = 140.dp, max = 140.dp),
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                model = lugar.imagenUrl,
                contentDescription = "Imagen url:  ${lugar.lugarNombre}"
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(){
            Text(lugar.lugarNombre, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)) {
                        append("Costo x Noche: ")
                    }

                    append(lugar.costoAlojamiento.toString())
                },
                fontSize = 16.sp
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)) {
                        append("Traslado: ")
                    }

                    append(lugar.costoTraslados.toString())
                },
                fontSize = 16.sp
            )
            Row(
//                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar Lugar",
                    modifier = Modifier.clickable{
                        alcanceCorrutina.launch(Dispatchers.IO){
                            val dao = DBHelper.getInstance(contexto).lugarDao()
                            dao.deleteLugar(lugar)
                            onSave()
                        }
                    },
                    tint = Color.Red
                )

                Icon(
                    Icons.Filled.ExitToApp,
                    contentDescription = "Eliminar Lugar",
                    modifier = Modifier.clickable{
                        appVM.lugarSeleccionado.value = lugar
                        appVM.pantallaActual.value = Pantalla.FORMEDIT

                    },
                    tint = Color.Black
                )
            }
        }
    }

}

@Composable
fun PantallaEditUI(lugar:Lugar) {
    val appVM: AppVM = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {

        Row() {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Ir hacia atrás",
                modifier = Modifier
                    .clickable {
                        appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL
                    }
                    .size(36.dp),
                tint = Color.Black,
            )
        }
        //titulo
        Text(
            lugar.lugarNombre,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        //imagen
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = lugar.imagenUrl,
                contentDescription = "lugar:  ${lugar.lugarNombre}"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column {
                Text(
                    text = "Costo x Noche:",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = lugar.costoAlojamiento.toString(),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(50.dp))
            Column {
                Text(
                    text = "Traslados:",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = lugar.costoTraslados.toString(),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Comentarios:",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 5.dp).align(Alignment.CenterHorizontally)
        )
        Text(
            text = lugar.comentarios.toString(),
            fontSize = 16.sp
        )

        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 10.dp)) {
            Icon(
                Icons.Filled.AccountBox,
                contentDescription = "Foto",
                modifier = Modifier
                    .clickable {
                        appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL
                    }
                    .size(36.dp),
                tint = Color.Black,
            )
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Editar",
                modifier = Modifier
                    .clickable {
                        appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL
                    }
                    .size(36.dp),
                tint = Color.Black,
            )
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Eliminar",
                modifier = Modifier
                    .clickable {
                        appVM.pantallaActual.value = Pantalla.PANTALLAPRINCIPAL
                    }
                    .size(36.dp),
                tint = Color.Red,
            )
        }
    }


    }
