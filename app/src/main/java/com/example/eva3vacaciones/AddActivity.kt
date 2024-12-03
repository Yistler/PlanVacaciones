package com.example.eva3vacaciones

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.example.eva3vacaciones.data.AppDatabase
import com.example.eva3vacaciones.data.LugarVisita
import com.example.eva3vacaciones.ui.theme.Eva3VacacionesTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppVM : ViewModel(){
    var permisosUbicacionOk:() -> Unit = {}
}

class AddActivity : ComponentActivity() {
    val appVM:AppVM by viewModels()

    val lanzadorPermisos = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
        if(
            (it[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false) or
            (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false)
        ){
            appVM.permisosUbicacionOk()
        }else{
            Log.v("lanzadorPermisos callback", "Se denegaron los permisos")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddPlaceItemUI(appVM, lanzadorPermisos)
        }
    }
}


class FaltalPermisionException(mensaje:String): Exception(mensaje)

fun getUbicacion(contexto: Context, onSucces:(ubicacion: Location) -> Unit){
    try {
        val servicio = LocationServices.getFusedLocationProviderClient(contexto)
        val tarea = servicio.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        )
        tarea.addOnSuccessListener {
            if(it != null){
                onSucces(it)
            }
        }
    }catch (se:SecurityException){
        throw FaltalPermisionException("Sin permisos de ubicación")
    }
}



//@Preview(showBackground = true)
@Composable
fun AddPlaceItemUI(appVM: AppVM, lanzadorPermisos: ActivityResultLauncher<Array<String>>) {
    val (nombreLugar, setNombreLugar) = remember { mutableStateOf("") }
    val (imagenRef, setImagenRef) = remember { mutableStateOf("") }
    var (latitud, setLatitud) = remember { mutableStateOf("") }
    var (longitud, setLongitud) = remember { mutableStateOf("") }
    val (ordenVisita, setOrdenVisita) = remember { mutableStateOf("") }
    val (costoAlojamiento, setCostoAlojamiento) = remember { mutableStateOf("") }
    val (CostoTraslado, setCostoTraslado) = remember { mutableStateOf("") }
    val (comentarios, setComentarios) = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(id = R.string.place))
            TextField(
                value = nombreLugar,
                onValueChange = {setNombreLugar(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(id = R.string.image))
            TextField(
                value = imagenRef,
                onValueChange = {setImagenRef(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.latitude))
                    TextField(
                        value = latitud,
                        onValueChange = {setLatitud(it)},
                        label = {Text("")},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.longitude))
                    TextField(
                        value = longitud,
                        onValueChange = {setLongitud(it)},
                        label = {Text("")},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    IconButton(onClick = {
                        appVM.permisosUbicacionOk = {
                            getUbicacion(contexto){ location ->
                                setLatitud(location.latitude.toString())
                                setLongitud(location.longitude.toString())
                            }
                        }

                        lanzadorPermisos.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AddLocation,
                            contentDescription = "ubicación"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(id = R.string.order))
            TextField(
                value = ordenVisita,
                onValueChange = {setOrdenVisita(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(id = R.string.cost_Alojamiento))
            TextField(
                value = costoAlojamiento,
                onValueChange = {setCostoAlojamiento(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(id = R.string.cost_traslado))
            TextField(
                value = CostoTraslado,
                onValueChange = {setCostoTraslado(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(id = R.string.comments))
            TextField(
                value = comentarios,
                onValueChange = {setComentarios(it)},
                label = {Text("")}
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                val alojamiento = costoAlojamiento.toDoubleOrNull() ?: 0.0
                val traslado = CostoTraslado.toDoubleOrNull() ?: 0.0
                val orden = ordenVisita.toIntOrNull() ?: 0
                val latitudLugar = latitud.toDoubleOrNull() ?: 0.0
                val longitudLugar = longitud.toDoubleOrNull() ?: 0.0

                if(nombreLugar.isNotBlank()){
                    val nuevoLugar = LugarVisita(
                        nombreLugar = nombreLugar,
                        imagenRef = imagenRef,
                        latitud = latitudLugar,
                        longitud = longitudLugar,
                        ordenVisita = orden,
                        costoAlojamiento = alojamiento,
                        CostoTraslado = traslado,
                        comentarios = comentarios
                    )
                    scope.launch(Dispatchers.IO){
                        val dao = AppDatabase.getInstance(contexto).lugarVisitaDao()
                        dao.agregarLugar(nuevoLugar)

                        withContext(Dispatchers.Main){
                            setNombreLugar("")
                            setImagenRef("")
                            setLatitud("")
                            setLongitud("")
                            setOrdenVisita("")
                            setCostoAlojamiento("")
                            setCostoTraslado("")
                            setComentarios("")
                        }
                    }
                }
            }) {
                Text(stringResource(id = R.string.btnAdd))
            }
        }
        Button(onClick = {
            val intent = Intent(contexto, MainActivity::class.java)
            contexto.startActivity(intent)
        },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
          Icon(
              Icons.Filled.ArrowBack ,
              contentDescription = "${ stringResource(id = R.string.btnBack)}")
        }
    }
}