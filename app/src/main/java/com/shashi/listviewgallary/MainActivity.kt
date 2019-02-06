package com.shashi.listviewgallary
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.indivisual_view.view.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var status:Int = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(status == PackageManager.PERMISSION_GRANTED){
            readFiles()
        }
        else{
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
        }
    }  //onCreate

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            readFiles()
        }else{
            Toast.makeText(this@MainActivity,
                "You can't read the file without storage permission ",
                Toast.LENGTH_LONG).show()
        }

    }
    fun readFiles(){
        var path = "/storage/sdcard0/WhatsApp/Media/WhatsApp Images/Sent/"
        var f = File(path)
        if(!f.exists()){
            path = "/storage/emulated/0/WhatsApp/Media/WhatsApp Images/Sent/"
            f = File(path)
        }
        var files : Array<File>  = f.listFiles()

        var myadapter = object : BaseAdapter() {     // Adapter is an array 
            override fun getCount(): Int = files.size

            override fun getItem(p0: Int): Any = 0

            override fun getItemId(p0: Int): Long = 0

            override fun getView(pos: Int, p1: View?, p2: ViewGroup?): View {

                var inflater = LayoutInflater.from(this@MainActivity) // Inflator is used to change the xml file into
                                                                                                                    // view object
                var v = inflater.inflate(R.layout.indivisual_view,null)

                var file =  files[pos]
                /* var u = Uri.fromFile(file)
                   v.iview.setImageURI(u)
                 Thumbnail creation   */
                // This code can be used to directly show image without converting into thumbnails, but it causes slowing the system
                var bmp = BitmapFactory.decodeFile(file.path)
                var compressed_bmp = ThumbnailUtils.
                    extractThumbnail(bmp, 100,80)
                v.iview.setImageBitmap(compressed_bmp)
                v.filename.text = file.name
                v.filesize.text = file.length().toString()

                v.btDel.setOnClickListener {
                    
                    var builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Image Deletion!")
                    builder.setMessage("Are you sure you want to permanently delete this image ?")
                    builder.setPositiveButton("YES"){_,_ ->
                        file.delete()
                        Toast.makeText(this@MainActivity,"Image Deleted Successfully!",Toast.LENGTH_SHORT).show()
                        readFiles()
                    }
                    builder.setNegativeButton("NO"){_,_ ->
                        Toast.makeText(this@MainActivity,"Image deletion failed!",Toast.LENGTH_SHORT).show()
                    }
                    builder.setNeutralButton("Cancle"){_,_ ->
                        Toast.makeText(this@MainActivity,"File deletion cancled!",Toast.LENGTH_SHORT).show()
                    }
                    val dialog : AlertDialog = builder.create()
                    dialog.show()
                }

                return  v
            }
        }
        lview.adapter =   myadapter
    }
}
