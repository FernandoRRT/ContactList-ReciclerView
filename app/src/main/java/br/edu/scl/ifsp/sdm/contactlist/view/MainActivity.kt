package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList : MutableList<Contact> = mutableListOf()

    //Adapter para preencher a lista de contatos
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
        //Constrututor do adapter. Primeiro parametro é o contexto (o this, referencia essa activity),
            this,
        //Já o segundo parametro é a lista de contatos (o dataservice).
        //Depois de criar o adapter precisamos associar ele com a listview (adapterView)
            contactList
        )
    }

    //Criei um contrato que espera um resultado de uma activity
    private lateinit var carl:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //pega o contato que foi retornado pela activity de contato. Data é um bundle que foi inferido ao contact
                //o getParcelableExtra é um método que pega um objeto que foi passado como parâmetro para a activity
                //Esse método é deprecado a partir da API 33, o que significa que ele ainda funciona, mas não é recomendado

                //o getParcelableExtra é um método que pega um objeto que foi passado como parâmetro para a activity
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                //aqui usei also ao invés de let pois precisei fazer duas coisas: adicionar o contato na lista e notificar o adapter
                contact?.also {
                     if(contactList.any{it.id == contact.id}) {
                         // Editar contato
                     } else {
                         //se o contato já existe, atualiza ele
                        contactList.add(contact)
                     }
                    //preciso notificicar se o adpter foi alterado
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

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
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            } else -> {
                false
            }
        }
    }

    private fun fillContats() {
        for (i in 1..10) {
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