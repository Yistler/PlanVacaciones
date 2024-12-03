package com.example.eva3vacaciones

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Obtener los datos de Intent
            DetailItemUI(intent)
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun DetailItemUI(intent: Intent) {
    val contexto = LocalContext.current

    val lugarId = intent.getLongExtra("lugarId", 0)
    val lugarNombre = intent.getStringExtra("lugarNombre") ?: ""
    val lugarImagen = intent.getStringExtra("lugarImagen") ?: ""
    val costoAlojamiento = intent.getDoubleExtra("costoAlojamiento", 0.0)
    val costoTraslado = intent.getDoubleExtra("costoTraslado", 0.0)
    val latitud = intent.getDoubleExtra("latitud", 0.0)
    val longitud = intent.getDoubleExtra("longitud", 0.0)
    val comentario = intent.getStringExtra("comentario") ?: ""

    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = lugarNombre,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        if(lugarImagen.isNotEmpty()){
            AsyncImage(
                model = lugarImagen,
                contentDescription = "Imagen del lugar visitado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }else{
            AsyncImage(
                model = "https://thumbs.dreamstime.com/b/no-image-available-icon-177641087.jpg",
                contentDescription = "imagen no valida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        //Precios
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            ){
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.cost_pert_night),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text("${costoAlojamiento} - USD")
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(id = R.string.transfer_cost),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text("${costoTraslado} - USD")
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ){
            Text(
                text = stringResource(id = R.string.comments),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "${comentario}",
                )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "Camara"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar"
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        // Agregar una línea de separación similar a <hr> con Divider
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        AndroidView(//Mapa
            factory = {
                MapView(it).apply {
                    setTileSource(TileSourceFactory.MAPNIK)//Trae las imagenes desde MAPNIK
                    org.osmdroid.config.Configuration.getInstance().userAgentValue = contexto.packageName
                    controller.setZoom(15.0)
                }
            }, update = {
                it.overlays.removeIf { true }//Quita el marcador si lo hay
                it.invalidate()//Redibuja o invalida obliga que se dibuje el mapa

                val geoPoint = GeoPoint(latitud, longitud)
                it.controller.animateTo(geoPoint)

                val marcador = Marker(it) //Marcador dentro de la coordenada
                marcador.position = geoPoint
                marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                it.overlays.add(marcador)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}