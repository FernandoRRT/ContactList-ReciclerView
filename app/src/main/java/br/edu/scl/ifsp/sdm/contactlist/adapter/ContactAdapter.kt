package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
// essa classe herda de ArrayAdapter, o que significa que ela será usada para exibir uma lista de objetos do tipo Contact
//Como vou ter que instanciar essa classe para exibir a lista de contatos, preciso passar os parâmetros que serão usados para
// inicializar o adapter.
class ContactAdapter(
    //Primeiro parâmetro do construtor da classe. Ele representa o contexto da aplicação ou da activity onde o Adapter será usado.
    //geralmente uma Activity, Application, ou outro tipo de Context
    context: Context,
    //Aqui, o construtor da classe ArrayAdapter é chamado, passando o context, o layout que será usado para exibir cada item da lista (R.layout.tile_contact), e a contactlist, que contém os dados a serem exibidos.
    private val contactlist: MutableList<Contact>):
    ArrayAdapter<Contact>(
        //que nem mencionado acima preciso passar o context para que o adapter tenha acesso a métodos e recursos do sistema
        // como por exemplo o layoutInflater
        context,
        //o layout que será usado para exibir cada item da lista
        R.layout.tile_contact,
        //a lista de contatos que será exibida
        contactlist) {

        //preciso sobrescrever o método getView para que eu possa customizar a exibição de cada item da lista
        override fun getView(
            //o primeiro parâmetro é a posição do item na lista
            position: Int,
            //o segundo parâmetro é a view que será exibida. Se a view for nula, o adapter vai inflar o layout que foi passado no construtor
            //Isso por que ele pode ser uma célula reciclada. Se não for nula, ele vai reutilizar a view que foi passada
            convertView: View?,
            //O layout pai que contém os itens da lista (geralmente um ListView ou RecyclerView).
            parent: ViewGroup): View {

            //aqui buscamos o contato na posição da lista
            val contact = contactlist[position]

            //Se convertView não for nulo, o código reutilizará a View existente para evitar inflar uma nova.
            var contactTileView = convertView

            //Verifica se a View fornecida em convertView é nula. Se for nula, significa que a View ainda não foi criada e, portanto, é necessário inflar uma nova.
            if (contactTileView == null) {
                //aqui criamos uma variável para armazenar o binding do layout tile_contact
                //Estamos usand ViewBinding para inflar o layout tile_contact. para isso passamos três parâmetros:
               val tcb = TileContactBinding.inflate(
                    // No primeiro parãmetro, eu preciso de um LayouInflater mas eu não tenho ele diretamente que nem quando estou dentro de uma activity.
                    // O getSystemService() é um método da classe Context usado para obter um serviço específico do sistema.
                    // como o serviço de layout, serviço de conectividade, serviço de áudio, etc.
                    // Esse método aceita uma constante que identifica qual serviço você deseja obter. Aqui, especificamente
                    // queremos um que retorna um objeto do tipo LayoutInflater. O "as LayoutInflater" é um cast para garantir
                    // que o serviço retornado seja do tipo LayoutInflater. Se não usar o cast, ele retornaria um tipo Any?.
                    // Ou seja, do tipo mais genérico, que pode ser qualquer coisa ou nulo. Por isso  você precisa "converter"
                    // o valor retornado no tipo específico que deseja usar.
                    context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                    // O parent é o layout pai no qual a nova View seria adicionada (normalmente o ListView ou RecyclerView).
                    // Aqui, ele é usado para calcular corretamente as propriedades da View inflada, mas a View não será adicionada ao parent automaticamente.
                    parent,
                    // false: Indica que a View inflada não deve ser automaticamente adicionada ao parent. No contexto de um adapter,
                    // o framework irá adicionar a View ao layout apropriado (como ListView ou RecyclerView) em outro momento.
                    false
                )
                //A View raiz do layout inflado é atribuída a contactTileView. Isso garante que a View correta seja exibida.
                contactTileView = tcb.root

                //Esse código cria uma instância de TileContactHolder, que é uma classe que armazena referências para as TextView associadas a cada célula do item de lista
                // (no caso, nameTv e emailTv). Aqui, os TextViews do layout tile_contact são atribuídos ao TileContactHolder.
                //Isso permite que acessá-los facilmente depois, sem a necessidade de chamar findViewById() repetidamente, o que melhora o desempenho.
                val tileContactHolder = TileContactHolder(tcb.nameTv, tcb.emailTv, tcb.phoneTv)

                //Agora que tenho o holder e ele está apontando para os elementos dentro da célula. Preciso saber que a célula saiba quem é o holder dela.
                //Aqui, o tag é definido como tileContactHolder. Isso permite que o TileContactHolder seja acessado posteriormente.
                //Cada View tem uma propriedade tag, que pode ser usada para "guardar" qualquer tipo de objeto.
                //Aqui, estamos armazenando o TileContactHolder no contactTileView usando a propriedade tag.
                // Isso será útil para recuperar esse holder posteriormente sem precisar recriar a lógica de busca dos elementos da UI.
                contactTileView.tag = tileContactHolder
            }

            //Crio um holder para armazenar o binding da view
            //Aqui, o código está recuperando o TileContactHolder que foi armazenado anteriormente usando tag.
            // A palavra-chave "as" é usada para fazer o cast de volta para o tipo TileContactHolder.
            //Como o tag é um tipo Any, ela pode armazenar e retornar qualquer objeto. Quando eu faço o cast para TileContactHolder
            // eu estou avisando que é esse tipo de objeto que eu tenho interesse.
            val holder = contactTileView.tag as TileContactHolder
            //Agora que recuperamos o holder, os campos são atualizados com as informações de contato
            holder.nameTv.text = contact.name
            holder.emailTv.text = contact.email
            holder.phoneTv.text = contact.phone

            //Retorna a view que foi inflada
            //Após inflar a View e configurar os valores dos campos com as informações do contato, a View (contactTileView) é retornada.
            // Esse é o processo básico de criação de uma célula de lista em um adapter, onde cada item da lista é uma célula customizada.
            return contactTileView
        }

    //Classe interna que armazena o binding da view
    //No Kotlin é data class fornece implementações automáticas para algumas funcionalidades úteis, como: equals(), hashCode() e toString().
    //Aqui, a classe TileContactHolder é uma data class que armazena referências para os elementos de UI (TextViews) dentro de cada célula da lista.
    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView, val phoneTv: TextView)
}