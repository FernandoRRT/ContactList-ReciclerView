package br.edu.scl.ifsp.sdm.contactlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList : MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        fillContats()
    }

    private fun fillContats() {
        for (i in 1..50) {
            contactList.add(
                Contact(
                i,
                "Nome $i",
                "Endereço $i",
                "Telefone 1234-$i",
                "Email $i"
                )
            )
        }

    }

}