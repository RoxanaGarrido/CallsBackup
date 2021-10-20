package es.recitecnic.rgarrido.callsbackup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.CancellationSignal
import android.provider.CallLog
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import es.recitecnic.rgarrido.callsbackup.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

/*
Aplicación Android para obtener de manera simple Call Logs
Número de teléfono, duración de llamada, tipo de llamada (código) y fecha (milisegundos desde la época)
Problemas: No sé como formatear la fecha.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var data = listOf<String>(CallLog.Calls._ID,
                              CallLog.Calls.NUMBER,
                              CallLog.Calls.TYPE,
                              CallLog.Calls.DURATION,
                              CallLog.Calls.DATE).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, Array(1){Manifest.permission.READ_CALL_LOG}, 101)
        }else
            displayLog()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            displayLog()
    }

    @SuppressLint("MissingPermission")
    private fun displayLog() {

        var from = listOf<String>(CallLog.Calls.NUMBER, CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE).toTypedArray()

        var to = intArrayOf(R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4)

        var rs: Cursor? = contentResolver.query(CallLog.Calls.CONTENT_URI, data, null,null,
            "${CallLog.Calls.LAST_MODIFIED} DESC")

        var adapter = SimpleCursorAdapter(applicationContext,
                                          R.layout.mylayout,
                                          rs,
                                          from,
                                          to, 0)

        binding.listview.adapter = adapter

    }
}