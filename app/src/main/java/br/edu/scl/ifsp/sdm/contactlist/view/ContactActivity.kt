package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {
        private val acb: ActivityContactBinding by lazy {
            ActivityContactBinding.inflate(layoutInflater)
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)

        //Precisamos veririfcar se a intent que chamou essa activity recebeu algum extra quando chamou essa intent de abertura .
        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        //se por acaso esse received contact for diferente de nulo, eu posso usar esses dados para preencher os meus campos de formulário.
        receivedContact?.let { received ->
            //verificar se recebeu mais algum extra além do contato.
            val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
            with(acb){
                if (viewContact) {
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = GONE
                }
                nameEt.setText(received.name)
                addressEt.setText(received.address)
                phoneEt.setText(received.phone)
                emailEt.setText(received.email)
            }
        }


        //adicionar um listenet ao botão save. Criei com o with pois vou ter que acessar vários elementos da view
        with(acb){
            saveBt.setOnClickListener {
                //criar um contato com os dados que foram preenchidos no formulário
                val contact = Contact(
                    //Aqui eu posso tanto estar editando um contato como recebendo um contato novo. Se eu estiver editando um contato eu vou receber o id dele.
                    //Se ele estiver nulo, significa que eu estou criando um novo contato. Nesse caso, eu posso usar o hashCode() para gerar um id único.
                    //Por isso eu usei o operador elvis ?: para verificar se o id é nulo. Se for nulo, ele vai usar o hashCode() para gerar um id.
                    id = receivedContact?.id?:hashCode(),
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString()
                )
                //criar um intent para retornar o contato para a activity que chamou essa
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_CONTACT, contact)
                }
                //setResult é um método que seta o resultado da activity. O primeiro parametro é o resultado (RESULT_OK é um
                //resultado padrão do android que indica que a operação foi bem sucedida), e o segundo é o intent que vai
                //retornar o contato
                setResult(RESULT_OK, resultIntent)
                //finish é um método que finaliza a activity
                finish()
            }
        }
    }
}