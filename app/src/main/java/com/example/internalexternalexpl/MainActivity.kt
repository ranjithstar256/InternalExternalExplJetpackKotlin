package com.example.internalexternalexpl

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.internalexternalexpl.ui.theme.InternalExternalExplTheme
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : ComponentActivity() {
    lateinit var inputString: String

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternalExternalExplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    inputString = ""
                    var tf1 by remember {
                        mutableStateOf("")
                    }
                    var tf2 by remember {
                        mutableStateOf("")
                    }
                    var sv by remember { mutableStateOf(false) }
                    var exsv by remember { mutableStateOf(false) }
                    var rd by remember { mutableStateOf(false) }
                    var exrd by remember { mutableStateOf(false) }

                    Column(Modifier.fillMaxWidth()) {
                        TextField(value = tf1, onValueChange = { tf1 = it })
                        TextField(value = tf2, onValueChange = { tf2 = it })

                        if (sv) {

                            val fos: FileOutputStream
                            try {
                                fos = openFileOutput(tf1, MODE_PRIVATE)
                                fos.write(tf2.toByteArray())
                                fos.close()
                                Toast.makeText(
                                    applicationContext, "$tf1 saved Internal",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }

                        if (rd) {
                            val stringBuffer = StringBuffer()
                            try {
                                //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
                                val inputReader =
                                    BufferedReader(InputStreamReader(openFileInput(tf1)))


                              //  while ((inputString == inputReader.readLine()) != null) {
                                    stringBuffer.append(inputReader.readLine().toString())
                              //  }
                            }
                            catch (e: IOException) {
                                e.printStackTrace()
                            }
                             Toast.makeText(
                                applicationContext, stringBuffer.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            Text(text = stringBuffer.toString())
                        }

                        if (exsv) {

                            try {
                                //File myFile = new File("/sdcard/"+filename);
                                /// File myFile = new File(Environment.getExternalStorageState()+filename);
                                val folder: File =
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                                // val directory =  getExternalFilesDir(null);// for external storage
                                val file = File(folder, tf1)
                                file.createNewFile()
                                val fOut = FileOutputStream(file)
                                val myOutWriter = OutputStreamWriter(fOut)
                                myOutWriter.append(tf2)
                                myOutWriter.close()
                                fOut.close()
                                Toast.makeText(
                                    applicationContext,
                                    tf1 + "saved External",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }

                        if (exrd){

                            var aDataRow = ""
                            var aBuffer = ""
                            try {
                                //File myFile = new File("/sdcard/"+filename);
//                    File myFile = new File(getFilesDir()+filename);
                                val folder: File =
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                             ///   val directory = filesDir //or getExternalFilesDir(null); for external storage
                                val file = File(folder, tf2)
                                val fIn = FileInputStream(file)
                                val myReader = BufferedReader(
                                    InputStreamReader(fIn)
                                )
                                while (myReader.readLine().also { aDataRow = it } != null) {
                                    aBuffer += """
                        $aDataRow
                        """.trimIndent()
                                }
                                myReader.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            Toast.makeText(applicationContext, aBuffer, Toast.LENGTH_LONG).show()
                        }

                        Column() {
                            Button(onClick = { sv = !sv }) {
                                Text(text = "Save Internal storage")
                            }
                            Button(onClick = { rd = !rd }) {
                                Text(text = "Read Internal storage")
                            }
                            Button(onClick = { exsv = !exsv }) {
                                Text(text = "Save External storage")
                            }
                            Button(onClick = { exrd = !exrd }) {
                                Text(text = "Read External storage")
                            }

                        }

                    }
                }
            }
        }
    }
}