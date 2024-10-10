package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.view.OnContactClickListener

//Início do código do adapter. Um adaptador é necessário para fornecer os dados a serem exibidos em um RecyclerView.
class ContactRvAdapter(
    //O adaptador recebe uma lista mutável de objetos Contact como parâmetro, chamada contactList.
    // Ela é armazenada como uma variável privada da classe para que possa ser acessada pelas funções internas.
    private val contactList: MutableList<Contact>,
    //esse é a função que passa para o adaptador o que fazer quando um contato é clicado
    private val onContactClickListener: OnContactClickListener
    //A classe ContactRvAdapter herda de RecyclerView.Adapter os métodos necessários para exibir os dados na lista.
    //Ess adaptador ele foi criado para lidar com um tipo específico de ViewHolder (o ContactViewHolder que vou criar logo abaixo).
    //No caso eu determinei o tipo ao colocar entre o operador diamante <ContactRvAdapter.ContactViewHolder>
    ): RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {
        //Antes era opcional criar um holdar no ListView, mas no RecyclerView é obrigatório.
        // Então eu criei uma inner class que nada mais é do que uma classe dentro de outra classe.
        inner class ContactViewHolder(
            //O construtor da classe ContactViewHolder recebe um objeto do tipo TileContactBinding como parâmetro.
            tileContactBinding: TileContactBinding
        //Agora aqui eu estou herdando de RecyclerView.ViewHolder para poder os métodos metódos necessários para exibir os dados na lista
        //Eu passo o tileContactBinding.root como parâmetro para o construtor da superclasse ViewHolder para que ele possa acessar
        // a view raiz do layout e os elementos do layout.
        ): RecyclerView.ViewHolder(tileContactBinding.root) {
            val nameTv = tileContactBinding.nameTv
            val phoneTv = tileContactBinding.phoneTv
            val emailTv = tileContactBinding.emailTv

        //vou criar aqui um bloco init que é um bloco de inicialização que é executado quando a classe é instanciada.
            init {
                tileContactBinding.root.apply {
                    //aqui eu chamo o método onContactClick do listener passando a posição do contato na lista
                    //No recicler view, eu preciso criar um menu de contexto para cada item da lista
                    setOnCreateContextMenuListener { menu, _, _ ->
                        (onContactClickListener as AppCompatActivity).menuInflater.inflate(
                            R.menu.context_menu_main, menu
                        )
                        menu.findItem(R.id.removeContactMi).setOnMenuItemClickListener {
                            onContactClickListener.onRemoveContactMenuItemClick(adapterPosition)
                            true
                        }
                        menu.findItem(R.id.editContactMi).setOnMenuItemClickListener {
                            onContactClickListener.onEditContactMenuItemClick(adapterPosition)
                            true
                        }
                    }
                    //todo holder tem uma referencia para uma view associada ao holder
                    setOnClickListener {
                        //aqui eu chamo o método onContactClick do listener passando a posição do contato na lista
                        onContactClickListener.onContactClick(adapterPosition)
                    }
                }
            }

        }

    //agora vamos criar os métodos que são obrigatórios para o RecyclerView.Adapter

    //o getItemCount é um método que retorna o número de itens na lista de contatos.
    //Ele é chamado pelo RecyclerView para saber quantos itens ele precisa exibir.
    //E é o primeiro método que você precisa implementar.
    override fun getItemCount() = contactList.size

    //O segundo método implementado é o onCreateViewHolder
    //Ele é chamado quando o RecyclerView precisa de um novo ViewHolder para exibir um item na lista.
    //Aqui ele retonar um objeto do tipo ContactViewHolder que é a classe que criamos acima.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TileContactBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            //com o método run eu devolvo um objeto diferente do que foi passado como parâmetro
            ContactViewHolder(this)
        }



    //O terceiro método que precisamos implementar é o onBindViewHolder
    //Ele é que vai fazer o preecnhimento ou a substitição das céluals quando necessário.
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        //Aqui eu pego o contato na posição da lista
        contactList[position].also { contact ->
            with(holder) {
                //Aqui eu preencho os campos do layout com os dados do contato
                holder.nameTv.text = contact.name
                holder.phoneTv.text = contact.phone
                holder.emailTv.text = contact.email
            }
        }
    }
}