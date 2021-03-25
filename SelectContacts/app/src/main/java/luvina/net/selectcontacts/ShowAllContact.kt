package luvina.net.selectcontacts

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ShowAllContact: AppCompatActivity() {
    lateinit var back:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_all_contacts)
        back = findViewById(R.id.button5)
        back.setOnClickListener(View.OnClickListener {
            finish();
        })
        showAllContact()
    }
        private fun showAllContact(){
            var uri:Uri = Uri.parse("content://contacts/people")
            var listContact = ArrayList<String>()
            val c1 = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            if (c1 != null) {
                c1.moveToFirst()
                while (!c1.isAfterLast){
                    var str:String
                    var idColumn:String = ContactsContract.Contacts._ID
                    var nameColumn = ContactsContract.Contacts.DISPLAY_NAME
                    var tel = ContactsContract.CommonDataKinds.Phone.NUMBER

                    str = "${c1.getString(c1.getColumnIndex(idColumn))} - ${c1.getString(c1.getColumnIndex(nameColumn))} " +
                            "- ${c1.getString(c1.getColumnIndex(tel)).replace("(", "")
                                .replace(")","").replace("-","").replace(" ", "")}"
                    listContact.add(str)
                    c1.moveToNext()
                }
                c1.close()
            }

            var listView:ListView = findViewById(R.id.listViewContact)
            var adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listContact)
            listView.adapter = adapter
        }
}