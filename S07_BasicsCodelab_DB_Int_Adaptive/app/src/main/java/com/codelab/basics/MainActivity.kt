package com.codelab.basics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.net.URL
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection


class MainActivity : ComponentActivity() {
    // ColorPicker is created here to get context, and then handed down through the functions to where it is used, MyApp, ShowEachListItem, CardContent, etc.
    val colorPicker = ColorPicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Open the DB
        val DBtest = DBClass(this@MainActivity)
        Log.d("CodeLab_DB", "onCreate: ")

        // Then the real data
        setContent {
            BasicsCodelabTheme {
                MyApp(
                    modifier = Modifier.fillMaxSize()
                    // Get the data from the DB for display
                    , names = DBtest.findAll(),
                    db = DBtest,
                    colorPicker = colorPicker
                )
            }
        }
    }
}

// DBClass is handed down through the methods, much like ColorPicker, so that the db can be updated in the CardContent function to change the access count.
@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<PokeModel>,
    db: DBClass,
    colorPicker: ColorPicker
) {
    // Get the size of this screen
    val windowInfo = rememberWindowInfo()
    // Which name to display
    var index by remember { mutableIntStateOf(-1) }
    // Fudge to force master list first, when compact
    var showMaster: Boolean = (index == -1)
    // To make it so when the menu of each Pokemon is opened, the favourite Pokemon can appear/disappear. It's based on screen size, so that the favourite Pokemon is only hidden when the details are shown on a compact screen
    var showFavouritePokemonCard by remember { mutableStateOf(
        windowInfo.screenWidthInfo !is WindowInfo.WindowType.Compact || index == -1
    ) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        // Put everything in a column so the favourite card can be on top of everything else
        Column(modifier = Modifier.fillMaxSize()) {
            // Loop through the Pokemon, and display the one with the highest access count if the list isn't empty (never should be, but a good check to have)
            if (names.isNotEmpty() && showFavouritePokemonCard) {
                var highestAccess = names[0]
                var favourite = 0
                for (i in names.indices) {
                    if (names[i].access > highestAccess.access) {
                        highestAccess = names[i];
                        favourite = i;
                    }
                }
                Log.d("CodeLab_DB", "MyApp: $highestAccess")
                FavouritePokemonCard(pokeModel = names[favourite], colorPicker = colorPicker)
            }

            Log.d("CodeLab_DB", "MyApp0: index = $index ")
            if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
                // Always check endpoints!
                if (showMaster || ((index < 0) || (index >= names.size))) {
                    Log.d("CodeLab_DB", "MyApp1: index = $index firstTime = $showMaster")
                    showMaster = false
                    showFavouritePokemonCard = true
                    ShowPageMaster(names = names, updateIndex = { index = it
                    showFavouritePokemonCard = true}, db = db, colorPicker = colorPicker)
                } else {
                    Log.d("CodeLab_DB", "MyApp2: $index ")
                    showFavouritePokemonCard = false;
                    ShowPageDetails(name = names[index], // List starts at 0, DB records start at 1
                        index = index, // Use index for prev, next screen
                        updateIndex = { newIndex ->
                            index = newIndex
                            showFavouritePokemonCard = newIndex < 0
                        })
                }
            }
            // Show master details side-by-side
            else {
                // Force favourite card to be visible in not compact mode
                showFavouritePokemonCard = true;
                index = if (index < 0) 0 else index
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        ShowPageMaster(names = names,
                            updateIndex = { index = it }, db = db, colorPicker = colorPicker)
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        ShowPageDetails(name = names[index], // List starts at 0, DB records start at 1
                            index = index, // Use index for prev, next screen
                            updateIndex = { newIndex ->
                                index = newIndex
                                showFavouritePokemonCard = newIndex < 0
                            })
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowPageMaster(
    modifier: Modifier = Modifier,
    names: List<PokeModel>,
    updateIndex: (index: Int) -> Unit,
    db: DBClass,
    colorPicker: ColorPicker
) {

    LazyColumn(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(items = names) { pos, name ->
            Log.d("CodeLab_DB", "Item at index $pos is $name")
            ShowEachListItem(name = name, pos, updateIndex, db, colorPicker)
        }
    }
}

// Specific Card that uses only one pokeModel (Pokemon)
@Composable
fun FavouritePokemonCard(pokeModel: PokeModel, colorPicker: ColorPicker) {
    // Uses colorPicker to get a color based on a Pokemon's primary type and if it's a legendary or not
    val containerColor = colorPicker.getColor(pokeModel.type1, pokeModel.legendary)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(containerColor)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = pokeModel.pokemonName,
                // Uses custom typography to get my custom font instead of the default
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = pokeModel.toFancyString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ColorPicker is handed here so it can be used, as well as handed further down. DBClass is just so it can be handed further down
@Composable
private fun ShowEachListItem(
    name: PokeModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    db: DBClass,
    colorPicker: ColorPicker
) {
    val containerColor = colorPicker.getColor(name.type1, name.legendary)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(containerColor)
        ),
        modifier = Modifier.padding(vertical = 1.dp, horizontal = 8.dp)
    ) {
        CardContent(name, pos, updateIndex, db, colorPicker)
        Log.d("CodeLab_DB", "Greeting: ")
    }
}
// Function to get the image from a website, and then convert it to a bitmap
private fun fetchImage(url: String): ByteArray? {
    return try {
        // Open connection to the website using the url. Setting doInput to true is what lets this read data (aka get the image) from the url
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        // Basic website stuff, if the response code is not OK, we can't get the image so return null and log
        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Log.e("fetchImage", "HTTP error code: $responseCode");
            return null
        }

        // Use input stream... To get a stream of input. Then us BitmapFactory to decode the image ad save as bitmap
        val input: InputStream = connection.inputStream
        val bitmap: Bitmap? = BitmapFactory.decodeStream(input)

        // Bitmap should be fine, but if not, return null and log
        if (bitmap == null) {
            Log.e("fetchImage", "Bitmap is null");
            return null
        }

        // Use ByteArrayOutputStream to convert the bitmap to a byte array (I just like the byte array better, still works and I used the bitmap as well)
        ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    } catch (e: Exception) {
        // If none of this works, return null and log the exception
        Log.e("fetchImage", "Error fetching image", e);
        return null
    }
}

// Both ColorPicker and DBClass are handed down so they can be used here
@Composable
private fun CardContent(
    name: PokeModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    db: DBClass,
    colorPicker: ColorPicker
) {
    val context = LocalContext.current
    var containerColor = colorPicker.getOppositeColor(name.type1, name.legendary)
    var contentColor = colorPicker.getColor(name.type1, name.legendary)
    var expanded by remember { mutableStateOf(false) }
    // Remember the image state so we don't have to reload for the images to appear
    val imageState = remember { mutableStateOf(name.image) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(containerColor),
                    contentColor = Color(contentColor)
                ),
                onClick = {
                    updateIndex(pos);
                    Log.d(
                        "CodeLab_DB",
                        "Clicked = ${name.toString()} "
                    )
                })
            { Text(
                text = "Pokemon ${pos}") }
            Text(
                // Just the name of this record
                text = name.pokemonName ,
                style = MaterialTheme.typography.headlineMedium
            )
            if (expanded) {
                Text(
                    text = (name.toFancyString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Log.d("CodeLab_DB", "Expanded name = ${name.toString()} ")
                // Second button where the magic happens
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(containerColor),
                        contentColor = Color(contentColor)
                    ),
                    onClick = {
                        // Use an intent to open the website for this specific Pokemon
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(name.link))
                        context.startActivity(intent)

                        // This CoroutineScope is a Kotlin thing. It basically lets all this code run, in the background without bothering anything else. We didn't go over threads, but I figured this was a good way to learn. All of this is separate from the main thread
                        CoroutineScope(Dispatchers.IO).launch {
                            // Get the bytes based of the image link
                            val fetchedImageBytes = fetchImage(name.link)
                            Log.v("Pokelink", name.link);
                            // If not null, update the db and the image state
                            if (fetchedImageBytes != null) {
                                db.updateImageByName(name.pokemonName, fetchedImageBytes)
                                Log.v("Image", "Image saved to database")
                                name.image = db.findImageByName(name.pokemonName)
                                imageState.value = name.image
                            } else {
                                Log.e("Image", "Failed to fetch image")
                            }
                        }

                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Visit Website")
                }
                // Finally, decode and display the image if it exists
                if (imageState.value != null) {
                    imageState.value?.let { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        Image(bitmap.asImageBitmap(), contentDescription = "Pokemon Image", modifier = Modifier.size(128.dp))
                    } ?: Log.e("ImageDisplay", "Bitmap is null, cannot display image.")
                } else {
                    Log.e("ImageDisplay", "name.image is null, not displaying image.")
                }
            }
        }
        // When the button is clicked to expand the content, the access count is incremented and the db is updated
        IconButton(onClick = {
            if (!expanded) {
                name.access++
                db.update(name)
            }
            expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

// ColorPicker is NOT used here, as it returns hex values, not ARGB values. I think a constant colour looks nice for this section anyway, but it's a relatively easy addition if I want to go back to this later
@Composable
private fun ShowPageDetails(
    name: PokeModel,
    updateIndex: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int
) {
    // Sorta global, not good
    val windowInfo = rememberWindowInfo()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = (name.toString()),
            style = MaterialTheme.typography.headlineMedium)
        Log.d("CodeLab_DB", "ShowData: $name.toString()")

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Button(
                colors = ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = { updateIndex(-1) })
            { Text(text = "Master") }
        }
        // Need check for end of array
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            onClick = { updateIndex(index + 1) })
        { Text(text = "Next") }
        if (index > 0) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = { updateIndex(index - 1) })
            { Text(text = "Prev") }
        }
    }
}