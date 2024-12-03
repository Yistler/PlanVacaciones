package com.example.eva3vacaciones

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eva3vacaciones.data.AppDatabase
import com.example.eva3vacaciones.data.LugarVisita
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppUI()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppUI() {
    val contexto = LocalContext.current
    val (lugarList, setLugarList) = remember { mutableStateOf(emptyList<LugarVisita>()) }

    //Cargar lugares desde la base de datos
    LaunchedEffect(lugarList) {
        withContext(Dispatchers.IO){
            val dao = AppDatabase.getInstance(contexto).lugarVisitaDao()
            setLugarList(dao.getAll())
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if(lugarList.isEmpty()){
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.empty),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                IconButton(onClick = {
                    val intent = Intent(contexto, AddActivity::class.java)
                    contexto.startActivity(intent)
                }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Add ,
                            contentDescription = "agregar Lugar",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = stringResource(id = R.string.btnAdd),
                        )
                    }
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(lugarList){lugar ->
                    LugarItemUI(lugar){
                        setLugarList(emptyList<LugarVisita>())
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    val intent = Intent(contexto, AddActivity::class.java)
                    contexto.startActivity(intent)
                },
                    modifier = Modifier
                        .wrapContentWidth()
                    ){
                    Icon(
                        Icons.Filled.Add ,
                        contentDescription = "agregar Lugar",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = stringResource(id = R.string.btnAdd),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun LugarItemUI(lugarVisita: LugarVisita, onSave:() -> Unit = {}){
    //Crea un CoroutineScope vinculado al ciclo de vida de LugarItemUI
    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .widthIn(min = 100.dp, max = 100.dp)
                .clickable {
                    val intent = Intent(contexto, DetailActivity::class.java)
                    //Pasar los datos del lugar seleccionado a la nueva actividad
                    intent.putExtra("lugarId", lugarVisita.id)
                    intent.putExtra("lugarNombre", lugarVisita.nombreLugar)
                    intent.putExtra("lugarImagen", lugarVisita.imagenRef)
                    intent.putExtra("costoAlojamiento", lugarVisita.costoAlojamiento)
                    intent.putExtra("costoTraslado", lugarVisita.CostoTraslado)
                    intent.putExtra("latitud", lugarVisita.latitud)
                    intent.putExtra("longitud", lugarVisita.longitud)
                    intent.putExtra("comentario", lugarVisita.comentarios)
                    contexto.startActivity(intent)
                }
        ){
            if(!lugarVisita.imagenRef.isNullOrEmpty()){

                AsyncImage(
                    model = lugarVisita.imagenRef,
                    contentDescription = "Imagen del lugar visitado"
                )
            }else{
                AsyncImage(
                    model = "https://thumbs.dreamstime.com/b/no-image-available-icon-177641087.jpg",
                    contentDescription = "imagen no valida")
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(lugarVisita.nombreLugar, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            Text("${stringResource(id = R.string.cost_pert_night)}: ${lugarVisita.costoAlojamiento.toString()}")
            Text("${stringResource(id = R.string.transfer_cost)  }: ${lugarVisita.CostoTraslado.toString()}")
            options(lugarVisita)
        }
    }
}

@Composable
fun options(lugarVisita: LugarVisita){
    val contexto = LocalContext.current
    Row {
        IconButton(onClick = {
            val intent = Intent(contexto, DetailActivity::class.java)
            //Pasar los datos del lugar seleccionado a la nueva actividad
            intent.putExtra("lugarId", lugarVisita.id)
            intent.putExtra("lugarNombre", lugarVisita.nombreLugar)
            intent.putExtra("lugarImagen", lugarVisita.imagenRef)
            intent.putExtra("costoAlojamiento", lugarVisita.costoAlojamiento)
            intent.putExtra("costoTraslado", lugarVisita.CostoTraslado)
            intent.putExtra("latitud", lugarVisita.latitud)
            intent.putExtra("longitud", lugarVisita.longitud)
            intent.putExtra("comentario", lugarVisita.comentarios)
            contexto.startActivity(intent)
        }) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Ubicación"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Editar"
            )
        }
        IconButton(onClick = {

        }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Eliminar"
            )
        }
    }
}