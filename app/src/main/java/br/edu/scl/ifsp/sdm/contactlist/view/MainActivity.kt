package br.edu.scl.ifsp.sdm.contactlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList : MutableList<Contact> = mutableListOf()

    //Adapter para preencher a lista de contatos
    private val contactAdapter: ArrayAdapter<String> by lazy {
        //Constrututor do arrayadapter. Primeiro parametro é o contexto (o this, referencia essa activity),
        // segundo é o layout do item da lista que ele vai inflar (simple_list_item_1 é um layout padrão do android),
        //que nada mais é que um textview. Já o terceiro parametro é a lista de contatos (o dataservice, que precisa
        // ser convertido em lista/array de string no nosso caso).
        //Depois de criar o adapter precisamos associar ele com a listview (adapterView)
        ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            contactList.map { it.toString() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        fillContats()

        //o contactsLv é a id fa ListView que criei no layout
        amb.contactsLv.adapter = contactAdapter
    }

    //override do método que cria o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //infla o menu que criei no res/menu/menu_main.xml
        //R.menu (diretório do menu). menu_main (nome do arquivo xml)
        menuInflater.inflate(R.menu.menu_main, menu)
        //essa função retorna true se o menu foi inflado com sucesso
        return true
    }

    //tratar das opções de clique no menu.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //verifica qual item do menu foi clicado
        return when(item.itemId) {
            //se foi o adicionar
            R.id.addContactMi -> {
//                //cria um novo contato
//                val newContact = Contact(
//                    contactList.size + 1,
//                    "Nome ${contactList.size + 1}",
//                    "Endereço ${contactList.size + 1}",
//                    "Telefone 1234-${contactList.size + 1}",
//                    "Email ${contactList.size + 1}"
//                )
//                //adiciona o novo contato na lista
//                contactList.add(newContact)
//                //notifica o adapter que a lista mudou
//                contactAdapter.notifyDataSetChanged()
                return true
            } else -> {
                false
            }
        }
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