package luvina.net.selectcontacts

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.Telephony
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.InputStream
import java.sql.Date
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var image_view:ImageView
    val BOOKMARKS_URI: Uri = Uri.parse("content://browser/bookmarks")
    private final val formatter = SimpleDateFormat("dd/MM/yyyy-hh:mm:ss a")
    private final val TITLE_BOOKMARK = "title";
    private final val URL_BOOKMARK = "url";
    lateinit var btnShow:Button
    lateinit var btnCall:Button
    lateinit var btnMedia:Button
    lateinit var btnBookmark:Button
    lateinit var btnMess:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image_view = findViewById(R.id.imageView)
        btnShow = findViewById(R.id.button)
        btnShow.setOnClickListener(this)

        btnCall = findViewById(R.id.button2)
        btnCall.setOnClickListener(this)

        btnMedia = findViewById(R.id.button3)
        btnMedia.setOnClickListener(this)

        btnBookmark = findViewById(R.id.button4)
        btnBookmark.setOnClickListener(this)

        btnMess = findViewById(R.id.button6)
        btnMess.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var intent:Intent? = null
        when(v){
            btnShow -> {
                intent = Intent(this, ShowAllContact::class.java)
                startActivity(intent)
            }
            btnCall -> accessCallLog()
            btnMedia -> accessMediaLog()
            btnBookmark -> accessBookMark()
            btnMess -> accessMess()
        }
    }
    private fun accessCallLog(){
        var arrStr = arrayOf(CallLog.Calls.DATE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION)
        var cursor:Cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)!!
        cursor.moveToFirst()
        var str:StringBuilder = StringBuilder()
        while (!cursor.isAfterLast){
            str.append(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) + " - " + formatter.format(Date(
                cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)).toLong())
            )).append(" - ").append(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)))
            str.append("\n")
            cursor.moveToNext()
        }

        cursor.close()
       Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        Log.e("time: ", str.toString())
    }
    private fun accessMediaLog(){
        var cursor:Cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)!!
        cursor.moveToFirst()
        var str:StringBuilder = StringBuilder()
        while (!cursor.isAfterLast){
            str.append(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)))
            str.append("\n")
            cursor.moveToNext()
        }
        cursor.close()
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri: Uri? = data?.data
                val imageStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it) }
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                image_view.setImageBitmap(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

    private  fun accessBookMark(){
        var cursor: Cursor = contentResolver.query(BOOKMARKS_URI, null, null, null, null)!!
        cursor.moveToFirst()
        var str:StringBuilder = StringBuilder()
        while (!cursor.isAfterLast){
            str.append(cursor.getString(cursor.getColumnIndex(TITLE_BOOKMARK))).append(" - ").append(URL_BOOKMARK)
            str.append("\n")
            cursor.moveToNext()
        }
        cursor.close()
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        Log.e("time: ", str.toString())

    }

    @SuppressLint("NewApi")
    fun accessMess(){
        var cursor:Cursor =
            contentResolver.query(Telephony.Sms.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )!!
        cursor.moveToFirst()
        var str:StringBuilder = StringBuilder()
        while (!cursor.isAfterLast){
            str.append(cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID)))
            str.append(" - ").append(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE)))
                    .append(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)))
                    .append("\n")
            cursor.moveToNext()
        }
        cursor.close()
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}