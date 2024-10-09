package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //data source
    private val contactList : MutableList<Contact> = mutableListOf()

    //Adapter para preencher a lista de contatos
    private val contactAdapter: ContactRvAdapter by lazy {
        ContactRvAdapter(contactList)
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
                contact?.also { newOrEditedContact ->
                     if(contactList.any{it.id == newOrEditedContact.id}) {
                         // Editar contato
                         val position = contactList.indexOfFirst { it.id == newOrEditedContact.id }
                         contactList[position] = newOrEditedContact
                     } else {
                         //se o contato já existe, atualiza ele
                        contactList.add(newOrEditedContact)
                     }
                    //preciso notificicar se o adpter foi alterado
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

        fillContats()

        //o contactsRv é a id fa RecyclerView que criei no layout
        //Ao contrário do LisTView, o recicler permite trabalhar com diferentes tipos de layout
        amb.contactsRv.adapter = contactAdapter
        //mas para isso ele exige um gerenciador de layout
        amb.contactsRv.layoutManager = LinearLayoutManager(this)
        //Tem que registrar o RecyclerView para que um context menu seja exibido quando um item da lista for pressionado e segurado por um tempo.
        // ao contrário do anterior, isso não serve mais --> registerForContextMenu(amb.contactsRv)

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

    //Aqui criamos o menu de contexto que vai aparecer ao fazer um long click em um item da lista
    //Para que ele apareça, temos que associar adapter view ao menu de contexto. Isso eu chamo lá na onCreate
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?, menuInfo:
        ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //infla o menu de contexto que criei no res/menu/context_menu_main.xml
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    //Esse é o corpo da função que trata as opções do menu de contexto.
    override fun onContextItemSelected(item: MenuItem): Boolean {
        //pega a posição do item que foi clicado
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when(item.itemId){
            R.id.removeContactMi -> {
                //remove o item da lista
                contactList.removeAt(position)
                //notifica o adapter que a lista foi alterada
                contactAdapter.notifyDataSetChanged()
                //Avisa o usuário que o contato foi removido
                Toast.makeText(this, getString(R.string.contato_removido), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMi -> {
                //pega o contato que foi clicado
                val contact = contactList[position]
                //cria um intent para abrir a activity de contato
                val editContactIntent = Intent(this, ContactActivity::class.java)
                //passa o contato como parâmetro para a activity
                editContactIntent.putExtra(EXTRA_CONTACT, contact)
                //inicia a activity
                carl.launch(editContactIntent)
                //fazendo tudo numa única linha fica
    //carl.launch(Intent(this, ContactActivity::class.java).apply {putExtra(EXTRA_CONTACT, contactList[position])})
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //tratamento de click curto para ReciclerView
    override fun onContactClick(position: Int) {
        Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        }.also {
            startActivity(it)
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